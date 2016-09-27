package org.ethtrader.ticker

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import org.glassfish.jersey.client.ClientProperties
import java.lang.System.clearProperty
import java.lang.System.setProperty
import java.math.BigDecimal
import java.net.URL
import java.security.SecureRandom
import java.security.cert.X509Certificate
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager
import javax.ws.rs.client.Client
import javax.ws.rs.client.ClientBuilder

class DataReader(providers: Map<String, ProviderConf>) {

    private val client = ClientBuilder.newBuilder()
            .property(ClientProperties.CONNECT_TIMEOUT, 10000)
            .property(ClientProperties.READ_TIMEOUT, 10000)
            .sslContext(sslContext())

    private val objectMapper = ObjectMapper()
            .registerKotlinModule()

    private val providerFunctions = providers.map { provider ->
        provider.key to { values: List<String> ->
            try {
                val response = readObject(provider.value.url.format(*values.toTypedArray()))
                println("JSON Response form provider [${provider.key}], $values: $response")
                println("Reading fields: ${provider.value.fields}")
                response.parseFields(provider.value.fields)
            } catch (e: Throwable) {
                println("Unexpected exception: " + e.printStackTrace())
                mapOf<String, BigDecimal>()
            }
        }
    }.toMap()

    fun read(dataPoints: Map<String, List<String>>): Map<String, Map<String, BigDecimal?>?> {
        val data = dataPoints.map {
            val function = providerFunctions.get(it.value.first())
            it.key to if (function == null) null else function(it.value.drop(1))
        }.toMap()
        println("Data points retrieved:\n - ${data.map { it }.joinToString("\n - ")}")
        return data
    }

    private fun JsonNode.parseFields(fields: Map<String, String>): Map<String, BigDecimal?> =
            fields.map {
                it.key to try {
                    val text = at(it.value).asText()
                    if (text.isNullOrEmpty()) BigDecimal.ZERO else BigDecimal(text)
                } catch(t: Throwable) {
                    null
                }
            }.toMap()

    private fun readObject(url: String, attempts: Int = 3): JsonNode {
        println("Fetching from URL: $url")
        return retry(times = 10) {
            Thread.sleep(1000)
            val json = client.build().target(url).request().get(String::class.java)
            objectMapper.readTree(json)
        }
    }

    private fun sslContext(): SSLContext? {
        val sslContext = SSLContext.getInstance("SSL")
        sslContext.init(null, arrayOf(object : X509TrustManager {
            override fun checkClientTrusted(p0: Array<out X509Certificate>?, p1: String?) = Unit
            override fun checkServerTrusted(p0: Array<out X509Certificate>?, p1: String?) = Unit
            override fun getAcceptedIssuers(): Array<out X509Certificate>? = null
        }), SecureRandom())
        return sslContext
    }
}

fun <T> retry(times: Int, total: Int = times, action: () -> T): T = try {
    Thread.sleep(total.toLong() - times)
    action()
} catch (e: Throwable) {
    if (times > 0) retry(times - 1, total, action) else throw e
}