package org.ethtrader.ticker

import org.jsoup.Jsoup
import java.io.InputStream

class Reddit(private val client: OAuthClient, val subRedditName: String) {

    fun uploadImage(name: String, ticker: InputStream) {
        val response = client.postRSubredditUpload_sr_img(subRedditName,
                "file" to ticker,
                "name" to name,
                "upload_type" to "img")
        println("Response: " + response)
    }

    fun uploadStylesheet(css: String) {
        val response = client.postRSubredditSubreddit_stylesheet(subRedditName,
                "stylesheet_contents" to css,
                "op" to "save")
        println("Response: " + response)
    }

    fun getStylesheet(): String = retry(3) {
        val cssPage = client.requestPlain("/r/$subRedditName/about/stylesheet/").get(String::class.java)
        val document = Jsoup.parse(cssPage)
        document.select("pre.subreddit-stylesheet-source > code").text()
    }
}