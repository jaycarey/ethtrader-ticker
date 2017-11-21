package org.ethtrader.ticker

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.node.ArrayNode
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import org.glassfish.jersey.client.ClientProperties
import java.math.BigDecimal
import java.security.SecureRandom
import java.security.cert.X509Certificate
import javax.net.ssl.SSLContext
import javax.net.ssl.X509TrustManager
import javax.ws.rs.client.ClientBuilder

class DataReader(providers: Map<String, ProviderConf>) {

    private val client = ClientBuilder.newBuilder()
            .property(ClientProperties.CONNECT_TIMEOUT, 10000)
            .property(ClientProperties.READ_TIMEOUT, 10000)
            .sslContext(sslContext())

    private val objectMapper = ObjectMapper()
            .registerKotlinModule()

    private val providerFunctions = providers.map { provider ->
        var cache = mapOf<String, JsonNode>()
        provider.key to { values: List<String> ->
            try {
                val url = provider.value.url.format(*values.toTypedArray())
                var rootNode = cache[url]
                if (rootNode == null) {
                    rootNode = readObject(url)
                    cache += url to rootNode
                }
                if (provider.value.filter != null) {
                    rootNode = rootNode.find {
                        it.at(provider.value.filter).asText() == values.last()
                    }
                }
                if (rootNode is ArrayNode && rootNode.size() == 1) {
                    rootNode = rootNode.first()
                }
                println("JSON Response form provider [${provider.key}], $values: $rootNode")
                println("Reading fields: ${provider.value.fields}")
                rootNode?.parseFields(provider.value.fields)
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
                } catch (t: Throwable) {
                    null
                }
            }.toMap()

    private fun readObject(url: String): JsonNode {
        return retry(times = 10) {
            Thread.sleep(300)
            val urlAndQuery = url.split("?")
            var target = client.build().target(urlAndQuery[0])
            target = urlAndQuery.getOrNull(1)?.split(",")?.map { it.split("=") }?.fold(target) { t, k -> t.queryParam(k[0], k[1]) } ?: target
            println("Fetching from URL: $target")
            val request = target.request()
            val response = request.get()
            val json = response.readEntity(String::class.java)
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