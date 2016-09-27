package com.ethtrader.ticker

import org.jsoup.Jsoup
import org.jsoup.examples.HtmlToPlainText
import org.jsoup.nodes.Element
import org.jsoup.nodes.Node
import org.jsoup.nodes.TextNode
import org.junit.Test
import java.io.BufferedWriter
import java.io.File
import java.util.*
import kotlin.text.RegexOption.MULTILINE

class RedditApiGeneration {

    private val url = "https://www.reddit.com/dev/api"
    private val apiStart = "// Generated API start."
    private val apiEnd = "// Generated API end."
    private val outputFile = File("src/main/java/${javaClass.`package`.name.replace(".", "/")}/Api.kt")

    @Test fun generateApi() {
        //                val document = Jsoup.parse(URL(url), TimeUnit.MINUTES.toMillis(1).toInt())
        val document = Jsoup.parse(javaClass.getResourceAsStream("/api.html"), Charsets.UTF_8.name(), url)
        val lines = outputFile.readLines()

        val writer = outputFile.bufferedWriter()
        try {
            lines.takeWhile { !it.equals(apiStart) }.forEach { writer.write("$it\n") }
            writer.write("$apiStart\n\n")
            document.select("div.section.methods > div").forEach { parseEndpoint(writer, it) }
            writer.write("\n$apiEnd\n\n")
        } catch(throwable: Throwable) {
            lines.dropWhile { !it.equals(apiEnd) }.forEach { writer.write("$it\n") }
        } finally {
            writer.close()
        }
    }

    private val types = mapOf(
            "comma-separated list" to "List<String>",
            "comma-delimited list" to "List<String>",
            "a positive integer" to "Int",
            "number of" to "Int",
            "expand subreddits" to "Boolean",
            "true, false" to "Boolean"
    ).withDefault { "String" }

    private fun parseEndpoint(writer: BufferedWriter, endpointElement: Element) {
        val queryParameters = parseQueryParameters(endpointElement)
        val method = parseEndpointAndGetMethod(endpointElement, writer, "get", endpointElement.selectFirst("h3")?.childNodes(), queryParameters)
        endpointElement.selectFirst("ul.uri-variants")?.childNodes()?.forEach {
            parseEndpointAndGetMethod(endpointElement, writer, method, it.childNodes(), queryParameters)
        }
    }

    private fun parseEndpointAndGetMethod(endpointElement: Element, writer: BufferedWriter, previousMetho: String, urlElements: List<Node>?, urlParams: Map<String, Param>): String {

        val (method, pathParams, url) = parseUrl(previousMetho, urlElements)
        val get = method == "get"

        val urlParamsAndType = urlParams.filterNot { pathParams.contains(it.value.name) }.map { it.value.parameterName to it.value.type + "?" }
        val pathParamsAndType = pathParams.map { it to (urlParams.get(it)?.type ?: "String") }

        val urlParamsPart = if (get) urlParamsAndType.map { "${it.first}: ${it.second}" } else listOf("vararg urlParams: Pair<String, Any?>")

        val methodParams = (pathParamsAndType.map { "${it.first}: ${it.second}" } + urlParamsPart).joinToString(", ")

        val paramsString = if (!get || urlParams.isEmpty()) "" else urlParams.map { "\n                \"${it.key}\" to ${format(it.value)}" }.joinToString(", ")
        val text = HtmlToPlainText().getPlainText(endpointElement).replace("\n\n", "\n - ").replace(Regex("\n - $", MULTILINE), "")

        val requestMethod = when (method) {
            "get" -> "get()"
            "post" -> "post(urlParams.multipart())"
            else -> "method(\"$method\")"
        }

        val name = method + url.replaceFirst("/api", "").replace("$", "").split("/", "[", "]").map { it.capitalize() }.joinToString ("")
        val readResponse = if (url.startsWith("/api")) "getResponse()" else "readEntity(String::class.java)"

        if (name.isNotEmpty()) {
            if (get) {
                writer.write("""
/*
 * ${text.lines().joinToString("\n * ")}
 **/
fun OAuthClient.$name($methodParams) =
        retry(3) { requestApi("$url"${if (paramsString.isEmpty()) "" else ", " + paramsString}).$requestMethod.$readResponse }
""")
            } else {
                writer.write("""
/*
 * ${text.lines().joinToString("\n * ")}
 **/
fun OAuthClient.$name($methodParams) =
        retry(3) { requestApi("$url").$requestMethod.$readResponse }
""")
            }
        }
        return method
    }

    private fun parseUrl(originalMethod: String, urlNodes: List<Node>?): Triple<String, List<String>, String> {
        var method = originalMethod
        var queryParameters = listOf<String>()
        var url = ""
        urlNodes?.forEach {
            when (it) {
                is TextNode -> url += it.text().replace("[", "").replace("]", "")
                is Element -> {
                    it.useText("span.method") { method = it }
                    it.useText("em.placeholder") { url += "$$it" }
                    it.useText("em.placeholder") { queryParameters += it }
                }
            }
        }
        return Triple(method.toLowerCase(Locale.ENGLISH), queryParameters, url.replace("→ ", ""))
    }

    private fun parseQueryParameters(endpointElement: Element): Map<String, Param> {
        val select = endpointElement.selectFirst("div.info")?.children()?.select("table.parameters > tbody > tr")
        return select?.map { row -> row.selectFirst("td > p")?.parseParameter(row) }?.filterNotNull()?.toMap() ?: mapOf()
    }

    private fun Element.parseParameter(row: Element): Pair<String, Param> {
        val text = text().split("(", ")").map { it.trim() }
        val name = row.selectFirst("th")!!.text().split(" ").first()
        val type = types.entries.firstOrNull { text.any { t -> t.contains(it.key) } }?.value ?: "String"
        val parameterName = name.split('|', '_', '-').map { it.capitalize() }.joinToString("").decapitalize()
        val optional = text.contains("default:") || text.contains("optional")
        return name to Param(name, optional, type, parameterName)
    }

    private fun format(it: Param) = when (it.type) {
        "List<String>" -> it.parameterName + ".csv()"
        else -> it.parameterName
    }

    private fun Element.useText(selector: String, action: (String) -> Unit) {
        val text = select(selector)?.text()?.trim()
        if (text != null && text != "") action(text)
    }

    data class Param(val name: String, val optional: Boolean, val type: String, val parameterName: String)

    fun Element.selectFirst(selector: String): Element? =
            select(selector).first()
}
