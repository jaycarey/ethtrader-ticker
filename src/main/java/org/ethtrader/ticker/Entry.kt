package org.ethtrader.ticker

import java.io.File
import java.net.CookieHandler
import java.net.CookieManager
import java.net.URL
import java.util.concurrent.TimeUnit

fun main(args: Array<String>) {
    try {
        CookieHandler.setDefault(CookieManager(null, java.net.CookiePolicy.ACCEPT_ALL));

        fun getResourceUrl(path: String): URL = URL(args[5] + path)

        val outputDir = File("output")
        outputDir.mkdirs()

        while (true) {
            try {
                val config = TickerEthConfig(::getResourceUrl)
                val reader = DataReader(config.providers)

                val tickerFactory = TickerFactory(outputDir, ::getResourceUrl)
                val client = OAuthClient(config.reddit.apiUrl, AuthInfo(args[1], args[2], args[3], args[4]))
                val reddit = Reddit(client, args[0])
                val test = args.contains("--test")

                val data = reader.read(config.dataPoints)
                config.tickers.entries.forEach {
                    try {
                        val (name, tickerConfig) = it
                        val ticker = tickerFactory.produceTicker(name, data, tickerConfig)
                        if (!test) reddit.uploadImage(name, ticker)
                    } catch (e: Exception) {
                        println("ERROR: " + e.message)
                        e.printStackTrace()
                    }
                }

                try {
                    val oldCss = reddit.getStylesheet()
                    File(outputDir, "old.css").writeTextAndLog(oldCss, "Wrote old css to %s")
                    val newCss = tickerFactory.getAndResetCss(oldCss)
                    File(outputDir, "new.css").writeTextAndLog(newCss, "Wrote new css to %s")

                    if (!test) reddit.uploadStylesheet(newCss)
                } catch (e: Throwable) {
                    println("Error trying to update tickers")
                    e.printStackTrace()
                }
                println("Waiting ${config.reddit.frequencySeconds} seconds.")
                Thread.sleep(TimeUnit.SECONDS.toMillis(config.reddit.frequencySeconds))
                if (test) break;
            } catch (e: Throwable) {
                System.err.println("Unexpected error while running ticker." + e)
            }
        }
    } catch (e: Throwable) {
        System.err.println("Usage: <<subreddit>> <<username>> <<password>> <<clientId>> <<secret>> <<resourceUrl>>")
        System.err.println("clientId and secret can be administered here: https://www.reddit.com/prefs/apps/")
        System.err.println("resourceUrl points to the configuration/resources to build the ticker.")
        System.err.println("\n Error: ${e.message}")
        e.printStackTrace()
    }
}

fun File.writeTextAndLog(text: String, message: String) {
    writeText(text)
    println(message.format(absolutePath))
}
