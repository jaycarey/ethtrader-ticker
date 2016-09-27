package org.ethtrader.ticker

import com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.PropertyNamingStrategy.SNAKE_CASE
import com.fasterxml.jackson.jaxrs.json.JacksonJaxbJsonProvider
import com.fasterxml.jackson.module.kotlin.readValue
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import org.glassfish.jersey.client.authentication.HttpAuthenticationFeature.basicBuilder
import org.glassfish.jersey.media.multipart.MultiPartFeature
import java.io.Serializable
import java.time.Instant.now
import javax.ws.rs.client.ClientBuilder.newBuilder
import javax.ws.rs.client.Entity
import javax.ws.rs.client.Invocation
import javax.ws.rs.core.HttpHeaders.AUTHORIZATION
import javax.ws.rs.core.HttpHeaders.USER_AGENT
import javax.ws.rs.core.MediaType

data class AuthInfo(val username: String, val password: String, val clientId: String, val secret: String) : Serializable
data class AuthData(val accessToken: String?, val expiresIn: Int, val scope: String?, val tokenType: String?)

class OAuthClient(private val apiUrl: String, private val authInfo: AuthInfo) {

    private var authData: AuthData? = null
    private var timeTokenAcquired = -1L

    private val objectMapper = ObjectMapper()
            .registerKotlinModule()
            .setPropertyNamingStrategy(SNAKE_CASE)
            .disable(FAIL_ON_UNKNOWN_PROPERTIES)

    private val client = newBuilder()
            .register(basicBuilder().credentials(authInfo.clientId, authInfo.secret).build())
            .register(JacksonJaxbJsonProvider(objectMapper, emptyArray()))
            .register(MultiPartFeature())
            .build()

    fun renewAccessToken() {
        if (!newTokenNeeded()) return
        val post = client
                .target("https://www.reddit.com/api/v1/")
                .queryParam("grant_type", "password")
                .queryParam("username", authInfo.username)
                .queryParam("password", authInfo.password)
                .path("access_token")
                .request()
                .post(Entity.text(""))
        authData = objectMapper.readValue(post.readEntity(String::class.java))
        timeTokenAcquired = now().epochSecond
    }

    private fun newTokenNeeded(): Boolean {
        val authData = authData
        return authData == null || now().epochSecond >= timeTokenAcquired + authData.expiresIn - 180
    }

    fun requestApi(path: String, vararg urlParameters: Pair<String, Any?>): Invocation.Builder {
        return request(apiUrl, path, urlParameters, MediaType.APPLICATION_JSON_TYPE)
                .header(AUTHORIZATION, "bearer " + authData?.accessToken)
    }

    fun requestPlain(path: String, vararg urlParameters: Pair<String, Any?>): Invocation.Builder {
        return request("https://www.reddit.com/", path, urlParameters, MediaType.WILDCARD_TYPE)
    }

    private fun request(baseUrl: String, path: String, urlParameters: Array<out Pair<String, Any?>>, mediaType: MediaType?): Invocation.Builder {
        println("Calling $path, parameters: ${if (urlParameters.isEmpty()) "" else "\n - "}${urlParameters.filter { it.second != null }.joinToString("\n - ")}")
        renewAccessToken()
        return urlParameters.filter { it.second != null }.fold(client.target(baseUrl).path(path)) { q, it -> q.queryParam(it.first, it.second) }
                .request()
                .accept(mediaType)
                .header(USER_AGENT, "kotlin:org.ethtrader:0.1 (by /u/etherboard)")
    }
}
