package org.ethtrader.ticker

import com.typesafe.config.Config
import com.typesafe.config.ConfigFactory
import java.awt.Color
import java.lang.Long
import java.net.URL
import kotlin.reflect.KFunction1

class TickerEthConfig(getResourceUrl: (String) -> URL) {
    init {
        System.setProperty("java.awt.headless", "true")
    }

    private val conf = ConfigFactory.parseURL(getResourceUrl("/ticker.conf")).getConfig("ticker")
    private val providerConf = conf.getConfig("providers")
    private val dataPointConf = conf.getConfig("dataPoints")
    private val tickerConf = conf.getConfig("tickers")

    val reddit = RedditConf(conf)
    val providers = providerConf.keys().map { it to ProviderConf(providerConf, it) }.toMap()
    val dataPoints = dataPointConf.keys().map { it to dataPointConf.getStringList(it) }.toMap()
    val tickers = tickerConf.keys().map { it to TickerConf(tickerConf, it) }.toMap()
}

class TickerConf(config: Config, name: String) {
    private val tickerConf = config.getConfig(name)
    val font = tickerConf.getString("font")
    val fontHeight = tickerConf.getInt("fontHeight")
    val color = tickerConf.getString("color").toColor()
    val background = tickerConf.getString("background").toColor()
    val backgroundHighlight = tickerConf.getString("backgroundHighlight").toColor()
    val positiveColor = tickerConf.getString("positiveColor").toColor()
    val negativeColor = tickerConf.getString("negativeColor").toColor()
    val segments = tickerConf.getStringList("segments")
    val top = tickerConf.getString("top")
    val cssSelector = tickerConf.getString("cssSelector")
}

fun String.toColor(): Color = if (startsWith("#") && length == 9) java.lang.Long.decode(this).let {
    Color(((it shr 24) and 0xFF).toInt(), ((it shr 16) and 0xFF).toInt(), ((it shr 8) and 0xFF).toInt(), (it and 0xFF).toInt())
} else Color.decode(this)

fun Config.keys(): Set<String> =
        entrySet().map { it.key.split(".", limit = 2).first() }.toSet()

class RedditConf(config: Config) {
    private val redditConf = config.getConfig("reddit")
    val frequencySeconds = redditConf.getLong("frequencySeconds")
    val authUrl = redditConf.getString("authUrl")
    val apiUrl = redditConf.getString("apiUrl")
}

class ProviderConf(config: Config, name: String) {
    private val providerConf = config.getConfig(name)
    private val fieldsConf = providerConf.getConfig("fields")
    val url = providerConf.getString("url")
    val fields: Map<String, String> = fieldsConf.keys().map { it to fieldsConf.getString(it) }.toMap()
}

fun Config.getStringOr(key: String, default: String?) = try {
    getString(key).toString()
} catch(e: Throwable) {
    default
}
