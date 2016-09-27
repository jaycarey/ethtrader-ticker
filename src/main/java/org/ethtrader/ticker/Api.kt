package org.ethtrader.ticker

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.PropertyNamingStrategy
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.fasterxml.jackson.module.kotlin.readValue
import org.glassfish.jersey.media.multipart.FormDataMultiPart
import org.glassfish.jersey.media.multipart.MultiPart
import org.glassfish.jersey.media.multipart.file.StreamDataBodyPart
import java.io.InputStream
import javax.ws.rs.client.Entity
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.Response

data class JsonError(val error: Int)

internal fun List<String>?.csv(): String? =
        this?.joinToString(",")

fun Array<out Pair<String, Any?>>.multipart(): Entity<MultiPart> =
        Entity.entity(fold(FormDataMultiPart()) { multiPart, pair ->
            when (pair.second) {
                is InputStream ->
                    multiPart.bodyPart(StreamDataBodyPart(pair.first, pair.second as InputStream)) as FormDataMultiPart
                else ->
                    multiPart.field (pair.first, pair.second.toString())
            }
        }, MediaType.MULTIPART_FORM_DATA)

private val objectMapper = ObjectMapper()
        .registerModule(KotlinModule())
        .setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE)
        .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)

private fun Response.getResponse(): ResponseWrapper {
    val string = readEntity(String::class.java)
    println(string)
    try {
        return objectMapper.readValue(string)
    } catch (e: Throwable) {
        val error: JsonError = objectMapper.readValue(string)
        throw RuntimeException("Error Json Object returned, error code: ${error.error}", e)
    }
}

data class ResponseWrapper(val kind: String?, val errors: List<String>?, val data: Map<String, Any?>?)

// Generated API start.


/*
 * view code 
 * <https://github.com/reddit/reddit/blob/master/r2/r2/controllers/apiv1/user.py#L57>
 * # <https://www.reddit.com/dev/api#GET_api_v1_me>GET /api/v1/meidentity 
 * <https://github.com/reddit/reddit/wiki/OAuth2>
 *  - Returns the identity of the user currently authenticated via OAuth.
 **/
fun OAuthClient.getV1Me() =
        retry(3) { requestApi("/api/v1/me").get().getResponse() }

/*
 * view code 
 * <https://github.com/reddit/reddit/blob/master/r2/r2/controllers/apiv1/user.py#L106>
 * # <https://www.reddit.com/dev/api#GET_api_v1_me_karma>GET /api/v1/me/karma
 * mysubreddits <https://github.com/reddit/reddit/wiki/OAuth2>
 *  - Return a breakdown of subreddit karma.
 **/
fun OAuthClient.getV1MeKarma() =
        retry(3) { requestApi("/api/v1/me/karma").get().getResponse() }

/*
 * view code 
 * <https://github.com/reddit/reddit/blob/master/r2/r2/controllers/apiv1/user.py#L67>
 * # <https://www.reddit.com/dev/api#GET_api_v1_me_prefs>GET /api/v1/me/prefs
 * identity <https://github.com/reddit/reddit/wiki/OAuth2>
 *  - Return the preference settings of the logged in user
 *  - fieldsA comma-separated list of items from this set:
 *  - threaded_messages
 * hide_downs
 * email_messages
 * show_link_flair
 * creddit_autorenew
 * show_trending
 * private_feeds
 * monitor_mentions
 * research
 * ignore_suggested_sort
 * media
 * clickgadget
 * use_global_defaults
 * label_nsfw
 * domain_details
 * show_stylesheets
 * highlight_controversial
 * no_profanity
 * default_theme_sr
 * lang
 * hide_ups
 * hide_from_robots
 * compress
 * store_visits
 * threaded_modmail
 * beta
 * other_theme
 * show_gold_expiration
 * over_18
 * enable_default_themes
 * show_promote
 * min_comment_score
 * public_votes
 * organic
 * collapse_read_messages
 * show_flair
 * mark_messages_read
 * hide_ads
 * min_link_score
 * newwindow
 * numsites
 * legacy_search
 * num_comments
 * highlight_new_comments
 * default_comment_sort
 * hide_locationbar
 **/
fun OAuthClient.getV1MePrefs(fields: List<String>?) =
        retry(3) { requestApi("/api/v1/me/prefs",
                "fields" to fields.csv()).get().getResponse() }

/*
 * view code 
 * <https://github.com/reddit/reddit/blob/master/r2/r2/controllers/apiv1/user.py#L123>
 * # <https://www.reddit.com/dev/api#PATCH_api_v1_me_prefs>PATCH /api/v1/me/prefs
 * account <https://github.com/reddit/reddit/wiki/OAuth2>
 *  - This endpoint expects JSON data of this format{ "beta": boolean value, 
 * "clickgadget": boolean value, "collapse_read_messages": boolean value, 
 * "compress": boolean value, "creddit_autorenew": boolean value, 
 * "default_comment_sort": one of (`confidence`, `old`, `top`, `qa`, 
 * `controversial`, `new`), "domain_details": boolean value, "email_messages": 
 * boolean value, "enable_default_themes": boolean value, "hide_ads": boolean 
 * value, "hide_downs": boolean value, "hide_from_robots": boolean value, 
 * "hide_locationbar": boolean value, "hide_ups": boolean value, 
 * "highlight_controversial": boolean value, "highlight_new_comments": boolean 
 * value, "ignore_suggested_sort": boolean value, "label_nsfw": boolean value, 
 * "lang": a valid IETF language tag (underscore separated), "legacy_search": 
 * boolean value, "mark_messages_read": boolean value, "media": one of (`on`, 
 * `off`, `subreddit`), "min_comment_score": an integer between -100 and 100, 
 * "min_link_score": an integer between -100 and 100, "monitor_mentions": boolean 
 * value, "newwindow": boolean value, "no_profanity": boolean value, 
 * "num_comments": an integer between 1 and 500, "numsites": an integer between 1 
 * and 100, "organic": boolean value, "other_theme": subreddit name, "over_18": 
 * boolean value, "private_feeds": boolean value, "public_votes": boolean value, 
 * "research": boolean value, "show_flair": boolean value, "show_gold_expiration": 
 * boolean value, "show_link_flair": boolean value, "show_promote": boolean value, 
 * "show_stylesheets": boolean value, "show_trending": boolean value, 
 * "store_visits": boolean value, "theme_selector": subreddit name, 
 * "threaded_messages": boolean value, "threaded_modmail": boolean value, 
 * "use_global_defaults": boolean value, } 
 **/
fun OAuthClient.patchV1MePrefs(vararg urlParams: Pair<String, Any?>) =
        retry(3) { requestApi("/api/v1/me/prefs").method("patch").getResponse() }

/*
 * view code 
 * <https://github.com/reddit/reddit/blob/master/r2/r2/controllers/apiv1/user.py#L94>
 * # <https://www.reddit.com/dev/api#GET_api_v1_me_trophies>GET /api/v1/me/trophies
 * identity <https://github.com/reddit/reddit/wiki/OAuth2>
 *  - Return a list of trophies for the current user.
 **/
fun OAuthClient.getV1MeTrophies() =
        retry(3) { requestApi("/api/v1/me/trophies").get().getResponse() }

/*
 * view code 
 * <https://github.com/reddit/reddit/blob/master/r2/r2/controllers/listingcontroller.py#L1818>
 * # <https://www.reddit.com/dev/api#GET_prefs_{where}>GET /prefs/whereread 
 * <https://github.com/reddit/reddit/wiki/OAuth2>rss support 
 * <https://www.reddit.com/wiki/rss>
 *  * → /prefs/friends
 *  * → /prefs/blocked
 *  * → /api/v1/me/friends
 *  * → /api/v1/me/blockedThis endpoint is a listing 
 * <https://www.reddit.com/dev/api#listings>.
 *  - afterfullname <https://www.reddit.com/dev/api#fullnames> of a thing
 *  - beforefullname <https://www.reddit.com/dev/api#fullnames> of a thing
 *  - counta positive integer (default: 0)
 *  - limitthe maximum number of items desired (default: 25, maximum: 100)
 *  - show(optional) the string all
 *  - sr_detail(optional) expand subreddits
 **/
fun OAuthClient.getPrefsWhere(where: String, after: String?, before: String?, count: Int?, limit: Int?, show: String?, srDetail: Boolean?) =
        retry(3) { requestApi("/prefs/$where",
                "after" to after, 
                "before" to before, 
                "count" to count, 
                "limit" to limit, 
                "show" to show, 
                "sr_detail" to srDetail).get().readEntity(String::class.java) }

/*
 * view code 
 * <https://github.com/reddit/reddit/blob/master/r2/r2/controllers/listingcontroller.py#L1818>
 * # <https://www.reddit.com/dev/api#GET_prefs_{where}>GET /prefs/whereread 
 * <https://github.com/reddit/reddit/wiki/OAuth2>rss support 
 * <https://www.reddit.com/wiki/rss>
 *  * → /prefs/friends
 *  * → /prefs/blocked
 *  * → /api/v1/me/friends
 *  * → /api/v1/me/blockedThis endpoint is a listing 
 * <https://www.reddit.com/dev/api#listings>.
 *  - afterfullname <https://www.reddit.com/dev/api#fullnames> of a thing
 *  - beforefullname <https://www.reddit.com/dev/api#fullnames> of a thing
 *  - counta positive integer (default: 0)
 *  - limitthe maximum number of items desired (default: 25, maximum: 100)
 *  - show(optional) the string all
 *  - sr_detail(optional) expand subreddits
 **/
fun OAuthClient.getPrefsFriends(after: String?, before: String?, count: Int?, limit: Int?, show: String?, srDetail: Boolean?) =
        retry(3) { requestApi("/prefs/friends",
                "after" to after, 
                "before" to before, 
                "count" to count, 
                "limit" to limit, 
                "show" to show, 
                "sr_detail" to srDetail).get().readEntity(String::class.java) }

/*
 * view code 
 * <https://github.com/reddit/reddit/blob/master/r2/r2/controllers/listingcontroller.py#L1818>
 * # <https://www.reddit.com/dev/api#GET_prefs_{where}>GET /prefs/whereread 
 * <https://github.com/reddit/reddit/wiki/OAuth2>rss support 
 * <https://www.reddit.com/wiki/rss>
 *  * → /prefs/friends
 *  * → /prefs/blocked
 *  * → /api/v1/me/friends
 *  * → /api/v1/me/blockedThis endpoint is a listing 
 * <https://www.reddit.com/dev/api#listings>.
 *  - afterfullname <https://www.reddit.com/dev/api#fullnames> of a thing
 *  - beforefullname <https://www.reddit.com/dev/api#fullnames> of a thing
 *  - counta positive integer (default: 0)
 *  - limitthe maximum number of items desired (default: 25, maximum: 100)
 *  - show(optional) the string all
 *  - sr_detail(optional) expand subreddits
 **/
fun OAuthClient.getPrefsBlocked(after: String?, before: String?, count: Int?, limit: Int?, show: String?, srDetail: Boolean?) =
        retry(3) { requestApi("/prefs/blocked",
                "after" to after, 
                "before" to before, 
                "count" to count, 
                "limit" to limit, 
                "show" to show, 
                "sr_detail" to srDetail).get().readEntity(String::class.java) }

/*
 * view code 
 * <https://github.com/reddit/reddit/blob/master/r2/r2/controllers/listingcontroller.py#L1818>
 * # <https://www.reddit.com/dev/api#GET_prefs_{where}>GET /prefs/whereread 
 * <https://github.com/reddit/reddit/wiki/OAuth2>rss support 
 * <https://www.reddit.com/wiki/rss>
 *  * → /prefs/friends
 *  * → /prefs/blocked
 *  * → /api/v1/me/friends
 *  * → /api/v1/me/blockedThis endpoint is a listing 
 * <https://www.reddit.com/dev/api#listings>.
 *  - afterfullname <https://www.reddit.com/dev/api#fullnames> of a thing
 *  - beforefullname <https://www.reddit.com/dev/api#fullnames> of a thing
 *  - counta positive integer (default: 0)
 *  - limitthe maximum number of items desired (default: 25, maximum: 100)
 *  - show(optional) the string all
 *  - sr_detail(optional) expand subreddits
 **/
fun OAuthClient.getV1MeFriends(after: String?, before: String?, count: Int?, limit: Int?, show: String?, srDetail: Boolean?) =
        retry(3) { requestApi("/api/v1/me/friends",
                "after" to after, 
                "before" to before, 
                "count" to count, 
                "limit" to limit, 
                "show" to show, 
                "sr_detail" to srDetail).get().getResponse() }

/*
 * view code 
 * <https://github.com/reddit/reddit/blob/master/r2/r2/controllers/listingcontroller.py#L1818>
 * # <https://www.reddit.com/dev/api#GET_prefs_{where}>GET /prefs/whereread 
 * <https://github.com/reddit/reddit/wiki/OAuth2>rss support 
 * <https://www.reddit.com/wiki/rss>
 *  * → /prefs/friends
 *  * → /prefs/blocked
 *  * → /api/v1/me/friends
 *  * → /api/v1/me/blockedThis endpoint is a listing 
 * <https://www.reddit.com/dev/api#listings>.
 *  - afterfullname <https://www.reddit.com/dev/api#fullnames> of a thing
 *  - beforefullname <https://www.reddit.com/dev/api#fullnames> of a thing
 *  - counta positive integer (default: 0)
 *  - limitthe maximum number of items desired (default: 25, maximum: 100)
 *  - show(optional) the string all
 *  - sr_detail(optional) expand subreddits
 **/
fun OAuthClient.getV1MeBlocked(after: String?, before: String?, count: Int?, limit: Int?, show: String?, srDetail: Boolean?) =
        retry(3) { requestApi("/api/v1/me/blocked",
                "after" to after, 
                "before" to before, 
                "count" to count, 
                "limit" to limit, 
                "show" to show, 
                "sr_detail" to srDetail).get().getResponse() }

/*
 * view code 
 * <https://github.com/reddit/reddit/blob/master/r2/r2/controllers/api.py#L335># 
 * <https://www.reddit.com/dev/api#GET_api_needs_captcha>GET /api/needs_captchaany 
 * <https://github.com/reddit/reddit/wiki/OAuth2>
 *  - Check whether CAPTCHAs are needed for API methods that define the "captcha" 
 * and "iden" parameters.
 **/
fun OAuthClient.getNeeds_captcha() =
        retry(3) { requestApi("/api/needs_captcha").get().getResponse() }

/*
 * view code 
 * <https://github.com/reddit/reddit/blob/master/r2/r2/controllers/api.py#L145># 
 * <https://www.reddit.com/dev/api#POST_api_new_captcha>POST /api/new_captchaany 
 * <https://github.com/reddit/reddit/wiki/OAuth2>
 *  - Responds with an iden of a new CAPTCHA.
 *  - Use this endpoint if a user cannot read a given CAPTCHA, and wishes to 
 * receive a new CAPTCHA.
 *  - To request the CAPTCHA image for an iden, use /captcha/iden 
 * <https://www.reddit.com/dev/api#GET_captcha_%7Biden%7D>.
 *  - api_typethe string json
 **/
fun OAuthClient.postNew_captcha(vararg urlParams: Pair<String, Any?>) =
        retry(3) { requestApi("/api/new_captcha").post(urlParams.multipart()).getResponse() }

/*
 * view code 
 * <https://github.com/reddit/reddit/blob/master/r2/r2/controllers/captcha.py#L32>#
 *  <https://www.reddit.com/dev/api#GET_captcha_{iden}>GET /captcha/idenany 
 * <https://github.com/reddit/reddit/wiki/OAuth2>
 *  - Request a CAPTCHA image given an iden.
 *  - An iden is given as the captcha field with a BAD_CAPTCHA error, you should 
 * use this endpoint if you get aBAD_CAPTCHA error response.
 *  - Responds with a 120x50 image/png which should be displayed to the user.
 *  - The user's response to the CAPTCHA should be sent as captcha along with your 
 * request.
 *  - To request a new CAPTCHA, use /api/new_captcha 
 * <https://www.reddit.com/dev/api#POST_api_new_captcha>.
 **/
fun OAuthClient.getCaptchaIden(iden: String) =
        retry(3) { requestApi("/captcha/$iden").get().readEntity(String::class.java) }

/*
 * view code 
 * <https://github.com/reddit/reddit/blob/master/r2/r2/controllers/api.py#L4280># 
 * <https://www.reddit.com/dev/api#POST_api_clearflairtemplates>POST [/r/subreddit
 * ]/api/clearflairtemplatesmodflair <https://github.com/reddit/reddit/wiki/OAuth2>
 *  - api_typethe string json
 *  - flair_typeone of (USER_FLAIR, LINK_FLAIR)
 *  - uh / X-Modhash headera modhash <https://www.reddit.com/dev/api#modhashes>
 **/
fun OAuthClient.postRSubredditClearflairtemplates(subreddit: String, vararg urlParams: Pair<String, Any?>) =
        retry(3) { requestApi("/r/$subreddit/api/clearflairtemplates").post(urlParams.multipart()).readEntity(String::class.java) }

/*
 * view code 
 * <https://github.com/reddit/reddit/blob/master/r2/r2/controllers/api.py#L4022># 
 * <https://www.reddit.com/dev/api#POST_api_deleteflair>POST [/r/subreddit
 * ]/api/deleteflairmodflair <https://github.com/reddit/reddit/wiki/OAuth2>
 *  - api_typethe string json
 *  - namea user by name
 *  - uh / X-Modhash headera modhash <https://www.reddit.com/dev/api#modhashes>
 **/
fun OAuthClient.postRSubredditDeleteflair(subreddit: String, vararg urlParams: Pair<String, Any?>) =
        retry(3) { requestApi("/r/$subreddit/api/deleteflair").post(urlParams.multipart()).readEntity(String::class.java) }

/*
 * view code 
 * <https://github.com/reddit/reddit/blob/master/r2/r2/controllers/api.py#L4261># 
 * <https://www.reddit.com/dev/api#POST_api_deleteflairtemplate>POST [/r/subreddit
 * ]/api/deleteflairtemplatemodflair <https://github.com/reddit/reddit/wiki/OAuth2>
 *  - api_typethe string json
 *  - flair_template_iduh / X-Modhash headera modhash 
 * <https://www.reddit.com/dev/api#modhashes>
 **/
fun OAuthClient.postRSubredditDeleteflairtemplate(subreddit: String, vararg urlParams: Pair<String, Any?>) =
        retry(3) { requestApi("/r/$subreddit/api/deleteflairtemplate").post(urlParams.multipart()).readEntity(String::class.java) }

/*
 * view code 
 * <https://github.com/reddit/reddit/blob/master/r2/r2/controllers/api.py#L3968># 
 * <https://www.reddit.com/dev/api#POST_api_flair>POST [/r/subreddit]/api/flair
 * modflair <https://github.com/reddit/reddit/wiki/OAuth2>
 *  - api_typethe string json
 *  - css_classa valid subreddit image name
 *  - linka fullname <https://www.reddit.com/dev/api#fullname> of a link
 *  - namea user by name
 *  - texta string no longer than 64 characters
 *  - uh / X-Modhash headera modhash <https://www.reddit.com/dev/api#modhashes>
 **/
fun OAuthClient.postRSubredditFlair(subreddit: String, vararg urlParams: Pair<String, Any?>) =
        retry(3) { requestApi("/r/$subreddit/api/flair").post(urlParams.multipart()).readEntity(String::class.java) }

/*
 * view code 
 * <https://github.com/reddit/reddit/blob/master/r2/r2/controllers/api.py#L4127># 
 * <https://www.reddit.com/dev/api#POST_api_flairconfig>POST [/r/subreddit
 * ]/api/flairconfigmodflair <https://github.com/reddit/reddit/wiki/OAuth2>
 *  - api_typethe string json
 *  - flair_enabledboolean value
 *  - flair_positionone of (left, right)
 *  - flair_self_assign_enabledboolean value
 *  - link_flair_positionone of (`,left,right`)
 *  - link_flair_self_assign_enabledboolean value
 *  - uh / X-Modhash headera modhash <https://www.reddit.com/dev/api#modhashes>
 **/
fun OAuthClient.postRSubredditFlairconfig(subreddit: String, vararg urlParams: Pair<String, Any?>) =
        retry(3) { requestApi("/r/$subreddit/api/flairconfig").post(urlParams.multipart()).readEntity(String::class.java) }

/*
 * view code 
 * <https://github.com/reddit/reddit/blob/master/r2/r2/controllers/api.py#L4042># 
 * <https://www.reddit.com/dev/api#POST_api_flaircsv>POST [/r/subreddit
 * ]/api/flaircsvmodflair <https://github.com/reddit/reddit/wiki/OAuth2>
 *  - Change the flair of multiple users in the same subreddit with a single API 
 * call.
 *  - Requires a string 'flair_csv' which has up to 100 lines of the form 'user,
 * flairtext,cssclass' (Lines beyond the 100th are ignored).
 *  - If both cssclass and flairtext are the empty string for a given user, instead 
 * clears that user's flair.
 *  - Returns an array of objects indicating if each flair setting was applied, or 
 * a reason for the failure.
 *  - flair_csvcomma-seperated flair information
 *  - uh / X-Modhash headera modhash <https://www.reddit.com/dev/api#modhashes>
 **/
fun OAuthClient.postRSubredditFlaircsv(subreddit: String, vararg urlParams: Pair<String, Any?>) =
        retry(3) { requestApi("/r/$subreddit/api/flaircsv").post(urlParams.multipart()).readEntity(String::class.java) }

/*
 * view code 
 * <https://github.com/reddit/reddit/blob/master/r2/r2/controllers/api.py#L4169># 
 * <https://www.reddit.com/dev/api#GET_api_flairlist>GET [/r/subreddit
 * ]/api/flairlistmodflair <https://github.com/reddit/reddit/wiki/OAuth2>
 *  - This endpoint is a listing <https://www.reddit.com/dev/api#listings>.
 *  - afterfullname <https://www.reddit.com/dev/api#fullnames> of a thing
 *  - beforefullname <https://www.reddit.com/dev/api#fullnames> of a thing
 *  - counta positive integer (default: 0)
 *  - limitthe maximum number of items desired (default: 25, maximum: 1000)
 *  - namea user by name
 *  - show(optional) the string all
 *  - sr_detail(optional) expand subreddits
 **/
fun OAuthClient.getRSubredditFlairlist(subreddit: String, after: String?, before: String?, count: Int?, limit: Int?, name: String?, show: String?, srDetail: Boolean?) =
        retry(3) { requestApi("/r/$subreddit/api/flairlist",
                "after" to after, 
                "before" to before, 
                "count" to count, 
                "limit" to limit, 
                "name" to name, 
                "show" to show, 
                "sr_detail" to srDetail).get().readEntity(String::class.java) }

/*
 * view code 
 * <https://github.com/reddit/reddit/blob/master/r2/r2/controllers/api.py#L4295># 
 * <https://www.reddit.com/dev/api#POST_api_flairselector>POST [/r/subreddit
 * ]/api/flairselectorflair <https://github.com/reddit/reddit/wiki/OAuth2>
 *  - Return information about a users's flair options.
 *  - If link is given, return link flair options. Otherwise, return user flair 
 * options for this subreddit.
 *  - The logged in user's flair is also returned. Subreddit moderators may give a 
 * user byname to instead retrieve that user's flair.
 *  - linka fullname <https://www.reddit.com/dev/api#fullname> of a link
 *  - namea user by name
 **/
fun OAuthClient.postRSubredditFlairselector(subreddit: String, vararg urlParams: Pair<String, Any?>) =
        retry(3) { requestApi("/r/$subreddit/api/flairselector").post(urlParams.multipart()).readEntity(String::class.java) }

/*
 * view code 
 * <https://github.com/reddit/reddit/blob/master/r2/r2/controllers/api.py#L4187># 
 * <https://www.reddit.com/dev/api#POST_api_flairtemplate>POST [/r/subreddit
 * ]/api/flairtemplatemodflair <https://github.com/reddit/reddit/wiki/OAuth2>
 *  - api_typethe string json
 *  - css_classa valid subreddit image name
 *  - flair_template_idflair_typeone of (USER_FLAIR, LINK_FLAIR)
 *  - texta string no longer than 64 characters
 *  - text_editableboolean value
 *  - uh / X-Modhash headera modhash <https://www.reddit.com/dev/api#modhashes>
 **/
fun OAuthClient.postRSubredditFlairtemplate(subreddit: String, vararg urlParams: Pair<String, Any?>) =
        retry(3) { requestApi("/r/$subreddit/api/flairtemplate").post(urlParams.multipart()).readEntity(String::class.java) }

/*
 * view code 
 * <https://github.com/reddit/reddit/blob/master/r2/r2/controllers/api.py#L4341># 
 * <https://www.reddit.com/dev/api#POST_api_selectflair>POST [/r/subreddit
 * ]/api/selectflairflair <https://github.com/reddit/reddit/wiki/OAuth2>
 *  - api_typethe string json
 *  - flair_template_idlinka fullname <https://www.reddit.com/dev/api#fullname> of 
 * a link
 *  - namea user by name
 *  - texta string no longer than 64 characters
 *  - uh / X-Modhash headera modhash <https://www.reddit.com/dev/api#modhashes>
 **/
fun OAuthClient.postRSubredditSelectflair(subreddit: String, vararg urlParams: Pair<String, Any?>) =
        retry(3) { requestApi("/r/$subreddit/api/selectflair").post(urlParams.multipart()).readEntity(String::class.java) }

/*
 * view code 
 * <https://github.com/reddit/reddit/blob/master/r2/r2/controllers/api.py#L4117># 
 * <https://www.reddit.com/dev/api#POST_api_setflairenabled>POST [/r/subreddit
 * ]/api/setflairenabledflair <https://github.com/reddit/reddit/wiki/OAuth2>
 *  - api_typethe string json
 *  - flair_enabledboolean value
 *  - uh / X-Modhash headera modhash <https://www.reddit.com/dev/api#modhashes>
 **/
fun OAuthClient.postRSubredditSetflairenabled(subreddit: String, vararg urlParams: Pair<String, Any?>) =
        retry(3) { requestApi("/r/$subreddit/api/setflairenabled").post(urlParams.multipart()).readEntity(String::class.java) }

/*
 * view code 
 * <https://github.com/reddit/reddit/blob/master/r2/r2/controllers/apiv1/gold.py#L77>
 * # <https://www.reddit.com/dev/api#POST_api_v1_gold_gild_{fullname}>POST 
 * /api/v1/gold/gild/fullnamecreddits 
 * <https://github.com/reddit/reddit/wiki/OAuth2>
 *  - fullnamefullname <https://www.reddit.com/dev/api#fullnames> of a thing
 **/
fun OAuthClient.postV1GoldGildFullname(fullname: String, vararg urlParams: Pair<String, Any?>) =
        retry(3) { requestApi("/api/v1/gold/gild/$fullname").post(urlParams.multipart()).getResponse() }

/*
 * view code 
 * <https://github.com/reddit/reddit/blob/master/r2/r2/controllers/apiv1/gold.py#L102>
 * # <https://www.reddit.com/dev/api#POST_api_v1_gold_give_{username}>POST 
 * /api/v1/gold/give/usernamecreddits 
 * <https://github.com/reddit/reddit/wiki/OAuth2>
 *  - monthsan integer between 1 and 36
 *  - usernameA valid, existing reddit username
 **/
fun OAuthClient.postV1GoldGiveUsername(username: String, vararg urlParams: Pair<String, Any?>) =
        retry(3) { requestApi("/api/v1/gold/give/$username").post(urlParams.multipart()).getResponse() }

/*
 * view code 
 * <https://github.com/reddit/reddit/blob/master/r2/r2/controllers/api.py#L2060># 
 * <https://www.reddit.com/dev/api#POST_api_comment>POST /api/commentany 
 * <https://github.com/reddit/reddit/wiki/OAuth2>
 *  - Submit a new comment or reply to a message.
 *  - parent is the fullname of the thing being replied to. Its value changes the 
 * kind of object created by this request:
 *  * the fullname of a Link: a top-level comment in that Link's thread. (requires
 * submit scope) 
 *  * the fullname of a Comment: a comment reply to that comment. (requires submit
 *  scope) 
 *  * the fullname of a Message: a message reply to that message. (requires 
 * privatemessages scope) text should be the raw markdown body of the comment or 
 * message.
 *  - To start a new message thread, use /api/compose 
 * <https://www.reddit.com/dev/api#POST_api_compose>.
 *  - api_typethe string json
 *  - textraw markdown text
 *  - thing_idfullname <https://www.reddit.com/dev/api#fullnames> of parent thing
 *  - uh / X-Modhash headera modhash <https://www.reddit.com/dev/api#modhashes>
 **/
fun OAuthClient.postComment(vararg urlParams: Pair<String, Any?>) =
        retry(3) { requestApi("/api/comment").post(urlParams.multipart()).getResponse() }

/*
 * view code 
 * <https://github.com/reddit/reddit/blob/master/r2/r2/controllers/api.py#L1491># 
 * <https://www.reddit.com/dev/api#POST_api_del>POST /api/deledit 
 * <https://github.com/reddit/reddit/wiki/OAuth2>
 *  - Delete a Link or Comment.
 *  - idfullname <https://www.reddit.com/dev/api#fullnames> of a thing created by 
 * the user
 *  - uh / X-Modhash headera modhash <https://www.reddit.com/dev/api#modhashes>
 **/
fun OAuthClient.postDel(vararg urlParams: Pair<String, Any?>) =
        retry(3) { requestApi("/api/del").post(urlParams.multipart()).getResponse() }

/*
 * view code 
 * <https://github.com/reddit/reddit/blob/master/r2/r2/controllers/api.py#L1975># 
 * <https://www.reddit.com/dev/api#POST_api_editusertext>POST /api/editusertextedit
 *  <https://github.com/reddit/reddit/wiki/OAuth2>
 *  - Edit the body text of a comment or self-post.
 *  - api_typethe string json
 *  - textraw markdown text
 *  - thing_idfullname <https://www.reddit.com/dev/api#fullnames> of a thing 
 * created by the user
 *  - uh / X-Modhash headera modhash <https://www.reddit.com/dev/api#modhashes>
 **/
fun OAuthClient.postEditusertext(vararg urlParams: Pair<String, Any?>) =
        retry(3) { requestApi("/api/editusertext").post(urlParams.multipart()).getResponse() }

/*
 * view code 
 * <https://github.com/reddit/reddit/blob/master/r2/r2/controllers/api.py#L3472># 
 * <https://www.reddit.com/dev/api#POST_api_hide>POST /api/hidereport 
 * <https://github.com/reddit/reddit/wiki/OAuth2>
 *  - Hide a link.
 *  - This removes it from the user's default view of subreddit listings.
 *  - See also: /api/unhide <https://www.reddit.com/dev/api#POST_api_unhide>.
 *  - idA comma-separated list of link fullnames 
 * <https://www.reddit.com/dev/api#fullnames>
 *  - uh / X-Modhash headera modhash <https://www.reddit.com/dev/api#modhashes>
 **/
fun OAuthClient.postHide(vararg urlParams: Pair<String, Any?>) =
        retry(3) { requestApi("/api/hide").post(urlParams.multipart()).getResponse() }

/*
 * view code 
 * <https://github.com/reddit/reddit/blob/master/r2/r2/controllers/api.py#L173># 
 * <https://www.reddit.com/dev/api#GET_api_info>GET [/r/subreddit]/api/inforead 
 * <https://github.com/reddit/reddit/wiki/OAuth2>
 *  - Return a listing of things specified by their fullnames.
 *  - Only Links, Comments, and Subreddits are allowed.
 *  - idA comma-separated list of thing fullnames 
 * <https://www.reddit.com/dev/api#fullnames>
 *  - urla valid URL
 **/
fun OAuthClient.getRSubredditInfo(subreddit: String, id: List<String>?, url: String?) =
        retry(3) { requestApi("/r/$subreddit/api/info",
                "id" to id.csv(), 
                "url" to url).get().readEntity(String::class.java) }

/*
 * view code 
 * <https://github.com/reddit/reddit/blob/master/r2/r2/controllers/api.py#L1522># 
 * <https://www.reddit.com/dev/api#POST_api_lock>POST /api/lockmodposts 
 * <https://github.com/reddit/reddit/wiki/OAuth2>
 *  - Lock a link.
 *  - Prevents a post from receiving new comments.
 *  - See also: /api/unlock <https://www.reddit.com/dev/api#POST_api_unlock>.
 *  - idfullname <https://www.reddit.com/dev/api#fullnames> of a link
 *  - uh / X-Modhash headera modhash <https://www.reddit.com/dev/api#modhashes>
 **/
fun OAuthClient.postLock(vararg urlParams: Pair<String, Any?>) =
        retry(3) { requestApi("/api/lock").post(urlParams.multipart()).getResponse() }

/*
 * view code 
 * <https://github.com/reddit/reddit/blob/master/r2/r2/controllers/api.py#L1568># 
 * <https://www.reddit.com/dev/api#POST_api_marknsfw>POST /api/marknsfwmodposts 
 * <https://github.com/reddit/reddit/wiki/OAuth2>
 *  - Mark a link NSFW.
 *  - See also: /api/unmarknsfw <https://www.reddit.com/dev/api#POST_api_unmarknsfw>
 * .
 *  - idfullname <https://www.reddit.com/dev/api#fullnames> of a thing
 *  - uh / X-Modhash headera modhash <https://www.reddit.com/dev/api#modhashes>
 **/
fun OAuthClient.postMarknsfw(vararg urlParams: Pair<String, Any?>) =
        retry(3) { requestApi("/api/marknsfw").post(urlParams.multipart()).getResponse() }

/*
 * view code 
 * <https://github.com/reddit/reddit/blob/master/r2/r2/controllers/api.py#L3535># 
 * <https://www.reddit.com/dev/api#GET_api_morechildren>GET /api/morechildrenread 
 * <https://github.com/reddit/reddit/wiki/OAuth2>
 *  - Retrieve additional comments omitted from a base comment tree.
 *  - When a comment tree is rendered, the most relevant comments are selected for 
 * display first. Remaining comments are stubbed out with "MoreComments" links. 
 * This API call is used to retrieve the additional comments represented by those 
 * stubs, up to 20 at a time.
 *  - The two core parameters required are link and children. link is the fullname 
 * of the link whose comments are being fetched.children is a comma-delimited list 
 * of comment ID36s that need to be fetched.
 *  - If id is passed, it should be the ID of the MoreComments object this call is 
 * replacing. This is needed only for the HTML UI's purposes and is optional 
 * otherwise.
 *  - NOTE: you may only make one request at a time to this API endpoint. Higher 
 * concurrency will result in an error being returned.
 *  - api_typethe string json
 *  - childrena comma-delimited list of comment ID36s
 *  - id(optional) id of the associated MoreChildren object
 *  - link_idfullname <https://www.reddit.com/dev/api#fullnames> of a thing
 *  - sortone of (confidence, top, new, controversial, old, random, qa)
 **/
fun OAuthClient.getMorechildren(apiType: String?, children: List<String>?, id: String?, linkId: String?, sort: String?) =
        retry(3) { requestApi("/api/morechildren",
                "api_type" to apiType, 
                "children" to children.csv(), 
                "id" to id, 
                "link_id" to linkId, 
                "sort" to sort).get().getResponse() }

/*
 * view code 
 * <https://github.com/reddit/reddit/blob/master/r2/r2/controllers/api.py#L1749># 
 * <https://www.reddit.com/dev/api#POST_api_report>POST /api/reportreport 
 * <https://github.com/reddit/reddit/wiki/OAuth2>
 *  - Report a link, comment or message.
 *  - Reporting a thing brings it to the attention of the subreddit's moderators. 
 * Reporting a message sends it to a system for admin review.
 *  - For links and comments, the thing is implicitly hidden as well (see /api/hide 
 * <https://www.reddit.com/dev/api#POST_api_hide> for details).
 *  - api_typethe string json
 *  - other_reasona string no longer than 100 characters
 *  - reasona string no longer than 100 characters
 *  - site_reasona string no longer than 100 characters
 *  - thing_idfullname <https://www.reddit.com/dev/api#fullnames> of a thing
 *  - uh / X-Modhash headera modhash <https://www.reddit.com/dev/api#modhashes>
 **/
fun OAuthClient.postReport(vararg urlParams: Pair<String, Any?>) =
        retry(3) { requestApi("/api/report").post(urlParams.multipart()).getResponse() }

/*
 * view code 
 * <https://github.com/reddit/reddit/blob/master/r2/r2/controllers/api.py#L3340># 
 * <https://www.reddit.com/dev/api#POST_api_save>POST /api/savesave 
 * <https://github.com/reddit/reddit/wiki/OAuth2>
 *  - Save a link or comment.
 *  - Saved things are kept in the user's saved listing for later perusal.
 *  - See also: /api/unsave <https://www.reddit.com/dev/api#POST_api_unsave>.
 *  - categorya category name
 *  - idfullname <https://www.reddit.com/dev/api#fullnames> of a thing
 *  - uh / X-Modhash headera modhash <https://www.reddit.com/dev/api#modhashes>
 **/
fun OAuthClient.postSave(vararg urlParams: Pair<String, Any?>) =
        retry(3) { requestApi("/api/save").post(urlParams.multipart()).getResponse() }

/*
 * view code 
 * <https://github.com/reddit/reddit/blob/master/r2/r2/controllers/api.py#L3323># 
 * <https://www.reddit.com/dev/api#GET_api_saved_categories>GET 
 * /api/saved_categoriessave <https://github.com/reddit/reddit/wiki/OAuth2>
 *  - Get a list of categories in which things are currently saved.
 *  - See also: /api/save <https://www.reddit.com/dev/api#POST_api_save>.
 **/
fun OAuthClient.getSaved_categories() =
        retry(3) { requestApi("/api/saved_categories").get().getResponse() }

/*
 * view code 
 * <https://github.com/reddit/reddit/blob/master/r2/r2/controllers/api.py#L1619># 
 * <https://www.reddit.com/dev/api#POST_api_sendreplies>POST /api/sendrepliesedit 
 * <https://github.com/reddit/reddit/wiki/OAuth2>
 *  - Enable or disable inbox replies for a link or comment.
 *  - state is a boolean that indicates whether you are enabling or disabling inbox 
 * replies - true to enable, false to disable.
 *  - idfullname <https://www.reddit.com/dev/api#fullnames> of a thing created by 
 * the user
 *  - stateboolean value
 *  - uh / X-Modhash headera modhash <https://www.reddit.com/dev/api#modhashes>
 **/
fun OAuthClient.postSendreplies(vararg urlParams: Pair<String, Any?>) =
        retry(3) { requestApi("/api/sendreplies").post(urlParams.multipart()).getResponse() }

/*
 * view code 
 * <https://github.com/reddit/reddit/blob/master/r2/r2/controllers/api.py#L1680># 
 * <https://www.reddit.com/dev/api#POST_api_set_contest_mode>POST 
 * /api/set_contest_modemodposts <https://github.com/reddit/reddit/wiki/OAuth2>
 *  - Set or unset "contest mode" for a link's comments.
 *  - state is a boolean that indicates whether you are enabling or disabling 
 * contest mode - true to enable, false to disable.
 *  - api_typethe string json
 *  - idstateboolean value
 *  - uh / X-Modhash headera modhash <https://www.reddit.com/dev/api#modhashes>
 **/
fun OAuthClient.postSet_contest_mode(vararg urlParams: Pair<String, Any?>) =
        retry(3) { requestApi("/api/set_contest_mode").post(urlParams.multipart()).getResponse() }

/*
 * view code 
 * <https://github.com/reddit/reddit/blob/master/r2/r2/controllers/api.py#L1706># 
 * <https://www.reddit.com/dev/api#POST_api_set_subreddit_sticky>POST 
 * /api/set_subreddit_stickymodposts <https://github.com/reddit/reddit/wiki/OAuth2>
 *  - Set or unset a Link as the sticky in its subreddit.
 *  - state is a boolean that indicates whether to sticky or unsticky this post - 
 * true to sticky, false to unsticky.
 *  - The num argument is optional, and only used when stickying a post. It allows 
 * specifying a particular "slot" to sticky the post into, and if there is already 
 * a post stickied in that slot it will be replaced. If there is no post in the 
 * specified slot to replace, ornum is None, the bottom-most slot will be used.
 *  - api_typethe string json
 *  - idnuman integer between 1 and 2
 *  - stateboolean value
 *  - uh / X-Modhash headera modhash <https://www.reddit.com/dev/api#modhashes>
 **/
fun OAuthClient.postSet_subreddit_sticky(vararg urlParams: Pair<String, Any?>) =
        retry(3) { requestApi("/api/set_subreddit_sticky").post(urlParams.multipart()).getResponse() }

/*
 * view code 
 * <https://github.com/reddit/reddit/blob/master/r2/r2/controllers/api.py#L1654># 
 * <https://www.reddit.com/dev/api#POST_api_set_suggested_sort>POST 
 * /api/set_suggested_sortmodposts <https://github.com/reddit/reddit/wiki/OAuth2>
 *  - Set a suggested sort for a link.
 *  - Suggested sorts are useful to display comments in a certain preferred way for 
 * posts. For example, casual conversation may be better sorted by new by default, 
 * or AMAs may be sorted by Q&A. A sort of an empty string clears the default sort.
 *  - api_typethe string json
 *  - idsortone of (confidence, top, new, controversial, old, random, qa, blank)
 *  - uh / X-Modhash headera modhash <https://www.reddit.com/dev/api#modhashes>
 **/
fun OAuthClient.postSet_suggested_sort(vararg urlParams: Pair<String, Any?>) =
        retry(3) { requestApi("/api/set_suggested_sort").post(urlParams.multipart()).getResponse() }

/*
 * view code 
 * <https://github.com/reddit/reddit/blob/master/r2/r2/controllers/api.py#L5090># 
 * <https://www.reddit.com/dev/api#POST_api_store_visits>POST /api/store_visitssave
 *  <https://github.com/reddit/reddit/wiki/OAuth2>
 *  - Requires a subscription to reddit gold <https://www.reddit.com/gold/about>
 *  - linksA comma-separated list of link fullnames 
 * <https://www.reddit.com/dev/api#fullnames>
 *  - uh / X-Modhash headera modhash <https://www.reddit.com/dev/api#modhashes>
 **/
fun OAuthClient.postStore_visits(vararg urlParams: Pair<String, Any?>) =
        retry(3) { requestApi("/api/store_visits").post(urlParams.multipart()).getResponse() }

/*
 * view code 
 * <https://github.com/reddit/reddit/blob/master/r2/r2/controllers/api.py#L438># 
 * <https://www.reddit.com/dev/api#POST_api_submit>POST /api/submitsubmit 
 * <https://github.com/reddit/reddit/wiki/OAuth2>
 *  - Submit a link to a subreddit.
 *  - Submit will create a link or self-post in the subreddit sr with the title 
 * title. If kind is "link", then url is expected to be a valid URL to link to. 
 * Otherwise,text, if present, will be the body of the self-post.
 *  - If a link with the same URL has already been submitted to the specified 
 * subreddit an error will be returned unlessresubmit is true. extension is used 
 * for determining which view-type (e.g.json, compact etc.) to use for the 
 * redirect that is generated if theresubmit error occurs.
 *  - api_typethe string json
 *  - captchathe user's response to the CAPTCHA challenge
 *  - extensionextension used for redirects
 *  - identhe identifier of the CAPTCHA challenge
 *  - kindone of (link, self)
 *  - resubmitboolean value
 *  - sendrepliesboolean value
 *  - srname of a subreddit
 *  - textraw markdown text
 *  - titletitle of the submission. up to 300 characters long
 *  - uh / X-Modhash headera modhash <https://www.reddit.com/dev/api#modhashes>
 *  - urla valid URL
 **/
fun OAuthClient.postSubmit(vararg urlParams: Pair<String, Any?>) =
        retry(3) { requestApi("/api/submit").post(urlParams.multipart()).getResponse() }

/*
 * view code 
 * <https://github.com/reddit/reddit/blob/master/r2/r2/controllers/api.py#L3490># 
 * <https://www.reddit.com/dev/api#POST_api_unhide>POST /api/unhidereport 
 * <https://github.com/reddit/reddit/wiki/OAuth2>
 *  - Unhide a link.
 *  - See also: /api/hide <https://www.reddit.com/dev/api#POST_api_hide>.
 *  - idA comma-separated list of link fullnames 
 * <https://www.reddit.com/dev/api#fullnames>
 *  - uh / X-Modhash headera modhash <https://www.reddit.com/dev/api#modhashes>
 **/
fun OAuthClient.postUnhide(vararg urlParams: Pair<String, Any?>) =
        retry(3) { requestApi("/api/unhide").post(urlParams.multipart()).getResponse() }

/*
 * view code 
 * <https://github.com/reddit/reddit/blob/master/r2/r2/controllers/api.py#L1545># 
 * <https://www.reddit.com/dev/api#POST_api_unlock>POST /api/unlockmodposts 
 * <https://github.com/reddit/reddit/wiki/OAuth2>
 *  - Unlock a link.
 *  - Allow a post to receive new comments.
 *  - See also: /api/lock <https://www.reddit.com/dev/api#POST_api_lock>.
 *  - idfullname <https://www.reddit.com/dev/api#fullnames> of a link
 *  - uh / X-Modhash headera modhash <https://www.reddit.com/dev/api#modhashes>
 **/
fun OAuthClient.postUnlock(vararg urlParams: Pair<String, Any?>) =
        retry(3) { requestApi("/api/unlock").post(urlParams.multipart()).getResponse() }

/*
 * view code 
 * <https://github.com/reddit/reddit/blob/master/r2/r2/controllers/api.py#L1589># 
 * <https://www.reddit.com/dev/api#POST_api_unmarknsfw>POST /api/unmarknsfwmodposts
 *  <https://github.com/reddit/reddit/wiki/OAuth2>
 *  - Remove the NSFW marking from a link.
 *  - See also: /api/marknsfw <https://www.reddit.com/dev/api#POST_api_marknsfw>.
 *  - idfullname <https://www.reddit.com/dev/api#fullnames> of a thing
 *  - uh / X-Modhash headera modhash <https://www.reddit.com/dev/api#modhashes>
 **/
fun OAuthClient.postUnmarknsfw(vararg urlParams: Pair<String, Any?>) =
        retry(3) { requestApi("/api/unmarknsfw").post(urlParams.multipart()).getResponse() }

/*
 * view code 
 * <https://github.com/reddit/reddit/blob/master/r2/r2/controllers/api.py#L3367># 
 * <https://www.reddit.com/dev/api#POST_api_unsave>POST /api/unsavesave 
 * <https://github.com/reddit/reddit/wiki/OAuth2>
 *  - Unsave a link or comment.
 *  - This removes the thing from the user's saved listings as well.
 *  - See also: /api/save <https://www.reddit.com/dev/api#POST_api_save>.
 *  - idfullname <https://www.reddit.com/dev/api#fullnames> of a thing
 *  - uh / X-Modhash headera modhash <https://www.reddit.com/dev/api#modhashes>
 **/
fun OAuthClient.postUnsave(vararg urlParams: Pair<String, Any?>) =
        retry(3) { requestApi("/api/unsave").post(urlParams.multipart()).getResponse() }

/*
 * view code 
 * <https://github.com/reddit/reddit/blob/master/r2/r2/controllers/api.py#L2308># 
 * <https://www.reddit.com/dev/api#POST_api_vote>POST /api/votevote 
 * <https://github.com/reddit/reddit/wiki/OAuth2>
 *  - Cast a vote on a thing.
 *  - id should be the fullname of the Link or Comment to vote on.
 *  - dir indicates the direction of the vote. Voting 1 is an upvote, -1 is a 
 * downvote, and0 is equivalent to "un-voting" by clicking again on a highlighted 
 * arrow.
 *  - Note: votes must be cast by humans. That is, API clients proxying a human's 
 * action one-for-one are OK, but bots deciding how to vote on content or 
 * amplifying a human's vote are not. Seethe reddit rules 
 * <https://www.reddit.com/rules> for more details on what constitutes vote 
 * cheating.
 *  - dirvote direction. one of (1, 0, -1)
 *  - idfullname <https://www.reddit.com/dev/api#fullnames> of a thing
 *  - rankan integer greater than 1
 *  - uh / X-Modhash headera modhash <https://www.reddit.com/dev/api#modhashes>
 **/
fun OAuthClient.postVote(vararg urlParams: Pair<String, Any?>) =
        retry(3) { requestApi("/api/vote").post(urlParams.multipart()).getResponse() }

/*
 * view code 
 * <https://github.com/reddit/reddit/blob/master/r2/r2/controllers/listingcontroller.py#L715>
 * # <https://www.reddit.com/dev/api#GET_by_id_{names}>GET /by_id/namesread 
 * <https://github.com/reddit/reddit/wiki/OAuth2>
 *  - Get a listing of links by fullname.
 *  - names is a list of fullnames for links separated by commas or spaces.
 *  - namesA comma-separated list of link fullnames 
 * <https://www.reddit.com/dev/api#fullnames>
 **/
fun OAuthClient.getBy_idNames(names: List<String>) =
        retry(3) { requestApi("/by_id/$names",
                "names" to names.csv()).get().readEntity(String::class.java) }

/*
 * view code 
 * <https://github.com/reddit/reddit/blob/master/r2/r2/controllers/front.py#L214># 
 * <https://www.reddit.com/dev/api#GET_comments_{article}>GET [/r/subreddit
 * ]/comments/articleread <https://github.com/reddit/reddit/wiki/OAuth2>rss support
 *  <https://www.reddit.com/wiki/rss>
 *  - Get the comment tree for a given Link article.
 *  - If supplied, comment is the ID36 of a comment in the comment tree for article
 * . This comment will be the (highlighted) focal point of the returned view and
 * context will be the number of parents shown.
 *  - depth is the maximum depth of subtrees in the thread.
 *  - limit is the maximum number of comments to return.
 *  - See also: /api/morechildren 
 * <https://www.reddit.com/dev/api#GET_api_morechildren> and /api/comment 
 * <https://www.reddit.com/dev/api#POST_api_comment>.
 *  - articleID36 of a link
 *  - comment(optional) ID36 of a comment
 *  - contextan integer between 0 and 8
 *  - depth(optional) an integer
 *  - limit(optional) an integer
 *  - showeditsboolean value
 *  - showmoreboolean value
 *  - sortone of (confidence, top, new, controversial, old, random, qa)
 *  - sr_detail(optional) expand subreddits
 **/
fun OAuthClient.getRSubredditCommentsArticle(subreddit: String, article: String, comment: String?, context: String?, depth: String?, limit: String?, showedits: String?, showmore: String?, sort: String?, srDetail: Boolean?) =
        retry(3) { requestApi("/r/$subreddit/comments/$article",
                "article" to article, 
                "comment" to comment, 
                "context" to context, 
                "depth" to depth, 
                "limit" to limit, 
                "showedits" to showedits, 
                "showmore" to showmore, 
                "sort" to sort, 
                "sr_detail" to srDetail).get().readEntity(String::class.java) }

/*
 * view code 
 * <https://github.com/reddit/reddit/blob/master/r2/r2/controllers/front.py#L1004>#
 *  <https://www.reddit.com/dev/api#GET_duplicates_{article}>GET /duplicates/
 * articleread <https://github.com/reddit/reddit/wiki/OAuth2>rss support 
 * <https://www.reddit.com/wiki/rss>
 *  - Return a list of other submissions of the same URL
 *  - This endpoint is a listing <https://www.reddit.com/dev/api#listings>.
 *  - afterfullname <https://www.reddit.com/dev/api#fullnames> of a thing
 *  - articleThe base 36 ID of a Link
 *  - beforefullname <https://www.reddit.com/dev/api#fullnames> of a thing
 *  - counta positive integer (default: 0)
 *  - limitthe maximum number of items desired (default: 25, maximum: 100)
 *  - show(optional) the string all
 *  - sr_detail(optional) expand subreddits
 **/
fun OAuthClient.getDuplicatesArticle(article: String, after: String?, before: String?, count: Int?, limit: Int?, show: String?, srDetail: Boolean?) =
        retry(3) { requestApi("/duplicates/$article",
                "after" to after, 
                "article" to article, 
                "before" to before, 
                "count" to count, 
                "limit" to limit, 
                "show" to show, 
                "sr_detail" to srDetail).get().readEntity(String::class.java) }

/*
 * view code 
 * <https://github.com/reddit/reddit/blob/master/r2/r2/controllers/listingcontroller.py#L543>
 * # <https://www.reddit.com/dev/api#GET_hot>GET [/r/subreddit]/hotread 
 * <https://github.com/reddit/reddit/wiki/OAuth2>rss support 
 * <https://www.reddit.com/wiki/rss>
 *  - This endpoint is a listing <https://www.reddit.com/dev/api#listings>.
 *  - afterfullname <https://www.reddit.com/dev/api#fullnames> of a thing
 *  - beforefullname <https://www.reddit.com/dev/api#fullnames> of a thing
 *  - counta positive integer (default: 0)
 *  - limitthe maximum number of items desired (default: 25, maximum: 100)
 *  - show(optional) the string all
 *  - sr_detail(optional) expand subreddits
 **/
fun OAuthClient.getRSubredditHot(subreddit: String, after: String?, before: String?, count: Int?, limit: Int?, show: String?, srDetail: Boolean?) =
        retry(3) { requestApi("/r/$subreddit/hot",
                "after" to after, 
                "before" to before, 
                "count" to count, 
                "limit" to limit, 
                "show" to show, 
                "sr_detail" to srDetail).get().readEntity(String::class.java) }

/*
 * view code 
 * <https://github.com/reddit/reddit/blob/master/r2/r2/controllers/listingcontroller.py#L572>
 * # <https://www.reddit.com/dev/api#GET_new>GET [/r/subreddit]/newread 
 * <https://github.com/reddit/reddit/wiki/OAuth2>rss support 
 * <https://www.reddit.com/wiki/rss>
 *  - This endpoint is a listing <https://www.reddit.com/dev/api#listings>.
 *  - afterfullname <https://www.reddit.com/dev/api#fullnames> of a thing
 *  - beforefullname <https://www.reddit.com/dev/api#fullnames> of a thing
 *  - counta positive integer (default: 0)
 *  - limitthe maximum number of items desired (default: 25, maximum: 100)
 *  - show(optional) the string all
 *  - sr_detail(optional) expand subreddits
 **/
fun OAuthClient.getRSubredditNew(subreddit: String, after: String?, before: String?, count: Int?, limit: Int?, show: String?, srDetail: Boolean?) =
        retry(3) { requestApi("/r/$subreddit/new",
                "after" to after, 
                "before" to before, 
                "count" to count, 
                "limit" to limit, 
                "show" to show, 
                "sr_detail" to srDetail).get().readEntity(String::class.java) }

/*
 * view code 
 * <https://github.com/reddit/reddit/blob/master/r2/r2/controllers/front.py#L128># 
 * <https://www.reddit.com/dev/api#GET_random>GET [/r/subreddit]/randomread 
 * <https://github.com/reddit/reddit/wiki/OAuth2>
 *  - The Serendipity button
 **/
fun OAuthClient.getRSubredditRandom(subreddit: String) =
        retry(3) { requestApi("/r/$subreddit/random").get().readEntity(String::class.java) }

/*
 * view code 
 * <https://github.com/reddit/reddit/blob/master/r2/r2/controllers/listingcontroller.py#L625>
 * # <https://www.reddit.com/dev/api#GET_{sort}>GET [/r/subreddit]/sortread 
 * <https://github.com/reddit/reddit/wiki/OAuth2>rss support 
 * <https://www.reddit.com/wiki/rss>
 *  * → [/r/subreddit]/top
 *  * → [/r/subreddit]/controversialThis endpoint is a listing 
 * <https://www.reddit.com/dev/api#listings>.
 *  - tone of (hour, day, week, month, year, all)
 *  - afterfullname <https://www.reddit.com/dev/api#fullnames> of a thing
 *  - beforefullname <https://www.reddit.com/dev/api#fullnames> of a thing
 *  - counta positive integer (default: 0)
 *  - limitthe maximum number of items desired (default: 25, maximum: 100)
 *  - show(optional) the string all
 *  - sr_detail(optional) expand subreddits
 **/
fun OAuthClient.getRSubredditSort(subreddit: String, sort: String, t: String?, after: String?, before: String?, count: Int?, limit: Int?, show: String?, srDetail: Boolean?) =
        retry(3) { requestApi("/r/$subreddit/$sort",
                "t" to t, 
                "after" to after, 
                "before" to before, 
                "count" to count, 
                "limit" to limit, 
                "show" to show, 
                "sr_detail" to srDetail).get().readEntity(String::class.java) }

/*
 * view code 
 * <https://github.com/reddit/reddit/blob/master/r2/r2/controllers/listingcontroller.py#L625>
 * # <https://www.reddit.com/dev/api#GET_{sort}>GET [/r/subreddit]/sortread 
 * <https://github.com/reddit/reddit/wiki/OAuth2>rss support 
 * <https://www.reddit.com/wiki/rss>
 *  * → [/r/subreddit]/top
 *  * → [/r/subreddit]/controversialThis endpoint is a listing 
 * <https://www.reddit.com/dev/api#listings>.
 *  - tone of (hour, day, week, month, year, all)
 *  - afterfullname <https://www.reddit.com/dev/api#fullnames> of a thing
 *  - beforefullname <https://www.reddit.com/dev/api#fullnames> of a thing
 *  - counta positive integer (default: 0)
 *  - limitthe maximum number of items desired (default: 25, maximum: 100)
 *  - show(optional) the string all
 *  - sr_detail(optional) expand subreddits
 **/
fun OAuthClient.getRSubredditTop(subreddit: String, t: String?, after: String?, before: String?, count: Int?, limit: Int?, show: String?, srDetail: Boolean?) =
        retry(3) { requestApi("/r/$subreddit/top",
                "t" to t, 
                "after" to after, 
                "before" to before, 
                "count" to count, 
                "limit" to limit, 
                "show" to show, 
                "sr_detail" to srDetail).get().readEntity(String::class.java) }

/*
 * view code 
 * <https://github.com/reddit/reddit/blob/master/r2/r2/controllers/listingcontroller.py#L625>
 * # <https://www.reddit.com/dev/api#GET_{sort}>GET [/r/subreddit]/sortread 
 * <https://github.com/reddit/reddit/wiki/OAuth2>rss support 
 * <https://www.reddit.com/wiki/rss>
 *  * → [/r/subreddit]/top
 *  * → [/r/subreddit]/controversialThis endpoint is a listing 
 * <https://www.reddit.com/dev/api#listings>.
 *  - tone of (hour, day, week, month, year, all)
 *  - afterfullname <https://www.reddit.com/dev/api#fullnames> of a thing
 *  - beforefullname <https://www.reddit.com/dev/api#fullnames> of a thing
 *  - counta positive integer (default: 0)
 *  - limitthe maximum number of items desired (default: 25, maximum: 100)
 *  - show(optional) the string all
 *  - sr_detail(optional) expand subreddits
 **/
fun OAuthClient.getRSubredditControversial(subreddit: String, t: String?, after: String?, before: String?, count: Int?, limit: Int?, show: String?, srDetail: Boolean?) =
        retry(3) { requestApi("/r/$subreddit/controversial",
                "t" to t, 
                "after" to after, 
                "before" to before, 
                "count" to count, 
                "limit" to limit, 
                "show" to show, 
                "sr_detail" to srDetail).get().readEntity(String::class.java) }

/*
 * Real-time updates on reddit.
 *  - In addition to the standard reddit API, WebSockets play a huge role in reddit 
 * live. Receiving push notification of changes to the thread via websockets is 
 * much better than polling the thread repeatedly.
 *  - To connect to the websocket server, fetch /live/thread/about.json 
 * <https://www.reddit.com/dev/api#GET_live_%7Bthread%7D_about.json> and get the 
 * websocket_url field. The websocket URL expires after a period of time; if it 
 * does, fetch a new one from that endpoint.
 *  - Once connected to the socket, a variety of messages can come in. All messages 
 * will be in text frames containing a JSON object with two keys:type and payload. 
 * Live threads can send messages with manytypes:
 *  * update - a new update has been posted in the thread. the payload contains 
 * the JSON representation of the update. 
 *  * activity - periodic update of the viewer counts for the thread. 
 *  * settings - the thread's settings have changed. the payload is an object 
 * with each key being a property of the thread (as inabout.json) and its new 
 * value. 
 *  * delete - an update has been deleted (removed from the thread). the payload 
 * is the ID of the deleted update. 
 *  * strike - an update has been stricken (marked incorrect and crossed out). the
 * payload is the ID of the stricken update. 
 *  * embeds_ready - a previously posted update has been parsed and embedded 
 * media is available for it now. thepayload contains a liveupdate_id and list of 
 * embeds to add to it. 
 *  * complete - the thread has been marked complete. no further updates will be 
 * sent. See /r/live <https://www.reddit.com/r/live> for more information.
 **/
fun OAuthClient.get() =
        retry(3) { requestApi("").get().readEntity(String::class.java) }

/*
 * view code 
 * <https://github.com/reddit/reddit-plugin-liveupdate/blob/master/reddit_liveupdate/controllers.py#L1015>
 * # <https://www.reddit.com/dev/api#POST_api_live_create>POST /api/live/create
 * submit <https://github.com/reddit/reddit/wiki/OAuth2>
 *  - Create a new live thread.
 *  - Once created, the initial settings can be modified with /api/live/thread/edit 
 * <https://www.reddit.com/dev/api#POST_api_live_%7Bthread%7D_edit> and new 
 * updates can be posted with/api/live/thread/update 
 * <https://www.reddit.com/dev/api#POST_api_live_%7Bthread%7D_update>.
 *  - api_typethe string json
 *  - descriptionraw markdown text
 *  - nsfwboolean value
 *  - resourcesraw markdown text
 *  - titlea string no longer than 120 characters
 *  - uh / X-Modhash headera modhash <https://www.reddit.com/dev/api#modhashes>
 **/
fun OAuthClient.postLiveCreate(vararg urlParams: Pair<String, Any?>) =
        retry(3) { requestApi("/api/live/create").post(urlParams.multipart()).getResponse() }

/*
 * view code 
 * <https://github.com/reddit/reddit-plugin-liveupdate/blob/master/reddit_liveupdate/controllers.py#L620>
 * # 
 * <https://www.reddit.com/dev/api#POST_api_live_{thread}_accept_contributor_invite>
 * POST /api/live/thread/accept_contributor_invitelivemanage 
 * <https://github.com/reddit/reddit/wiki/OAuth2>
 *  - Accept a pending invitation to contribute to the thread.
 *  - See also: /api/live/thread/leave_contributor 
 * <https://www.reddit.com/dev/api#POST_api_live_%7Bthread%7D_leave_contributor>.
 *  - api_typethe string json
 *  - uh / X-Modhash headera modhash <https://www.reddit.com/dev/api#modhashes>
 **/
fun OAuthClient.postLiveThreadAccept_contributor_invite(thread: String, vararg urlParams: Pair<String, Any?>) =
        retry(3) { requestApi("/api/live/$thread/accept_contributor_invite").post(urlParams.multipart()).getResponse() }

/*
 * view code 
 * <https://github.com/reddit/reddit-plugin-liveupdate/blob/master/reddit_liveupdate/controllers.py#L828>
 * # <https://www.reddit.com/dev/api#POST_api_live_{thread}_close_thread>POST 
 * /api/live/thread/close_threadlivemanage 
 * <https://github.com/reddit/reddit/wiki/OAuth2>
 *  - Permanently close the thread, disallowing future updates.
 *  - Requires the close permission for this thread.
 *  - api_typethe string json
 *  - uh / X-Modhash headera modhash <https://www.reddit.com/dev/api#modhashes>
 **/
fun OAuthClient.postLiveThreadClose_thread(thread: String, vararg urlParams: Pair<String, Any?>) =
        retry(3) { requestApi("/api/live/$thread/close_thread").post(urlParams.multipart()).getResponse() }

/*
 * view code 
 * <https://github.com/reddit/reddit-plugin-liveupdate/blob/master/reddit_liveupdate/controllers.py#L770>
 * # <https://www.reddit.com/dev/api#POST_api_live_{thread}_delete_update>POST 
 * /api/live/thread/delete_updateedit 
 * <https://github.com/reddit/reddit/wiki/OAuth2>
 *  - Delete an update from the thread.
 *  - Requires that specified update must have been authored by the user or that 
 * you have theedit permission for this thread.
 *  - See also: /api/live/thread/update 
 * <https://www.reddit.com/dev/api#POST_api_live_%7Bthread%7D_update>.
 *  - api_typethe string json
 *  - idthe ID of a single update. e.g. 
 * LiveUpdate_ff87068e-a126-11e3-9f93-12313b0b3603
 *  - uh / X-Modhash headera modhash <https://www.reddit.com/dev/api#modhashes>
 **/
fun OAuthClient.postLiveThreadDelete_update(thread: String, vararg urlParams: Pair<String, Any?>) =
        retry(3) { requestApi("/api/live/$thread/delete_update").post(urlParams.multipart()).getResponse() }

/*
 * view code 
 * <https://github.com/reddit/reddit-plugin-liveupdate/blob/master/reddit_liveupdate/controllers.py#L427>
 * # <https://www.reddit.com/dev/api#POST_api_live_{thread}_edit>POST /api/live/
 * thread/editlivemanage <https://github.com/reddit/reddit/wiki/OAuth2>
 *  - Configure the thread.
 *  - Requires the settings permission for this thread.
 *  - See also: /live/thread/about.json 
 * <https://www.reddit.com/dev/api#GET_live_%7Bthread%7D_about.json>.
 *  - api_typethe string json
 *  - descriptionraw markdown text
 *  - nsfwboolean value
 *  - resourcesraw markdown text
 *  - titlea string no longer than 120 characters
 *  - uh / X-Modhash headera modhash <https://www.reddit.com/dev/api#modhashes>
 **/
fun OAuthClient.postLiveThreadEdit(thread: String, vararg urlParams: Pair<String, Any?>) =
        retry(3) { requestApi("/api/live/$thread/edit").post(urlParams.multipart()).getResponse() }

/*
 * view code 
 * <https://github.com/reddit/reddit-plugin-liveupdate/blob/master/reddit_liveupdate/controllers.py#L517>
 * # <https://www.reddit.com/dev/api#POST_api_live_{thread}_invite_contributor>
 * POST /api/live/thread/invite_contributorlivemanage 
 * <https://github.com/reddit/reddit/wiki/OAuth2>
 *  - Invite another user to contribute to the thread.
 *  - Requires the manage permission for this thread. If the recipient accepts the 
 * invite, they will be granted the permissions specified.
 *  - See also: /api/live/thread/accept_contributor_invite 
 * <https://www.reddit.com/dev/api#POST_api_live_%7Bthread%7D_accept_contributor_invite>
 * , and/api/live/thread/rm_contributor_invite 
 * <https://www.reddit.com/dev/api#POST_api_live_%7Bthread%7D_rm_contributor_invite>
 * .
 *  - api_typethe string json
 *  - namethe name of an existing user
 *  - permissionspermission description e.g. +update,+edit,-manage
 *  - typeone of (liveupdate_contributor_invite, liveupdate_contributor)
 *  - uh / X-Modhash headera modhash <https://www.reddit.com/dev/api#modhashes>
 **/
fun OAuthClient.postLiveThreadInvite_contributor(thread: String, vararg urlParams: Pair<String, Any?>) =
        retry(3) { requestApi("/api/live/$thread/invite_contributor").post(urlParams.multipart()).getResponse() }

/*
 * view code 
 * <https://github.com/reddit/reddit-plugin-liveupdate/blob/master/reddit_liveupdate/controllers.py#L580>
 * # <https://www.reddit.com/dev/api#POST_api_live_{thread}_leave_contributor>POST 
 * /api/live/thread/leave_contributorlivemanage 
 * <https://github.com/reddit/reddit/wiki/OAuth2>
 *  - Abdicate contributorship of the thread.
 *  - See also: /api/live/thread/accept_contributor_invite 
 * <https://www.reddit.com/dev/api#POST_api_live_%7Bthread%7D_accept_contributor_invite>
 * , and/api/live/thread/invite_contributor 
 * <https://www.reddit.com/dev/api#POST_api_live_%7Bthread%7D_invite_contributor>.
 *  - api_typethe string json
 *  - uh / X-Modhash headera modhash <https://www.reddit.com/dev/api#modhashes>
 **/
fun OAuthClient.postLiveThreadLeave_contributor(thread: String, vararg urlParams: Pair<String, Any?>) =
        retry(3) { requestApi("/api/live/$thread/leave_contributor").post(urlParams.multipart()).getResponse() }

/*
 * view code 
 * <https://github.com/reddit/reddit-plugin-liveupdate/blob/master/reddit_liveupdate/controllers.py#L851>
 * # <https://www.reddit.com/dev/api#POST_api_live_{thread}_report>POST /api/live/
 * thread/reportreport <https://github.com/reddit/reddit/wiki/OAuth2>
 *  - Report the thread for violating the rules of reddit.
 *  - api_typethe string json
 *  - typeone of (spam, vote-manipulation, personal-information, sexualizing-minors,
 * site-breaking)
 *  - uh / X-Modhash headera modhash <https://www.reddit.com/dev/api#modhashes>
 **/
fun OAuthClient.postLiveThreadReport(thread: String, vararg urlParams: Pair<String, Any?>) =
        retry(3) { requestApi("/api/live/$thread/report").post(urlParams.multipart()).getResponse() }

/*
 * view code 
 * <https://github.com/reddit/reddit-plugin-liveupdate/blob/master/reddit_liveupdate/controllers.py#L702>
 * # <https://www.reddit.com/dev/api#POST_api_live_{thread}_rm_contributor>POST 
 * /api/live/thread/rm_contributorlivemanage 
 * <https://github.com/reddit/reddit/wiki/OAuth2>
 *  - Revoke another user's contributorship.
 *  - Requires the manage permission for this thread.
 *  - See also: /api/live/thread/invite_contributor 
 * <https://www.reddit.com/dev/api#POST_api_live_%7Bthread%7D_invite_contributor>.
 *  - api_typethe string json
 *  - idfullname <https://www.reddit.com/dev/api#fullnames> of a account
 *  - uh / X-Modhash headera modhash <https://www.reddit.com/dev/api#modhashes>
 **/
fun OAuthClient.postLiveThreadRm_contributor(thread: String, vararg urlParams: Pair<String, Any?>) =
        retry(3) { requestApi("/api/live/$thread/rm_contributor").post(urlParams.multipart()).getResponse() }

/*
 * view code 
 * <https://github.com/reddit/reddit-plugin-liveupdate/blob/master/reddit_liveupdate/controllers.py#L599>
 * # <https://www.reddit.com/dev/api#POST_api_live_{thread}_rm_contributor_invite>
 * POST /api/live/thread/rm_contributor_invitelivemanage 
 * <https://github.com/reddit/reddit/wiki/OAuth2>
 *  - Revoke an outstanding contributor invite.
 *  - Requires the manage permission for this thread.
 *  - See also: /api/live/thread/invite_contributor 
 * <https://www.reddit.com/dev/api#POST_api_live_%7Bthread%7D_invite_contributor>.
 *  - api_typethe string json
 *  - idfullname <https://www.reddit.com/dev/api#fullnames> of a account
 *  - uh / X-Modhash headera modhash <https://www.reddit.com/dev/api#modhashes>
 **/
fun OAuthClient.postLiveThreadRm_contributor_invite(thread: String, vararg urlParams: Pair<String, Any?>) =
        retry(3) { requestApi("/api/live/$thread/rm_contributor_invite").post(urlParams.multipart()).getResponse() }

/*
 * view code 
 * <https://github.com/reddit/reddit-plugin-liveupdate/blob/master/reddit_liveupdate/controllers.py#L649>
 * # 
 * <https://www.reddit.com/dev/api#POST_api_live_{thread}_set_contributor_permissions>
 * POST /api/live/thread/set_contributor_permissionslivemanage 
 * <https://github.com/reddit/reddit/wiki/OAuth2>
 *  - Change a contributor or contributor invite's permissions.
 *  - Requires the manage permission for this thread.
 *  - See also: /api/live/thread/invite_contributor 
 * <https://www.reddit.com/dev/api#POST_api_live_%7Bthread%7D_invite_contributor> 
 * and/api/live/thread/rm_contributor 
 * <https://www.reddit.com/dev/api#POST_api_live_%7Bthread%7D_rm_contributor>.
 *  - api_typethe string json
 *  - namethe name of an existing user
 *  - permissionspermission description e.g. +update,+edit,-manage
 *  - typeone of (liveupdate_contributor_invite, liveupdate_contributor)
 *  - uh / X-Modhash headera modhash <https://www.reddit.com/dev/api#modhashes>
 **/
fun OAuthClient.postLiveThreadSet_contributor_permissions(thread: String, vararg urlParams: Pair<String, Any?>) =
        retry(3) { requestApi("/api/live/$thread/set_contributor_permissions").post(urlParams.multipart()).getResponse() }

/*
 * view code 
 * <https://github.com/reddit/reddit-plugin-liveupdate/blob/master/reddit_liveupdate/controllers.py#L799>
 * # <https://www.reddit.com/dev/api#POST_api_live_{thread}_strike_update>POST 
 * /api/live/thread/strike_updateedit 
 * <https://github.com/reddit/reddit/wiki/OAuth2>
 *  - Strike (mark incorrect and cross out) the content of an update.
 *  - Requires that specified update must have been authored by the user or that 
 * you have theedit permission for this thread.
 *  - See also: /api/live/thread/update 
 * <https://www.reddit.com/dev/api#POST_api_live_%7Bthread%7D_update>.
 *  - api_typethe string json
 *  - idthe ID of a single update. e.g. 
 * LiveUpdate_ff87068e-a126-11e3-9f93-12313b0b3603
 *  - uh / X-Modhash headera modhash <https://www.reddit.com/dev/api#modhashes>
 **/
fun OAuthClient.postLiveThreadStrike_update(thread: String, vararg urlParams: Pair<String, Any?>) =
        retry(3) { requestApi("/api/live/$thread/strike_update").post(urlParams.multipart()).getResponse() }

/*
 * view code 
 * <https://github.com/reddit/reddit-plugin-liveupdate/blob/master/reddit_liveupdate/controllers.py#L722>
 * # <https://www.reddit.com/dev/api#POST_api_live_{thread}_update>POST /api/live/
 * thread/updatesubmit <https://github.com/reddit/reddit/wiki/OAuth2>
 *  - Post an update to the thread.
 *  - Requires the update permission for this thread.
 *  - See also: /api/live/thread/strike_update 
 * <https://www.reddit.com/dev/api#POST_api_live_%7Bthread%7D_strike_update>, and 
 * /api/live/thread/delete_update 
 * <https://www.reddit.com/dev/api#POST_api_live_%7Bthread%7D_delete_update>.
 *  - api_typethe string json
 *  - bodyraw markdown text
 *  - uh / X-Modhash headera modhash <https://www.reddit.com/dev/api#modhashes>
 **/
fun OAuthClient.postLiveThreadUpdate(thread: String, vararg urlParams: Pair<String, Any?>) =
        retry(3) { requestApi("/api/live/$thread/update").post(urlParams.multipart()).getResponse() }

/*
 * view code 
 * <https://github.com/reddit/reddit-plugin-liveupdate/blob/master/reddit_liveupdate/controllers.py#L259>
 * # <https://www.reddit.com/dev/api#GET_live_{thread}>GET /live/threadread 
 * <https://github.com/reddit/reddit/wiki/OAuth2>rss support 
 * <https://www.reddit.com/wiki/rss>
 *  - Get a list of updates posted in this thread.
 *  - See also: /api/live/thread/update 
 * <https://www.reddit.com/dev/api#POST_api_live_%7Bthread%7D_update>.
 *  - This endpoint is a listing <https://www.reddit.com/dev/api#listings>.
 *  - afterthe ID of a single update. e.g. 
 * LiveUpdate_ff87068e-a126-11e3-9f93-12313b0b3603
 *  - beforethe ID of a single update. e.g. 
 * LiveUpdate_ff87068e-a126-11e3-9f93-12313b0b3603
 *  - counta positive integer (default: 0)
 *  - is_embed(internal use only)
 *  - limitthe maximum number of items desired (default: 25, maximum: 100)
 *  - stylesrsubreddit name
 **/
fun OAuthClient.getLiveThread(thread: String, after: String?, before: String?, count: Int?, isEmbed: String?, limit: Int?, stylesr: String?) =
        retry(3) { requestApi("/live/$thread",
                "after" to after, 
                "before" to before, 
                "count" to count, 
                "is_embed" to isEmbed, 
                "limit" to limit, 
                "stylesr" to stylesr).get().readEntity(String::class.java) }

/*
 * view code 
 * <https://github.com/reddit/reddit-plugin-liveupdate/blob/master/reddit_liveupdate/controllers.py#L382>
 * # <https://www.reddit.com/dev/api#GET_live_{thread}_about>GET /live/thread/about
 * read <https://github.com/reddit/reddit/wiki/OAuth2>
 *  - Get some basic information about the live thread.
 *  - See also: /api/live/thread/edit 
 * <https://www.reddit.com/dev/api#POST_api_live_%7Bthread%7D_edit>.
 **/
fun OAuthClient.getLiveThreadAbout(thread: String) =
        retry(3) { requestApi("/live/$thread/about").get().readEntity(String::class.java) }

/*
 * view code 
 * <https://github.com/reddit/reddit-plugin-liveupdate/blob/master/reddit_liveupdate/controllers.py#L472>
 * # <https://www.reddit.com/dev/api#GET_live_{thread}_contributors>GET /live/
 * thread/contributorsread <https://github.com/reddit/reddit/wiki/OAuth2>
 *  - Get a list of users that contribute to this thread.
 *  - See also: /api/live/thread/invite_contributor 
 * <https://www.reddit.com/dev/api#POST_api_live_%7Bthread%7D_invite_contributor>, 
 * and/api/live/thread/rm_contributor 
 * <https://www.reddit.com/dev/api#POST_api_live_%7Bthread%7D_rm_contributor>.
 **/
fun OAuthClient.getLiveThreadContributors(thread: String) =
        retry(3) { requestApi("/live/$thread/contributors").get().readEntity(String::class.java) }

/*
 * view code 
 * <https://github.com/reddit/reddit-plugin-liveupdate/blob/master/reddit_liveupdate/controllers.py#L398>
 * # <https://www.reddit.com/dev/api#GET_live_{thread}_discussions>GET /live/thread
 * /discussionsread <https://github.com/reddit/reddit/wiki/OAuth2>rss support 
 * <https://www.reddit.com/wiki/rss>
 *  - Get a list of reddit submissions linking to this thread.
 *  - This endpoint is a listing <https://www.reddit.com/dev/api#listings>.
 *  - afterfullname <https://www.reddit.com/dev/api#fullnames> of a thing
 *  - beforefullname <https://www.reddit.com/dev/api#fullnames> of a thing
 *  - counta positive integer (default: 0)
 *  - limitthe maximum number of items desired (default: 25, maximum: 100)
 *  - show(optional) the string all
 *  - sr_detail(optional) expand subreddits
 **/
fun OAuthClient.getLiveThreadDiscussions(thread: String, after: String?, before: String?, count: Int?, limit: Int?, show: String?, srDetail: Boolean?) =
        retry(3) { requestApi("/live/$thread/discussions",
                "after" to after, 
                "before" to before, 
                "count" to count, 
                "limit" to limit, 
                "show" to show, 
                "sr_detail" to srDetail).get().readEntity(String::class.java) }

/*
 * view code 
 * <https://github.com/reddit/reddit/blob/master/r2/r2/controllers/api.py#L1842># 
 * <https://www.reddit.com/dev/api#POST_api_block>POST /api/blockprivatemessages 
 * <https://github.com/reddit/reddit/wiki/OAuth2>
 *  - For blocking via inbox.
 *  - idfullname <https://www.reddit.com/dev/api#fullnames> of a thing
 *  - uh / X-Modhash headera modhash <https://www.reddit.com/dev/api#modhashes>
 **/
fun OAuthClient.postBlock(vararg urlParams: Pair<String, Any?>) =
        retry(3) { requestApi("/api/block").post(urlParams.multipart()).getResponse() }

/*
 * view code 
 * <https://github.com/reddit/reddit/blob/master/r2/r2/controllers/api.py#L3403># 
 * <https://www.reddit.com/dev/api#POST_api_collapse_message>POST 
 * /api/collapse_message
 *  - Collapse a message
 *  - See also: /api/uncollapse_message 
 * <https://www.reddit.com/dev/api#POST_uncollapse_message>
 *  - idA comma-separated list of thing fullnames 
 * <https://www.reddit.com/dev/api#fullnames>
 *  - uh / X-Modhash headera modhash <https://www.reddit.com/dev/api#modhashes>
 **/
fun OAuthClient.postCollapse_message(vararg urlParams: Pair<String, Any?>) =
        retry(3) { requestApi("/api/collapse_message").post(urlParams.multipart()).getResponse() }

/*
 * view code 
 * <https://github.com/reddit/reddit/blob/master/r2/r2/controllers/api.py#L345># 
 * <https://www.reddit.com/dev/api#POST_api_compose>POST /api/compose
 * privatemessages <https://github.com/reddit/reddit/wiki/OAuth2>
 *  - Handles message composition under /message/compose.
 *  - api_typethe string json
 *  - captchathe user's response to the CAPTCHA challenge
 *  - from_srsubreddit name
 *  - identhe identifier of the CAPTCHA challenge
 *  - subjecta string no longer than 100 characters
 *  - textraw markdown text
 *  - tothe name of an existing user
 *  - uh / X-Modhash headera modhash <https://www.reddit.com/dev/api#modhashes>
 **/
fun OAuthClient.postCompose(vararg urlParams: Pair<String, Any?>) =
        retry(3) { requestApi("/api/compose").post(urlParams.multipart()).getResponse() }

/*
 * view code 
 * <https://github.com/reddit/reddit/blob/master/r2/r2/controllers/api.py#L3455># 
 * <https://www.reddit.com/dev/api#POST_api_read_all_messages>POST 
 * /api/read_all_messagesprivatemessages 
 * <https://github.com/reddit/reddit/wiki/OAuth2>
 *  - Queue up marking all messages for a user as read.
 *  - This may take some time, and returns 202 to acknowledge acceptance of the 
 * request.
 *  - uh / X-Modhash headera modhash <https://www.reddit.com/dev/api#modhashes>
 **/
fun OAuthClient.postRead_all_messages(vararg urlParams: Pair<String, Any?>) =
        retry(3) { requestApi("/api/read_all_messages").post(urlParams.multipart()).getResponse() }

/*
 * view code 
 * <https://github.com/reddit/reddit/blob/master/r2/r2/controllers/api.py#L3441># 
 * <https://www.reddit.com/dev/api#POST_api_read_message>POST /api/read_message
 * privatemessages <https://github.com/reddit/reddit/wiki/OAuth2>
 *  - idA comma-separated list of thing fullnames 
 * <https://www.reddit.com/dev/api#fullnames>
 *  - uh / X-Modhash headera modhash <https://www.reddit.com/dev/api#modhashes>
 **/
fun OAuthClient.postRead_message(vararg urlParams: Pair<String, Any?>) =
        retry(3) { requestApi("/api/read_message").post(urlParams.multipart()).getResponse() }

/*
 * view code 
 * <https://github.com/reddit/reddit/blob/master/r2/r2/controllers/api.py#L1881># 
 * <https://www.reddit.com/dev/api#POST_api_unblock_subreddit>POST 
 * /api/unblock_subredditprivatemessages 
 * <https://github.com/reddit/reddit/wiki/OAuth2>
 *  - idfullname <https://www.reddit.com/dev/api#fullnames> of a thing
 *  - uh / X-Modhash headera modhash <https://www.reddit.com/dev/api#modhashes>
 **/
fun OAuthClient.postUnblock_subreddit(vararg urlParams: Pair<String, Any?>) =
        retry(3) { requestApi("/api/unblock_subreddit").post(urlParams.multipart()).getResponse() }

/*
 * view code 
 * <https://github.com/reddit/reddit/blob/master/r2/r2/controllers/api.py#L3415># 
 * <https://www.reddit.com/dev/api#POST_api_uncollapse_message>POST 
 * /api/uncollapse_message
 *  - Uncollapse a message
 *  - See also: /api/collapse_message 
 * <https://www.reddit.com/dev/api#POST_collapse_message>
 *  - idA comma-separated list of thing fullnames 
 * <https://www.reddit.com/dev/api#fullnames>
 *  - uh / X-Modhash headera modhash <https://www.reddit.com/dev/api#modhashes>
 **/
fun OAuthClient.postUncollapse_message(vararg urlParams: Pair<String, Any?>) =
        retry(3) { requestApi("/api/uncollapse_message").post(urlParams.multipart()).getResponse() }

/*
 * view code 
 * <https://github.com/reddit/reddit/blob/master/r2/r2/controllers/api.py#L3427># 
 * <https://www.reddit.com/dev/api#POST_api_unread_message>POST /api/unread_message
 * privatemessages <https://github.com/reddit/reddit/wiki/OAuth2>
 *  - idA comma-separated list of thing fullnames 
 * <https://www.reddit.com/dev/api#fullnames>
 *  - uh / X-Modhash headera modhash <https://www.reddit.com/dev/api#modhashes>
 **/
fun OAuthClient.postUnread_message(vararg urlParams: Pair<String, Any?>) =
        retry(3) { requestApi("/api/unread_message").post(urlParams.multipart()).getResponse() }

/*
 * view code 
 * <https://github.com/reddit/reddit/blob/master/r2/r2/controllers/listingcontroller.py#L1299>
 * # <https://www.reddit.com/dev/api#GET_message_{where}>GET /message/where
 * privatemessages <https://github.com/reddit/reddit/wiki/OAuth2>rss support 
 * <https://www.reddit.com/wiki/rss>
 *  * → /message/inbox
 *  * → /message/unread
 *  * → /message/sentThis endpoint is a listing 
 * <https://www.reddit.com/dev/api#listings>.
 *  - markone of (true, false)
 *  - midafterfullname <https://www.reddit.com/dev/api#fullnames> of a thing
 *  - beforefullname <https://www.reddit.com/dev/api#fullnames> of a thing
 *  - counta positive integer (default: 0)
 *  - limitthe maximum number of items desired (default: 25, maximum: 100)
 *  - show(optional) the string all
 *  - sr_detail(optional) expand subreddits
 **/
fun OAuthClient.getMessageWhere(where: String, mark: Boolean?, after: String?, before: String?, count: Int?, limit: Int?, show: String?, srDetail: Boolean?) =
        retry(3) { requestApi("/message/$where",
                "mark" to mark, 
                "after" to after, 
                "before" to before, 
                "count" to count, 
                "limit" to limit, 
                "show" to show, 
                "sr_detail" to srDetail).get().readEntity(String::class.java) }

/*
 * view code 
 * <https://github.com/reddit/reddit/blob/master/r2/r2/controllers/listingcontroller.py#L1299>
 * # <https://www.reddit.com/dev/api#GET_message_{where}>GET /message/where
 * privatemessages <https://github.com/reddit/reddit/wiki/OAuth2>rss support 
 * <https://www.reddit.com/wiki/rss>
 *  * → /message/inbox
 *  * → /message/unread
 *  * → /message/sentThis endpoint is a listing 
 * <https://www.reddit.com/dev/api#listings>.
 *  - markone of (true, false)
 *  - midafterfullname <https://www.reddit.com/dev/api#fullnames> of a thing
 *  - beforefullname <https://www.reddit.com/dev/api#fullnames> of a thing
 *  - counta positive integer (default: 0)
 *  - limitthe maximum number of items desired (default: 25, maximum: 100)
 *  - show(optional) the string all
 *  - sr_detail(optional) expand subreddits
 **/
fun OAuthClient.getMessageInbox(mark: Boolean?, after: String?, before: String?, count: Int?, limit: Int?, show: String?, srDetail: Boolean?) =
        retry(3) { requestApi("/message/inbox",
                "mark" to mark, 
                "after" to after, 
                "before" to before, 
                "count" to count, 
                "limit" to limit, 
                "show" to show, 
                "sr_detail" to srDetail).get().readEntity(String::class.java) }

/*
 * view code 
 * <https://github.com/reddit/reddit/blob/master/r2/r2/controllers/listingcontroller.py#L1299>
 * # <https://www.reddit.com/dev/api#GET_message_{where}>GET /message/where
 * privatemessages <https://github.com/reddit/reddit/wiki/OAuth2>rss support 
 * <https://www.reddit.com/wiki/rss>
 *  * → /message/inbox
 *  * → /message/unread
 *  * → /message/sentThis endpoint is a listing 
 * <https://www.reddit.com/dev/api#listings>.
 *  - markone of (true, false)
 *  - midafterfullname <https://www.reddit.com/dev/api#fullnames> of a thing
 *  - beforefullname <https://www.reddit.com/dev/api#fullnames> of a thing
 *  - counta positive integer (default: 0)
 *  - limitthe maximum number of items desired (default: 25, maximum: 100)
 *  - show(optional) the string all
 *  - sr_detail(optional) expand subreddits
 **/
fun OAuthClient.getMessageUnread(mark: Boolean?, after: String?, before: String?, count: Int?, limit: Int?, show: String?, srDetail: Boolean?) =
        retry(3) { requestApi("/message/unread",
                "mark" to mark, 
                "after" to after, 
                "before" to before, 
                "count" to count, 
                "limit" to limit, 
                "show" to show, 
                "sr_detail" to srDetail).get().readEntity(String::class.java) }

/*
 * view code 
 * <https://github.com/reddit/reddit/blob/master/r2/r2/controllers/listingcontroller.py#L1299>
 * # <https://www.reddit.com/dev/api#GET_message_{where}>GET /message/where
 * privatemessages <https://github.com/reddit/reddit/wiki/OAuth2>rss support 
 * <https://www.reddit.com/wiki/rss>
 *  * → /message/inbox
 *  * → /message/unread
 *  * → /message/sentThis endpoint is a listing 
 * <https://www.reddit.com/dev/api#listings>.
 *  - markone of (true, false)
 *  - midafterfullname <https://www.reddit.com/dev/api#fullnames> of a thing
 *  - beforefullname <https://www.reddit.com/dev/api#fullnames> of a thing
 *  - counta positive integer (default: 0)
 *  - limitthe maximum number of items desired (default: 25, maximum: 100)
 *  - show(optional) the string all
 *  - sr_detail(optional) expand subreddits
 **/
fun OAuthClient.getMessageSent(mark: Boolean?, after: String?, before: String?, count: Int?, limit: Int?, show: String?, srDetail: Boolean?) =
        retry(3) { requestApi("/message/sent",
                "mark" to mark, 
                "after" to after, 
                "before" to before, 
                "count" to count, 
                "limit" to limit, 
                "show" to show, 
                "sr_detail" to srDetail).get().readEntity(String::class.java) }

/*
 * view code 
 * <https://github.com/reddit/reddit/blob/master/r2/r2/controllers/apiv1/scopes.py#L34>
 * # <https://www.reddit.com/dev/api#GET_api_v1_scopes>GET /api/v1/scopesany 
 * <https://github.com/reddit/reddit/wiki/OAuth2>
 *  - Retrieve descriptions of reddit's OAuth2 scopes.
 *  - If no scopes are given, information on all scopes are returned.
 *  - Invalid scope(s) will result in a 400 error with body that indicates the 
 * invalid scope(s).
 *  - scopes(optional) An OAuth2 scope string
 **/
fun OAuthClient.getV1Scopes(scopes: String?) =
        retry(3) { requestApi("/api/v1/scopes",
                "scopes" to scopes).get().getResponse() }

/*
 * view code 
 * <https://github.com/reddit/reddit/blob/master/r2/r2/controllers/front.py#L593># 
 * <https://www.reddit.com/dev/api#GET_about_log>GET [/r/subreddit]/about/logmodlog
 *  <https://github.com/reddit/reddit/wiki/OAuth2>rss support 
 * <https://www.reddit.com/wiki/rss>
 *  - Get a list of recent moderation actions.
 *  - Moderator actions taken within a subreddit are logged. This listing is a view 
 * of that log with various filters to aid in analyzing the information.
 *  - The optional mod parameter can be a comma-delimited list of moderator names 
 * to restrict the results to, or the stringa to restrict the results to admin 
 * actions taken within the subreddit.
 *  - The type parameter is optional and if sent limits the log entries returned to 
 * only those of the type specified.
 *  - This endpoint is a listing <https://www.reddit.com/dev/api#listings>.
 *  - afterfullname <https://www.reddit.com/dev/api#fullnames> of a thing
 *  - beforefullname <https://www.reddit.com/dev/api#fullnames> of a thing
 *  - counta positive integer (default: 0)
 *  - limitthe maximum number of items desired (default: 25, maximum: 500)
 *  - mod(optional) a moderator filter
 *  - show(optional) the string all
 *  - sr_detail(optional) expand subreddits
 *  - typeone of (banuser, unbanuser, removelink, approvelink, removecomment, 
 * approvecomment, addmoderator, invitemoderator, uninvitemoderator, 
 * acceptmoderatorinvite, removemoderator, addcontributor, removecontributor, 
 * editsettings, editflair, distinguish, marknsfw, wikibanned, wikicontributor, 
 * wikiunbanned, wikipagelisted, removewikicontributor, wikirevise, wikipermlevel, 
 * ignorereports, unignorereports, setpermissions, setsuggestedsort, sticky, 
 * unsticky, setcontestmode, unsetcontestmode, lock, unlock, muteuser, unmuteuser, 
 * createrule, editrule, deleterule)
 **/
fun OAuthClient.getRSubredditAboutLog(subreddit: String, after: String?, before: String?, count: Int?, limit: Int?, mod: String?, show: String?, srDetail: Boolean?, type: String?) =
        retry(3) { requestApi("/r/$subreddit/about/log",
                "after" to after, 
                "before" to before, 
                "count" to count, 
                "limit" to limit, 
                "mod" to mod, 
                "show" to show, 
                "sr_detail" to srDetail, 
                "type" to type).get().readEntity(String::class.java) }

/*
 * view code 
 * <https://github.com/reddit/reddit/blob/master/r2/r2/controllers/front.py#L818># 
 * <https://www.reddit.com/dev/api#GET_about_{location}>GET [/r/subreddit]/about/
 * locationread <https://github.com/reddit/reddit/wiki/OAuth2>
 *  * → [/r/subreddit]/about/reports
 *  * → [/r/subreddit]/about/spam
 *  * → [/r/subreddit]/about/modqueue
 *  * → [/r/subreddit]/about/unmoderated
 *  * → [/r/subreddit]/about/editedReturn a listing of posts relevant to 
 * moderators.
 *  * reports: Things that have been reported. 
 *  * spam: Things that have been marked as spam or otherwise removed. 
 *  * modqueue: Things requiring moderator review, such as reported things and 
 * items caught by the spam filter. 
 *  * unmoderated: Things that have yet to be approved/removed by a mod. 
 *  * edited: Things that have been edited recently. Requires the "posts" 
 * moderator permission for the subreddit.
 *  - This endpoint is a listing <https://www.reddit.com/dev/api#listings>.
 *  - afterfullname <https://www.reddit.com/dev/api#fullnames> of a thing
 *  - beforefullname <https://www.reddit.com/dev/api#fullnames> of a thing
 *  - counta positive integer (default: 0)
 *  - limitthe maximum number of items desired (default: 25, maximum: 100)
 *  - locationonlyone of (links, comments)
 *  - show(optional) the string all
 *  - sr_detail(optional) expand subreddits
 **/
fun OAuthClient.getRSubredditAboutLocation(subreddit: String, location: String, after: String?, before: String?, count: Int?, limit: Int?, only: String?, show: String?, srDetail: Boolean?) =
        retry(3) { requestApi("/r/$subreddit/about/$location",
                "after" to after, 
                "before" to before, 
                "count" to count, 
                "limit" to limit, 
                "only" to only, 
                "show" to show, 
                "sr_detail" to srDetail).get().readEntity(String::class.java) }

/*
 * view code 
 * <https://github.com/reddit/reddit/blob/master/r2/r2/controllers/front.py#L818># 
 * <https://www.reddit.com/dev/api#GET_about_{location}>GET [/r/subreddit]/about/
 * locationread <https://github.com/reddit/reddit/wiki/OAuth2>
 *  * → [/r/subreddit]/about/reports
 *  * → [/r/subreddit]/about/spam
 *  * → [/r/subreddit]/about/modqueue
 *  * → [/r/subreddit]/about/unmoderated
 *  * → [/r/subreddit]/about/editedReturn a listing of posts relevant to 
 * moderators.
 *  * reports: Things that have been reported. 
 *  * spam: Things that have been marked as spam or otherwise removed. 
 *  * modqueue: Things requiring moderator review, such as reported things and 
 * items caught by the spam filter. 
 *  * unmoderated: Things that have yet to be approved/removed by a mod. 
 *  * edited: Things that have been edited recently. Requires the "posts" 
 * moderator permission for the subreddit.
 *  - This endpoint is a listing <https://www.reddit.com/dev/api#listings>.
 *  - afterfullname <https://www.reddit.com/dev/api#fullnames> of a thing
 *  - beforefullname <https://www.reddit.com/dev/api#fullnames> of a thing
 *  - counta positive integer (default: 0)
 *  - limitthe maximum number of items desired (default: 25, maximum: 100)
 *  - locationonlyone of (links, comments)
 *  - show(optional) the string all
 *  - sr_detail(optional) expand subreddits
 **/
fun OAuthClient.getRSubredditAboutReports(subreddit: String, after: String?, before: String?, count: Int?, limit: Int?, only: String?, show: String?, srDetail: Boolean?) =
        retry(3) { requestApi("/r/$subreddit/about/reports",
                "after" to after, 
                "before" to before, 
                "count" to count, 
                "limit" to limit, 
                "only" to only, 
                "show" to show, 
                "sr_detail" to srDetail).get().readEntity(String::class.java) }

/*
 * view code 
 * <https://github.com/reddit/reddit/blob/master/r2/r2/controllers/front.py#L818># 
 * <https://www.reddit.com/dev/api#GET_about_{location}>GET [/r/subreddit]/about/
 * locationread <https://github.com/reddit/reddit/wiki/OAuth2>
 *  * → [/r/subreddit]/about/reports
 *  * → [/r/subreddit]/about/spam
 *  * → [/r/subreddit]/about/modqueue
 *  * → [/r/subreddit]/about/unmoderated
 *  * → [/r/subreddit]/about/editedReturn a listing of posts relevant to 
 * moderators.
 *  * reports: Things that have been reported. 
 *  * spam: Things that have been marked as spam or otherwise removed. 
 *  * modqueue: Things requiring moderator review, such as reported things and 
 * items caught by the spam filter. 
 *  * unmoderated: Things that have yet to be approved/removed by a mod. 
 *  * edited: Things that have been edited recently. Requires the "posts" 
 * moderator permission for the subreddit.
 *  - This endpoint is a listing <https://www.reddit.com/dev/api#listings>.
 *  - afterfullname <https://www.reddit.com/dev/api#fullnames> of a thing
 *  - beforefullname <https://www.reddit.com/dev/api#fullnames> of a thing
 *  - counta positive integer (default: 0)
 *  - limitthe maximum number of items desired (default: 25, maximum: 100)
 *  - locationonlyone of (links, comments)
 *  - show(optional) the string all
 *  - sr_detail(optional) expand subreddits
 **/
fun OAuthClient.getRSubredditAboutSpam(subreddit: String, after: String?, before: String?, count: Int?, limit: Int?, only: String?, show: String?, srDetail: Boolean?) =
        retry(3) { requestApi("/r/$subreddit/about/spam",
                "after" to after, 
                "before" to before, 
                "count" to count, 
                "limit" to limit, 
                "only" to only, 
                "show" to show, 
                "sr_detail" to srDetail).get().readEntity(String::class.java) }

/*
 * view code 
 * <https://github.com/reddit/reddit/blob/master/r2/r2/controllers/front.py#L818># 
 * <https://www.reddit.com/dev/api#GET_about_{location}>GET [/r/subreddit]/about/
 * locationread <https://github.com/reddit/reddit/wiki/OAuth2>
 *  * → [/r/subreddit]/about/reports
 *  * → [/r/subreddit]/about/spam
 *  * → [/r/subreddit]/about/modqueue
 *  * → [/r/subreddit]/about/unmoderated
 *  * → [/r/subreddit]/about/editedReturn a listing of posts relevant to 
 * moderators.
 *  * reports: Things that have been reported. 
 *  * spam: Things that have been marked as spam or otherwise removed. 
 *  * modqueue: Things requiring moderator review, such as reported things and 
 * items caught by the spam filter. 
 *  * unmoderated: Things that have yet to be approved/removed by a mod. 
 *  * edited: Things that have been edited recently. Requires the "posts" 
 * moderator permission for the subreddit.
 *  - This endpoint is a listing <https://www.reddit.com/dev/api#listings>.
 *  - afterfullname <https://www.reddit.com/dev/api#fullnames> of a thing
 *  - beforefullname <https://www.reddit.com/dev/api#fullnames> of a thing
 *  - counta positive integer (default: 0)
 *  - limitthe maximum number of items desired (default: 25, maximum: 100)
 *  - locationonlyone of (links, comments)
 *  - show(optional) the string all
 *  - sr_detail(optional) expand subreddits
 **/
fun OAuthClient.getRSubredditAboutModqueue(subreddit: String, after: String?, before: String?, count: Int?, limit: Int?, only: String?, show: String?, srDetail: Boolean?) =
        retry(3) { requestApi("/r/$subreddit/about/modqueue",
                "after" to after, 
                "before" to before, 
                "count" to count, 
                "limit" to limit, 
                "only" to only, 
                "show" to show, 
                "sr_detail" to srDetail).get().readEntity(String::class.java) }

/*
 * view code 
 * <https://github.com/reddit/reddit/blob/master/r2/r2/controllers/front.py#L818># 
 * <https://www.reddit.com/dev/api#GET_about_{location}>GET [/r/subreddit]/about/
 * locationread <https://github.com/reddit/reddit/wiki/OAuth2>
 *  * → [/r/subreddit]/about/reports
 *  * → [/r/subreddit]/about/spam
 *  * → [/r/subreddit]/about/modqueue
 *  * → [/r/subreddit]/about/unmoderated
 *  * → [/r/subreddit]/about/editedReturn a listing of posts relevant to 
 * moderators.
 *  * reports: Things that have been reported. 
 *  * spam: Things that have been marked as spam or otherwise removed. 
 *  * modqueue: Things requiring moderator review, such as reported things and 
 * items caught by the spam filter. 
 *  * unmoderated: Things that have yet to be approved/removed by a mod. 
 *  * edited: Things that have been edited recently. Requires the "posts" 
 * moderator permission for the subreddit.
 *  - This endpoint is a listing <https://www.reddit.com/dev/api#listings>.
 *  - afterfullname <https://www.reddit.com/dev/api#fullnames> of a thing
 *  - beforefullname <https://www.reddit.com/dev/api#fullnames> of a thing
 *  - counta positive integer (default: 0)
 *  - limitthe maximum number of items desired (default: 25, maximum: 100)
 *  - locationonlyone of (links, comments)
 *  - show(optional) the string all
 *  - sr_detail(optional) expand subreddits
 **/
fun OAuthClient.getRSubredditAboutUnmoderated(subreddit: String, after: String?, before: String?, count: Int?, limit: Int?, only: String?, show: String?, srDetail: Boolean?) =
        retry(3) { requestApi("/r/$subreddit/about/unmoderated",
                "after" to after, 
                "before" to before, 
                "count" to count, 
                "limit" to limit, 
                "only" to only, 
                "show" to show, 
                "sr_detail" to srDetail).get().readEntity(String::class.java) }

/*
 * view code 
 * <https://github.com/reddit/reddit/blob/master/r2/r2/controllers/front.py#L818># 
 * <https://www.reddit.com/dev/api#GET_about_{location}>GET [/r/subreddit]/about/
 * locationread <https://github.com/reddit/reddit/wiki/OAuth2>
 *  * → [/r/subreddit]/about/reports
 *  * → [/r/subreddit]/about/spam
 *  * → [/r/subreddit]/about/modqueue
 *  * → [/r/subreddit]/about/unmoderated
 *  * → [/r/subreddit]/about/editedReturn a listing of posts relevant to 
 * moderators.
 *  * reports: Things that have been reported. 
 *  * spam: Things that have been marked as spam or otherwise removed. 
 *  * modqueue: Things requiring moderator review, such as reported things and 
 * items caught by the spam filter. 
 *  * unmoderated: Things that have yet to be approved/removed by a mod. 
 *  * edited: Things that have been edited recently. Requires the "posts" 
 * moderator permission for the subreddit.
 *  - This endpoint is a listing <https://www.reddit.com/dev/api#listings>.
 *  - afterfullname <https://www.reddit.com/dev/api#fullnames> of a thing
 *  - beforefullname <https://www.reddit.com/dev/api#fullnames> of a thing
 *  - counta positive integer (default: 0)
 *  - limitthe maximum number of items desired (default: 25, maximum: 100)
 *  - locationonlyone of (links, comments)
 *  - show(optional) the string all
 *  - sr_detail(optional) expand subreddits
 **/
fun OAuthClient.getRSubredditAboutEdited(subreddit: String, after: String?, before: String?, count: Int?, limit: Int?, only: String?, show: String?, srDetail: Boolean?) =
        retry(3) { requestApi("/r/$subreddit/about/edited",
                "after" to after, 
                "before" to before, 
                "count" to count, 
                "limit" to limit, 
                "only" to only, 
                "show" to show, 
                "sr_detail" to srDetail).get().readEntity(String::class.java) }

/*
 * view code 
 * <https://github.com/reddit/reddit/blob/master/r2/r2/controllers/api.py#L1317># 
 * <https://www.reddit.com/dev/api#POST_api_accept_moderator_invite>POST [/r/
 * subreddit]/api/accept_moderator_invitemodself 
 * <https://github.com/reddit/reddit/wiki/OAuth2>
 *  - Accept an invite to moderate the specified subreddit.
 *  - The authenticated user must have been invited to moderate the subreddit by 
 * one of its current moderators.
 *  - See also: /api/friend <https://www.reddit.com/dev/api#POST_api_friend> and 
 * /subreddits/mine 
 * <https://www.reddit.com/dev/api#GET_subreddits_mine_%7Bwhere%7D>.
 *  - api_typethe string json
 *  - uh / X-Modhash headera modhash <https://www.reddit.com/dev/api#modhashes>
 **/
fun OAuthClient.postRSubredditAccept_moderator_invite(subreddit: String, vararg urlParams: Pair<String, Any?>) =
        retry(3) { requestApi("/r/$subreddit/api/accept_moderator_invite").post(urlParams.multipart()).readEntity(String::class.java) }

/*
 * view code 
 * <https://github.com/reddit/reddit/blob/master/r2/r2/controllers/api.py#L3096># 
 * <https://www.reddit.com/dev/api#POST_api_approve>POST /api/approvemodposts 
 * <https://github.com/reddit/reddit/wiki/OAuth2>
 *  - Approve a link or comment.
 *  - If the thing was removed, it will be re-inserted into appropriate listings. 
 * Any reports on the approved thing will be discarded.
 *  - See also: /api/remove <https://www.reddit.com/dev/api#POST_api_remove>.
 *  - idfullname <https://www.reddit.com/dev/api#fullnames> of a thing
 *  - uh / X-Modhash headera modhash <https://www.reddit.com/dev/api#modhashes>
 **/
fun OAuthClient.postApprove(vararg urlParams: Pair<String, Any?>) =
        retry(3) { requestApi("/api/approve").post(urlParams.multipart()).getResponse() }

/*
 * view code 
 * <https://github.com/reddit/reddit/blob/master/r2/r2/controllers/api.py#L3192># 
 * <https://www.reddit.com/dev/api#POST_api_distinguish>POST /api/distinguish
 * modposts <https://github.com/reddit/reddit/wiki/OAuth2>
 *  - Distinguish a thing's author with a sigil.
 *  - This can be useful to draw attention to and confirm the identity of the user 
 * in the context of a link or comment of theirs. The options for distinguish are 
 * as follows:
 *  * yes - add a moderator distinguish ([M]). only if the user is a moderator of 
 * the subreddit the thing is in. 
 *  * no - remove any distinguishes. 
 *  * admin - add an admin distinguish ([A]). admin accounts only. 
 *  * special - add a user-specific distinguish. depends on user. The first time 
 * a top-level comment is moderator distinguished, the author of the link the 
 * comment is in reply to will get a notification in their inbox.
 *  - api_typethe string json
 *  - howone of (yes, no, admin, special)
 *  - idfullname <https://www.reddit.com/dev/api#fullnames> of a thing
 *  - uh / X-Modhash headera modhash <https://www.reddit.com/dev/api#modhashes>
 **/
fun OAuthClient.postDistinguish(vararg urlParams: Pair<String, Any?>) =
        retry(3) { requestApi("/api/distinguish").post(urlParams.multipart()).getResponse() }

/*
 * view code 
 * <https://github.com/reddit/reddit/blob/master/r2/r2/controllers/api.py#L3140># 
 * <https://www.reddit.com/dev/api#POST_api_ignore_reports>POST /api/ignore_reports
 * modposts <https://github.com/reddit/reddit/wiki/OAuth2>
 *  - Prevent future reports on a thing from causing notifications.
 *  - Any reports made about a thing after this flag is set on it will not cause 
 * notifications or make the thing show up in the various moderation listings.
 *  - See also: /api/unignore_reports 
 * <https://www.reddit.com/dev/api#POST_api_unignore_reports>.
 *  - idfullname <https://www.reddit.com/dev/api#fullnames> of a thing
 *  - uh / X-Modhash headera modhash <https://www.reddit.com/dev/api#modhashes>
 **/
fun OAuthClient.postIgnore_reports(vararg urlParams: Pair<String, Any?>) =
        retry(3) { requestApi("/api/ignore_reports").post(urlParams.multipart()).getResponse() }

/*
 * view code 
 * <https://github.com/reddit/reddit/blob/master/r2/r2/controllers/api.py#L812># 
 * <https://www.reddit.com/dev/api#POST_api_leavecontributor>POST 
 * /api/leavecontributormodself <https://github.com/reddit/reddit/wiki/OAuth2>
 *  - Abdicate approved submitter status in a subreddit.
 *  - See also: /api/friend <https://www.reddit.com/dev/api#POST_api_friend>.
 *  - idfullname <https://www.reddit.com/dev/api#fullnames> of a thing
 *  - uh / X-Modhash headera modhash <https://www.reddit.com/dev/api#modhashes>
 **/
fun OAuthClient.postLeavecontributor(vararg urlParams: Pair<String, Any?>) =
        retry(3) { requestApi("/api/leavecontributor").post(urlParams.multipart()).getResponse() }

/*
 * view code 
 * <https://github.com/reddit/reddit/blob/master/r2/r2/controllers/api.py#L796># 
 * <https://www.reddit.com/dev/api#POST_api_leavemoderator>POST /api/leavemoderator
 * modself <https://github.com/reddit/reddit/wiki/OAuth2>
 *  - Abdicate moderator status in a subreddit.
 *  - See also: /api/friend <https://www.reddit.com/dev/api#POST_api_friend>.
 *  - idfullname <https://www.reddit.com/dev/api#fullnames> of a thing
 *  - uh / X-Modhash headera modhash <https://www.reddit.com/dev/api#modhashes>
 **/
fun OAuthClient.postLeavemoderator(vararg urlParams: Pair<String, Any?>) =
        retry(3) { requestApi("/api/leavemoderator").post(urlParams.multipart()).getResponse() }

/*
 * view code 
 * <https://github.com/reddit/reddit/blob/master/r2/r2/controllers/api.py#L1901># 
 * <https://www.reddit.com/dev/api#POST_api_mute_message_author>POST 
 * /api/mute_message_authormodcontributors 
 * <https://github.com/reddit/reddit/wiki/OAuth2>
 *  - For muting user via modmail.
 *  - idfullname <https://www.reddit.com/dev/api#fullnames> of a thing
 *  - uh / X-Modhash headera modhash <https://www.reddit.com/dev/api#modhashes>
 **/
fun OAuthClient.postMute_message_author(vararg urlParams: Pair<String, Any?>) =
        retry(3) { requestApi("/api/mute_message_author").post(urlParams.multipart()).getResponse() }

/*
 * view code 
 * <https://github.com/reddit/reddit/blob/master/r2/r2/controllers/api.py#L3034># 
 * <https://www.reddit.com/dev/api#POST_api_remove>POST /api/removemodposts 
 * <https://github.com/reddit/reddit/wiki/OAuth2>
 *  - Remove a link, comment, or modmail message.
 *  - If the thing is a link, it will be removed from all subreddit listings. If 
 * the thing is a comment, it will be redacted and removed from all subreddit 
 * comment listings.
 *  - See also: /api/approve <https://www.reddit.com/dev/api#POST_api_approve>.
 *  - idfullname <https://www.reddit.com/dev/api#fullnames> of a thing
 *  - spamboolean value
 *  - uh / X-Modhash headera modhash <https://www.reddit.com/dev/api#modhashes>
 **/
fun OAuthClient.postRemove(vararg urlParams: Pair<String, Any?>) =
        retry(3) { requestApi("/api/remove").post(urlParams.multipart()).getResponse() }

/*
 * view code 
 * <https://github.com/reddit/reddit/blob/master/r2/r2/controllers/api.py#L3168># 
 * <https://www.reddit.com/dev/api#POST_api_unignore_reports>POST 
 * /api/unignore_reportsmodposts <https://github.com/reddit/reddit/wiki/OAuth2>
 *  - Allow future reports on a thing to cause notifications.
 *  - See also: /api/ignore_reports 
 * <https://www.reddit.com/dev/api#POST_api_ignore_reports>.
 *  - idfullname <https://www.reddit.com/dev/api#fullnames> of a thing
 *  - uh / X-Modhash headera modhash <https://www.reddit.com/dev/api#modhashes>
 **/
fun OAuthClient.postUnignore_reports(vararg urlParams: Pair<String, Any?>) =
        retry(3) { requestApi("/api/unignore_reports").post(urlParams.multipart()).getResponse() }

/*
 * view code 
 * <https://github.com/reddit/reddit/blob/master/r2/r2/controllers/api.py#L1945># 
 * <https://www.reddit.com/dev/api#POST_api_unmute_message_author>POST 
 * /api/unmute_message_authormodcontributors 
 * <https://github.com/reddit/reddit/wiki/OAuth2>
 *  - For unmuting user via modmail.
 *  - idfullname <https://www.reddit.com/dev/api#fullnames> of a thing
 *  - uh / X-Modhash headera modhash <https://www.reddit.com/dev/api#modhashes>
 **/
fun OAuthClient.postUnmute_message_author(vararg urlParams: Pair<String, Any?>) =
        retry(3) { requestApi("/api/unmute_message_author").post(urlParams.multipart()).getResponse() }

/*
 * view code 
 * <https://github.com/reddit/reddit/blob/master/r2/r2/controllers/front.py#L556># 
 * <https://www.reddit.com/dev/api#GET_stylesheet>GET [/r/subreddit]/stylesheet
 * modconfig <https://github.com/reddit/reddit/wiki/OAuth2>
 *  - Redirect to the subreddit's stylesheet if one exists.
 *  - See also: /api/subreddit_stylesheet 
 * <https://www.reddit.com/dev/api#POST_api_subreddit_stylesheet>.
 **/
fun OAuthClient.getRSubredditStylesheet(subreddit: String) =
        retry(3) { requestApi("/r/$subreddit/stylesheet").get().readEntity(String::class.java) }

/*
 * view code 
 * <https://github.com/reddit/reddit/blob/master/r2/r2/controllers/multi.py#L285># 
 * <https://www.reddit.com/dev/api#POST_api_multi_copy>POST /api/multi/copy
 * subscribe <https://github.com/reddit/reddit/wiki/OAuth2>
 *  - Copy a multi.
 *  - Responds with 409 Conflict if the target already exists.
 *  - A "copied from ..." line will automatically be appended to the description.
 *  - display_namea string no longer than 50 characters
 *  - frommultireddit url path
 *  - todestination multireddit url path
 *  - uh / X-Modhash headera modhash <https://www.reddit.com/dev/api#modhashes>
 **/
fun OAuthClient.postMultiCopy(vararg urlParams: Pair<String, Any?>) =
        retry(3) { requestApi("/api/multi/copy").post(urlParams.multipart()).getResponse() }

/*
 * view code 
 * <https://github.com/reddit/reddit/blob/master/r2/r2/controllers/multi.py#L115># 
 * <https://www.reddit.com/dev/api#GET_api_multi_mine>GET /api/multi/mineread 
 * <https://github.com/reddit/reddit/wiki/OAuth2>
 *  - Fetch a list of multis belonging to the current user.
 *  - expand_srsboolean value
 **/
fun OAuthClient.getMultiMine(expandSrs: String?) =
        retry(3) { requestApi("/api/multi/mine",
                "expand_srs" to expandSrs).get().getResponse() }

/*
 * view code 
 * <https://github.com/reddit/reddit/blob/master/r2/r2/controllers/multi.py#L334># 
 * <https://www.reddit.com/dev/api#POST_api_multi_rename>POST /api/multi/rename
 * subscribe <https://github.com/reddit/reddit/wiki/OAuth2>
 *  - Rename a multi.
 *  - display_namea string no longer than 50 characters
 *  - frommultireddit url path
 *  - todestination multireddit url path
 *  - uh / X-Modhash headera modhash <https://www.reddit.com/dev/api#modhashes>
 **/
fun OAuthClient.postMultiRename(vararg urlParams: Pair<String, Any?>) =
        retry(3) { requestApi("/api/multi/rename").post(urlParams.multipart()).getResponse() }

/*
 * view code 
 * <https://github.com/reddit/reddit/blob/master/r2/r2/controllers/multi.py#L104># 
 * <https://www.reddit.com/dev/api#GET_api_multi_user_{username}>GET 
 * /api/multi/user/usernameread <https://github.com/reddit/reddit/wiki/OAuth2>
 *  - Fetch a list of public multis belonging to username
 *  - expand_srsboolean value
 *  - usernameA valid, existing reddit username
 **/
fun OAuthClient.getMultiUserUsername(username: String, expandSrs: String?) =
        retry(3) { requestApi("/api/multi/user/$username",
                "expand_srs" to expandSrs, 
                "username" to username).get().getResponse() }

/*
 * view code 
 * <https://github.com/reddit/reddit/blob/master/r2/r2/controllers/multi.py#L255># 
 * <https://www.reddit.com/dev/api#DELETE_api_multi_{multipath}>DELETE /api/multi/
 * multipathsubscribe <https://github.com/reddit/reddit/wiki/OAuth2>
 *  * → /api/filter/filterpathDelete a multi.
 *  - multipathmultireddit url path
 *  - uh / X-Modhash headera modhash <https://www.reddit.com/dev/api#modhashes>
 *  - expand_srsboolean value
 **/
fun OAuthClient.deleteMultiMultipath(multipath: String, vararg urlParams: Pair<String, Any?>) =
        retry(3) { requestApi("/api/multi/$multipath").method("delete").getResponse() }

/*
 * view code 
 * <https://github.com/reddit/reddit/blob/master/r2/r2/controllers/multi.py#L255># 
 * <https://www.reddit.com/dev/api#DELETE_api_multi_{multipath}>DELETE /api/multi/
 * multipathsubscribe <https://github.com/reddit/reddit/wiki/OAuth2>
 *  * → /api/filter/filterpathDelete a multi.
 *  - multipathmultireddit url path
 *  - uh / X-Modhash headera modhash <https://www.reddit.com/dev/api#modhashes>
 *  - expand_srsboolean value
 **/
fun OAuthClient.deleteFilterFilterpath(filterpath: String, vararg urlParams: Pair<String, Any?>) =
        retry(3) { requestApi("/api/filter/$filterpath").method("delete").getResponse() }

/*
 * view code 
 * <https://github.com/reddit/reddit/blob/master/r2/r2/controllers/multi.py#L127># 
 * <https://www.reddit.com/dev/api#GET_api_multi_{multipath}>GET /api/multi/
 * multipathread <https://github.com/reddit/reddit/wiki/OAuth2>
 *  * → /api/filter/filterpathFetch a multi's data and subreddit list by name.
 *  - expand_srsboolean value
 *  - multipathmultireddit url path
 **/
fun OAuthClient.getMultiMultipath(multipath: String, expandSrs: String?) =
        retry(3) { requestApi("/api/multi/$multipath",
                "expand_srs" to expandSrs, 
                "multipath" to multipath).get().getResponse() }

/*
 * view code 
 * <https://github.com/reddit/reddit/blob/master/r2/r2/controllers/multi.py#L127># 
 * <https://www.reddit.com/dev/api#GET_api_multi_{multipath}>GET /api/multi/
 * multipathread <https://github.com/reddit/reddit/wiki/OAuth2>
 *  * → /api/filter/filterpathFetch a multi's data and subreddit list by name.
 *  - expand_srsboolean value
 *  - multipathmultireddit url path
 **/
fun OAuthClient.getFilterFilterpath(filterpath: String, expandSrs: String?, multipath: String?) =
        retry(3) { requestApi("/api/filter/$filterpath",
                "expand_srs" to expandSrs, 
                "multipath" to multipath).get().getResponse() }

/*
 * view code 
 * <https://github.com/reddit/reddit/blob/master/r2/r2/controllers/multi.py#L199># 
 * <https://www.reddit.com/dev/api#POST_api_multi_{multipath}>POST /api/multi/
 * multipathsubscribe <https://github.com/reddit/reddit/wiki/OAuth2>
 *  * → /api/filter/filterpathCreate a multi. Responds with 409 Conflict if it 
 * already exists.
 *  - modeljson data:
 *  - { "description_md": raw markdown text, "display_name": a string no longer 
 * than 50 characters, "icon_name": one of (`art and design`, `ask`, `books`, 
 * `business`, `cars`, `comics`, `cute animals`, `diy`, `entertainment`, `food and 
 * drink`, `funny`, `games`, `grooming`, `health`, `life advice`, `military`, 
 * `models pinup`, `music`, `news`, `philosophy`, `pictures and gifs`, `science`, 
 * `shopping`, `sports`, `style`, `tech`, `travel`, `unusual stories`, `video`, 
 * ``, `None`), "key_color": a 6-digit rgb hex color, e.g. `#AABBCC`, 
 * "subreddits": [ { "name": subreddit name, }, ... ], "visibility": one of 
 * (`private`, `public`, `hidden`), "weighting_scheme": one of (`classic`, 
 * `fresh`), } multipathmultireddit url path
 *  - uh / X-Modhash headera modhash <https://www.reddit.com/dev/api#modhashes>
 *  - expand_srsboolean value
 **/
fun OAuthClient.postMultiMultipath(multipath: String, vararg urlParams: Pair<String, Any?>) =
        retry(3) { requestApi("/api/multi/$multipath").post(urlParams.multipart()).getResponse() }

/*
 * view code 
 * <https://github.com/reddit/reddit/blob/master/r2/r2/controllers/multi.py#L199># 
 * <https://www.reddit.com/dev/api#POST_api_multi_{multipath}>POST /api/multi/
 * multipathsubscribe <https://github.com/reddit/reddit/wiki/OAuth2>
 *  * → /api/filter/filterpathCreate a multi. Responds with 409 Conflict if it 
 * already exists.
 *  - modeljson data:
 *  - { "description_md": raw markdown text, "display_name": a string no longer 
 * than 50 characters, "icon_name": one of (`art and design`, `ask`, `books`, 
 * `business`, `cars`, `comics`, `cute animals`, `diy`, `entertainment`, `food and 
 * drink`, `funny`, `games`, `grooming`, `health`, `life advice`, `military`, 
 * `models pinup`, `music`, `news`, `philosophy`, `pictures and gifs`, `science`, 
 * `shopping`, `sports`, `style`, `tech`, `travel`, `unusual stories`, `video`, 
 * ``, `None`), "key_color": a 6-digit rgb hex color, e.g. `#AABBCC`, 
 * "subreddits": [ { "name": subreddit name, }, ... ], "visibility": one of 
 * (`private`, `public`, `hidden`), "weighting_scheme": one of (`classic`, 
 * `fresh`), } multipathmultireddit url path
 *  - uh / X-Modhash headera modhash <https://www.reddit.com/dev/api#modhashes>
 *  - expand_srsboolean value
 **/
fun OAuthClient.postFilterFilterpath(filterpath: String, vararg urlParams: Pair<String, Any?>) =
        retry(3) { requestApi("/api/filter/$filterpath").post(urlParams.multipart()).getResponse() }

/*
 * view code 
 * <https://github.com/reddit/reddit/blob/master/r2/r2/controllers/multi.py#L233># 
 * <https://www.reddit.com/dev/api#PUT_api_multi_{multipath}>PUT /api/multi/
 * multipathsubscribe <https://github.com/reddit/reddit/wiki/OAuth2>
 *  * → /api/filter/filterpathCreate or update a multi.
 *  - modeljson data:
 *  - { "description_md": raw markdown text, "display_name": a string no longer 
 * than 50 characters, "icon_name": one of (`art and design`, `ask`, `books`, 
 * `business`, `cars`, `comics`, `cute animals`, `diy`, `entertainment`, `food and 
 * drink`, `funny`, `games`, `grooming`, `health`, `life advice`, `military`, 
 * `models pinup`, `music`, `news`, `philosophy`, `pictures and gifs`, `science`, 
 * `shopping`, `sports`, `style`, `tech`, `travel`, `unusual stories`, `video`, 
 * ``, `None`), "key_color": a 6-digit rgb hex color, e.g. `#AABBCC`, 
 * "subreddits": [ { "name": subreddit name, }, ... ], "visibility": one of 
 * (`private`, `public`, `hidden`), "weighting_scheme": one of (`classic`, 
 * `fresh`), } multipathmultireddit url path
 *  - uh / X-Modhash headera modhash <https://www.reddit.com/dev/api#modhashes>
 *  - expand_srsboolean value
 **/
fun OAuthClient.putMultiMultipath(multipath: String, vararg urlParams: Pair<String, Any?>) =
        retry(3) { requestApi("/api/multi/$multipath").method("put").getResponse() }

/*
 * view code 
 * <https://github.com/reddit/reddit/blob/master/r2/r2/controllers/multi.py#L233># 
 * <https://www.reddit.com/dev/api#PUT_api_multi_{multipath}>PUT /api/multi/
 * multipathsubscribe <https://github.com/reddit/reddit/wiki/OAuth2>
 *  * → /api/filter/filterpathCreate or update a multi.
 *  - modeljson data:
 *  - { "description_md": raw markdown text, "display_name": a string no longer 
 * than 50 characters, "icon_name": one of (`art and design`, `ask`, `books`, 
 * `business`, `cars`, `comics`, `cute animals`, `diy`, `entertainment`, `food and 
 * drink`, `funny`, `games`, `grooming`, `health`, `life advice`, `military`, 
 * `models pinup`, `music`, `news`, `philosophy`, `pictures and gifs`, `science`, 
 * `shopping`, `sports`, `style`, `tech`, `travel`, `unusual stories`, `video`, 
 * ``, `None`), "key_color": a 6-digit rgb hex color, e.g. `#AABBCC`, 
 * "subreddits": [ { "name": subreddit name, }, ... ], "visibility": one of 
 * (`private`, `public`, `hidden`), "weighting_scheme": one of (`classic`, 
 * `fresh`), } multipathmultireddit url path
 *  - uh / X-Modhash headera modhash <https://www.reddit.com/dev/api#modhashes>
 *  - expand_srsboolean value
 **/
fun OAuthClient.putFilterFilterpath(filterpath: String, vararg urlParams: Pair<String, Any?>) =
        retry(3) { requestApi("/api/filter/$filterpath").method("put").getResponse() }

/*
 * view code 
 * <https://github.com/reddit/reddit/blob/master/r2/r2/controllers/multi.py#L427># 
 * <https://www.reddit.com/dev/api#GET_api_multi_{multipath}_description>GET 
 * /api/multi/multipath/descriptionread 
 * <https://github.com/reddit/reddit/wiki/OAuth2>
 *  - Get a multi's description.
 *  - multipathmultireddit url path
 **/
fun OAuthClient.getMultiMultipathDescription(multipath: String) =
        retry(3) { requestApi("/api/multi/$multipath/description",
                "multipath" to multipath).get().getResponse() }

/*
 * view code 
 * <https://github.com/reddit/reddit/blob/master/r2/r2/controllers/multi.py#L440># 
 * <https://www.reddit.com/dev/api#PUT_api_multi_{multipath}_description>PUT 
 * /api/multi/multipath/descriptionread 
 * <https://github.com/reddit/reddit/wiki/OAuth2>
 *  - Change a multi's markdown description.
 *  - modeljson data:
 *  - { "body_md": raw markdown text, } multipathmultireddit url path
 *  - uh / X-Modhash headera modhash <https://www.reddit.com/dev/api#modhashes>
 **/
fun OAuthClient.putMultiMultipathDescription(multipath: String, vararg urlParams: Pair<String, Any?>) =
        retry(3) { requestApi("/api/multi/$multipath/description").method("put").getResponse() }

/*
 * view code 
 * <https://github.com/reddit/reddit/blob/master/r2/r2/controllers/multi.py#L410># 
 * <https://www.reddit.com/dev/api#DELETE_api_multi_{multipath}_r_{srname}>DELETE 
 * /api/multi/multipath/r/srnamesubscribe 
 * <https://github.com/reddit/reddit/wiki/OAuth2>
 *  * → /api/filter/filterpath/r/srnameRemove a subreddit from a multi.
 *  - multipathmultireddit url path
 *  - srnamesubreddit name
 *  - uh / X-Modhash headera modhash <https://www.reddit.com/dev/api#modhashes>
 **/
fun OAuthClient.deleteMultiMultipathRSrname(multipath: String, srname: String, vararg urlParams: Pair<String, Any?>) =
        retry(3) { requestApi("/api/multi/$multipath/r/$srname").method("delete").getResponse() }

/*
 * view code 
 * <https://github.com/reddit/reddit/blob/master/r2/r2/controllers/multi.py#L410># 
 * <https://www.reddit.com/dev/api#DELETE_api_multi_{multipath}_r_{srname}>DELETE 
 * /api/multi/multipath/r/srnamesubscribe 
 * <https://github.com/reddit/reddit/wiki/OAuth2>
 *  * → /api/filter/filterpath/r/srnameRemove a subreddit from a multi.
 *  - multipathmultireddit url path
 *  - srnamesubreddit name
 *  - uh / X-Modhash headera modhash <https://www.reddit.com/dev/api#modhashes>
 **/
fun OAuthClient.deleteFilterFilterpathRSrname(filterpath: String, srname: String, vararg urlParams: Pair<String, Any?>) =
        retry(3) { requestApi("/api/filter/$filterpath/r/$srname").method("delete").getResponse() }

/*
 * view code 
 * <https://github.com/reddit/reddit/blob/master/r2/r2/controllers/multi.py#L371># 
 * <https://www.reddit.com/dev/api#GET_api_multi_{multipath}_r_{srname}>GET 
 * /api/multi/multipath/r/srnameread <https://github.com/reddit/reddit/wiki/OAuth2>
 *  * → /api/filter/filterpath/r/srnameGet data about a subreddit in a multi.
 *  - multipathmultireddit url path
 *  - srnamesubreddit name
 **/
fun OAuthClient.getMultiMultipathRSrname(multipath: String, srname: String) =
        retry(3) { requestApi("/api/multi/$multipath/r/$srname",
                "multipath" to multipath, 
                "srname" to srname).get().getResponse() }

/*
 * view code 
 * <https://github.com/reddit/reddit/blob/master/r2/r2/controllers/multi.py#L371># 
 * <https://www.reddit.com/dev/api#GET_api_multi_{multipath}_r_{srname}>GET 
 * /api/multi/multipath/r/srnameread <https://github.com/reddit/reddit/wiki/OAuth2>
 *  * → /api/filter/filterpath/r/srnameGet data about a subreddit in a multi.
 *  - multipathmultireddit url path
 *  - srnamesubreddit name
 **/
fun OAuthClient.getFilterFilterpathRSrname(filterpath: String, srname: String, multipath: String?) =
        retry(3) { requestApi("/api/filter/$filterpath/r/$srname",
                "multipath" to multipath, 
                "srname" to srname).get().getResponse() }

/*
 * view code 
 * <https://github.com/reddit/reddit/blob/master/r2/r2/controllers/multi.py#L386># 
 * <https://www.reddit.com/dev/api#PUT_api_multi_{multipath}_r_{srname}>PUT 
 * /api/multi/multipath/r/srnamesubscribe 
 * <https://github.com/reddit/reddit/wiki/OAuth2>
 *  * → /api/filter/filterpath/r/srnameAdd a subreddit to a multi.
 *  - modeljson data:
 *  - { "name": subreddit name, } multipathmultireddit url path
 *  - srnamesubreddit name
 *  - uh / X-Modhash headera modhash <https://www.reddit.com/dev/api#modhashes>
 **/
fun OAuthClient.putMultiMultipathRSrname(multipath: String, srname: String, vararg urlParams: Pair<String, Any?>) =
        retry(3) { requestApi("/api/multi/$multipath/r/$srname").method("put").getResponse() }

/*
 * view code 
 * <https://github.com/reddit/reddit/blob/master/r2/r2/controllers/multi.py#L386># 
 * <https://www.reddit.com/dev/api#PUT_api_multi_{multipath}_r_{srname}>PUT 
 * /api/multi/multipath/r/srnamesubscribe 
 * <https://github.com/reddit/reddit/wiki/OAuth2>
 *  * → /api/filter/filterpath/r/srnameAdd a subreddit to a multi.
 *  - modeljson data:
 *  - { "name": subreddit name, } multipathmultireddit url path
 *  - srnamesubreddit name
 *  - uh / X-Modhash headera modhash <https://www.reddit.com/dev/api#modhashes>
 **/
fun OAuthClient.putFilterFilterpathRSrname(filterpath: String, srname: String, vararg urlParams: Pair<String, Any?>) =
        retry(3) { requestApi("/api/filter/$filterpath/r/$srname").method("put").getResponse() }

/*
 * view code 
 * <https://github.com/reddit/reddit/blob/master/r2/r2/controllers/front.py#L1092>#
 *  <https://www.reddit.com/dev/api#GET_search>GET [/r/subreddit]/searchread 
 * <https://github.com/reddit/reddit/wiki/OAuth2>rss support 
 * <https://www.reddit.com/wiki/rss>
 *  - Search links page.
 *  - This endpoint is a listing <https://www.reddit.com/dev/api#listings>.
 *  - afterfullname <https://www.reddit.com/dev/api#fullnames> of a thing
 *  - beforefullname <https://www.reddit.com/dev/api#fullnames> of a thing
 *  - counta positive integer (default: 0)
 *  - include_facetsboolean value
 *  - limitthe maximum number of items desired (default: 25, maximum: 100)
 *  - qa string no longer than 512 characters
 *  - restrict_srboolean value
 *  - show(optional) the string all
 *  - sortone of (relevance, hot, top, new, comments)
 *  - sr_detail(optional) expand subreddits
 *  - syntaxone of (cloudsearch, lucene, plain)
 *  - tone of (hour, day, week, month, year, all)
 *  - type(optional) comma-delimited list of result types (sr, link)
 **/
fun OAuthClient.getRSubredditSearch(subreddit: String, after: String?, before: String?, count: Int?, includeFacets: String?, limit: Int?, q: String?, restrictSr: String?, show: String?, sort: String?, srDetail: Boolean?, syntax: String?, t: String?, type: List<String>?) =
        retry(3) { requestApi("/r/$subreddit/search",
                "after" to after, 
                "before" to before, 
                "count" to count, 
                "include_facets" to includeFacets, 
                "limit" to limit, 
                "q" to q, 
                "restrict_sr" to restrictSr, 
                "show" to show, 
                "sort" to sort, 
                "sr_detail" to srDetail, 
                "syntax" to syntax, 
                "t" to t, 
                "type" to type.csv()).get().readEntity(String::class.java) }

/*
 * view code 
 * <https://github.com/reddit/reddit/blob/master/r2/r2/controllers/listingcontroller.py#L1855>
 * # <https://www.reddit.com/dev/api#GET_about_{where}>GET [/r/subreddit]/about/
 * whereread <https://github.com/reddit/reddit/wiki/OAuth2>rss support 
 * <https://www.reddit.com/wiki/rss>
 *  * → [/r/subreddit]/about/banned
 *  * → [/r/subreddit]/about/muted
 *  * → [/r/subreddit]/about/wikibanned
 *  * → [/r/subreddit]/about/contributors
 *  * → [/r/subreddit]/about/wikicontributors
 *  * → [/r/subreddit]/about/moderatorsThis endpoint is a listing 
 * <https://www.reddit.com/dev/api#listings>.
 *  - afterfullname <https://www.reddit.com/dev/api#fullnames> of a thing
 *  - beforefullname <https://www.reddit.com/dev/api#fullnames> of a thing
 *  - counta positive integer (default: 0)
 *  - limitthe maximum number of items desired (default: 25, maximum: 100)
 *  - show(optional) the string all
 *  - sr_detail(optional) expand subreddits
 *  - userA valid, existing reddit username
 **/
fun OAuthClient.getRSubredditAboutWhere(subreddit: String, where: String, after: String?, before: String?, count: Int?, limit: Int?, show: String?, srDetail: Boolean?, user: String?) =
        retry(3) { requestApi("/r/$subreddit/about/$where",
                "after" to after, 
                "before" to before, 
                "count" to count, 
                "limit" to limit, 
                "show" to show, 
                "sr_detail" to srDetail, 
                "user" to user).get().readEntity(String::class.java) }

/*
 * view code 
 * <https://github.com/reddit/reddit/blob/master/r2/r2/controllers/listingcontroller.py#L1855>
 * # <https://www.reddit.com/dev/api#GET_about_{where}>GET [/r/subreddit]/about/
 * whereread <https://github.com/reddit/reddit/wiki/OAuth2>rss support 
 * <https://www.reddit.com/wiki/rss>
 *  * → [/r/subreddit]/about/banned
 *  * → [/r/subreddit]/about/muted
 *  * → [/r/subreddit]/about/wikibanned
 *  * → [/r/subreddit]/about/contributors
 *  * → [/r/subreddit]/about/wikicontributors
 *  * → [/r/subreddit]/about/moderatorsThis endpoint is a listing 
 * <https://www.reddit.com/dev/api#listings>.
 *  - afterfullname <https://www.reddit.com/dev/api#fullnames> of a thing
 *  - beforefullname <https://www.reddit.com/dev/api#fullnames> of a thing
 *  - counta positive integer (default: 0)
 *  - limitthe maximum number of items desired (default: 25, maximum: 100)
 *  - show(optional) the string all
 *  - sr_detail(optional) expand subreddits
 *  - userA valid, existing reddit username
 **/
fun OAuthClient.getRSubredditAboutBanned(subreddit: String, after: String?, before: String?, count: Int?, limit: Int?, show: String?, srDetail: Boolean?, user: String?) =
        retry(3) { requestApi("/r/$subreddit/about/banned",
                "after" to after, 
                "before" to before, 
                "count" to count, 
                "limit" to limit, 
                "show" to show, 
                "sr_detail" to srDetail, 
                "user" to user).get().readEntity(String::class.java) }

/*
 * view code 
 * <https://github.com/reddit/reddit/blob/master/r2/r2/controllers/listingcontroller.py#L1855>
 * # <https://www.reddit.com/dev/api#GET_about_{where}>GET [/r/subreddit]/about/
 * whereread <https://github.com/reddit/reddit/wiki/OAuth2>rss support 
 * <https://www.reddit.com/wiki/rss>
 *  * → [/r/subreddit]/about/banned
 *  * → [/r/subreddit]/about/muted
 *  * → [/r/subreddit]/about/wikibanned
 *  * → [/r/subreddit]/about/contributors
 *  * → [/r/subreddit]/about/wikicontributors
 *  * → [/r/subreddit]/about/moderatorsThis endpoint is a listing 
 * <https://www.reddit.com/dev/api#listings>.
 *  - afterfullname <https://www.reddit.com/dev/api#fullnames> of a thing
 *  - beforefullname <https://www.reddit.com/dev/api#fullnames> of a thing
 *  - counta positive integer (default: 0)
 *  - limitthe maximum number of items desired (default: 25, maximum: 100)
 *  - show(optional) the string all
 *  - sr_detail(optional) expand subreddits
 *  - userA valid, existing reddit username
 **/
fun OAuthClient.getRSubredditAboutMuted(subreddit: String, after: String?, before: String?, count: Int?, limit: Int?, show: String?, srDetail: Boolean?, user: String?) =
        retry(3) { requestApi("/r/$subreddit/about/muted",
                "after" to after, 
                "before" to before, 
                "count" to count, 
                "limit" to limit, 
                "show" to show, 
                "sr_detail" to srDetail, 
                "user" to user).get().readEntity(String::class.java) }

/*
 * view code 
 * <https://github.com/reddit/reddit/blob/master/r2/r2/controllers/listingcontroller.py#L1855>
 * # <https://www.reddit.com/dev/api#GET_about_{where}>GET [/r/subreddit]/about/
 * whereread <https://github.com/reddit/reddit/wiki/OAuth2>rss support 
 * <https://www.reddit.com/wiki/rss>
 *  * → [/r/subreddit]/about/banned
 *  * → [/r/subreddit]/about/muted
 *  * → [/r/subreddit]/about/wikibanned
 *  * → [/r/subreddit]/about/contributors
 *  * → [/r/subreddit]/about/wikicontributors
 *  * → [/r/subreddit]/about/moderatorsThis endpoint is a listing 
 * <https://www.reddit.com/dev/api#listings>.
 *  - afterfullname <https://www.reddit.com/dev/api#fullnames> of a thing
 *  - beforefullname <https://www.reddit.com/dev/api#fullnames> of a thing
 *  - counta positive integer (default: 0)
 *  - limitthe maximum number of items desired (default: 25, maximum: 100)
 *  - show(optional) the string all
 *  - sr_detail(optional) expand subreddits
 *  - userA valid, existing reddit username
 **/
fun OAuthClient.getRSubredditAboutWikibanned(subreddit: String, after: String?, before: String?, count: Int?, limit: Int?, show: String?, srDetail: Boolean?, user: String?) =
        retry(3) { requestApi("/r/$subreddit/about/wikibanned",
                "after" to after, 
                "before" to before, 
                "count" to count, 
                "limit" to limit, 
                "show" to show, 
                "sr_detail" to srDetail, 
                "user" to user).get().readEntity(String::class.java) }

/*
 * view code 
 * <https://github.com/reddit/reddit/blob/master/r2/r2/controllers/listingcontroller.py#L1855>
 * # <https://www.reddit.com/dev/api#GET_about_{where}>GET [/r/subreddit]/about/
 * whereread <https://github.com/reddit/reddit/wiki/OAuth2>rss support 
 * <https://www.reddit.com/wiki/rss>
 *  * → [/r/subreddit]/about/banned
 *  * → [/r/subreddit]/about/muted
 *  * → [/r/subreddit]/about/wikibanned
 *  * → [/r/subreddit]/about/contributors
 *  * → [/r/subreddit]/about/wikicontributors
 *  * → [/r/subreddit]/about/moderatorsThis endpoint is a listing 
 * <https://www.reddit.com/dev/api#listings>.
 *  - afterfullname <https://www.reddit.com/dev/api#fullnames> of a thing
 *  - beforefullname <https://www.reddit.com/dev/api#fullnames> of a thing
 *  - counta positive integer (default: 0)
 *  - limitthe maximum number of items desired (default: 25, maximum: 100)
 *  - show(optional) the string all
 *  - sr_detail(optional) expand subreddits
 *  - userA valid, existing reddit username
 **/
fun OAuthClient.getRSubredditAboutContributors(subreddit: String, after: String?, before: String?, count: Int?, limit: Int?, show: String?, srDetail: Boolean?, user: String?) =
        retry(3) { requestApi("/r/$subreddit/about/contributors",
                "after" to after, 
                "before" to before, 
                "count" to count, 
                "limit" to limit, 
                "show" to show, 
                "sr_detail" to srDetail, 
                "user" to user).get().readEntity(String::class.java) }

/*
 * view code 
 * <https://github.com/reddit/reddit/blob/master/r2/r2/controllers/listingcontroller.py#L1855>
 * # <https://www.reddit.com/dev/api#GET_about_{where}>GET [/r/subreddit]/about/
 * whereread <https://github.com/reddit/reddit/wiki/OAuth2>rss support 
 * <https://www.reddit.com/wiki/rss>
 *  * → [/r/subreddit]/about/banned
 *  * → [/r/subreddit]/about/muted
 *  * → [/r/subreddit]/about/wikibanned
 *  * → [/r/subreddit]/about/contributors
 *  * → [/r/subreddit]/about/wikicontributors
 *  * → [/r/subreddit]/about/moderatorsThis endpoint is a listing 
 * <https://www.reddit.com/dev/api#listings>.
 *  - afterfullname <https://www.reddit.com/dev/api#fullnames> of a thing
 *  - beforefullname <https://www.reddit.com/dev/api#fullnames> of a thing
 *  - counta positive integer (default: 0)
 *  - limitthe maximum number of items desired (default: 25, maximum: 100)
 *  - show(optional) the string all
 *  - sr_detail(optional) expand subreddits
 *  - userA valid, existing reddit username
 **/
fun OAuthClient.getRSubredditAboutWikicontributors(subreddit: String, after: String?, before: String?, count: Int?, limit: Int?, show: String?, srDetail: Boolean?, user: String?) =
        retry(3) { requestApi("/r/$subreddit/about/wikicontributors",
                "after" to after, 
                "before" to before, 
                "count" to count, 
                "limit" to limit, 
                "show" to show, 
                "sr_detail" to srDetail, 
                "user" to user).get().readEntity(String::class.java) }

/*
 * view code 
 * <https://github.com/reddit/reddit/blob/master/r2/r2/controllers/listingcontroller.py#L1855>
 * # <https://www.reddit.com/dev/api#GET_about_{where}>GET [/r/subreddit]/about/
 * whereread <https://github.com/reddit/reddit/wiki/OAuth2>rss support 
 * <https://www.reddit.com/wiki/rss>
 *  * → [/r/subreddit]/about/banned
 *  * → [/r/subreddit]/about/muted
 *  * → [/r/subreddit]/about/wikibanned
 *  * → [/r/subreddit]/about/contributors
 *  * → [/r/subreddit]/about/wikicontributors
 *  * → [/r/subreddit]/about/moderatorsThis endpoint is a listing 
 * <https://www.reddit.com/dev/api#listings>.
 *  - afterfullname <https://www.reddit.com/dev/api#fullnames> of a thing
 *  - beforefullname <https://www.reddit.com/dev/api#fullnames> of a thing
 *  - counta positive integer (default: 0)
 *  - limitthe maximum number of items desired (default: 25, maximum: 100)
 *  - show(optional) the string all
 *  - sr_detail(optional) expand subreddits
 *  - userA valid, existing reddit username
 **/
fun OAuthClient.getRSubredditAboutModerators(subreddit: String, after: String?, before: String?, count: Int?, limit: Int?, show: String?, srDetail: Boolean?, user: String?) =
        retry(3) { requestApi("/r/$subreddit/about/moderators",
                "after" to after, 
                "before" to before, 
                "count" to count, 
                "limit" to limit, 
                "show" to show, 
                "sr_detail" to srDetail, 
                "user" to user).get().readEntity(String::class.java) }

/*
 * view code 
 * <https://github.com/reddit/reddit/blob/master/r2/r2/controllers/api.py#L2539># 
 * <https://www.reddit.com/dev/api#POST_api_delete_sr_banner>POST [/r/subreddit
 * ]/api/delete_sr_bannermodconfig <https://github.com/reddit/reddit/wiki/OAuth2>
 *  - Remove the subreddit's custom mobile banner.
 *  - See also: /api/upload_sr_img 
 * <https://www.reddit.com/dev/api#POST_api_upload_sr_img>.
 *  - api_typethe string json
 *  - uh / X-Modhash headera modhash <https://www.reddit.com/dev/api#modhashes>
 **/
fun OAuthClient.postRSubredditDelete_sr_banner(subreddit: String, vararg urlParams: Pair<String, Any?>) =
        retry(3) { requestApi("/r/$subreddit/api/delete_sr_banner").post(urlParams.multipart()).readEntity(String::class.java) }

/*
 * view code 
 * <https://github.com/reddit/reddit/blob/master/r2/r2/controllers/api.py#L2479># 
 * <https://www.reddit.com/dev/api#POST_api_delete_sr_header>POST [/r/subreddit
 * ]/api/delete_sr_headermodconfig <https://github.com/reddit/reddit/wiki/OAuth2>
 *  - Remove the subreddit's custom header image.
 *  - The sitewide-default header image will be shown again after this call.
 *  - See also: /api/upload_sr_img 
 * <https://www.reddit.com/dev/api#POST_api_upload_sr_img>.
 *  - api_typethe string json
 *  - uh / X-Modhash headera modhash <https://www.reddit.com/dev/api#modhashes>
 **/
fun OAuthClient.postRSubredditDelete_sr_header(subreddit: String, vararg urlParams: Pair<String, Any?>) =
        retry(3) { requestApi("/r/$subreddit/api/delete_sr_header").post(urlParams.multipart()).readEntity(String::class.java) }

/*
 * view code 
 * <https://github.com/reddit/reddit/blob/master/r2/r2/controllers/api.py#L2512># 
 * <https://www.reddit.com/dev/api#POST_api_delete_sr_icon>POST [/r/subreddit
 * ]/api/delete_sr_iconmodconfig <https://github.com/reddit/reddit/wiki/OAuth2>
 *  - Remove the subreddit's custom mobile icon.
 *  - See also: /api/upload_sr_img 
 * <https://www.reddit.com/dev/api#POST_api_upload_sr_img>.
 *  - api_typethe string json
 *  - uh / X-Modhash headera modhash <https://www.reddit.com/dev/api#modhashes>
 **/
fun OAuthClient.postRSubredditDelete_sr_icon(subreddit: String, vararg urlParams: Pair<String, Any?>) =
        retry(3) { requestApi("/r/$subreddit/api/delete_sr_icon").post(urlParams.multipart()).readEntity(String::class.java) }

/*
 * view code 
 * <https://github.com/reddit/reddit/blob/master/r2/r2/controllers/api.py#L2447># 
 * <https://www.reddit.com/dev/api#POST_api_delete_sr_img>POST [/r/subreddit
 * ]/api/delete_sr_imgmodconfig <https://github.com/reddit/reddit/wiki/OAuth2>
 *  - Remove an image from the subreddit's custom image set.
 *  - The image will no longer count against the subreddit's image limit. However, 
 * the actual image data may still be accessible for an unspecified amount of 
 * time. If the image is currently referenced by the subreddit's stylesheet, that 
 * stylesheet will no longer validate and won't be editable until the image 
 * reference is removed.
 *  - See also: /api/upload_sr_img 
 * <https://www.reddit.com/dev/api#POST_api_upload_sr_img>.
 *  - api_typethe string json
 *  - img_namea valid subreddit image name
 *  - uh / X-Modhash headera modhash <https://www.reddit.com/dev/api#modhashes>
 **/
fun OAuthClient.postRSubredditDelete_sr_img(subreddit: String, vararg urlParams: Pair<String, Any?>) =
        retry(3) { requestApi("/r/$subreddit/api/delete_sr_img").post(urlParams.multipart()).readEntity(String::class.java) }

/*
 * view code 
 * <https://github.com/reddit/reddit/blob/master/r2/r2/controllers/api.py#L5046># 
 * <https://www.reddit.com/dev/api#GET_api_recommend_sr_{srnames}>GET 
 * /api/recommend/sr/srnamesread <https://github.com/reddit/reddit/wiki/OAuth2>
 *  - Return subreddits recommended for the given subreddit(s).
 *  - Gets a list of subreddits recommended for srnames, filtering out any that 
 * appear in the optionalomit param.
 *  - omitcomma-delimited list of subreddit names
 *  - srnamescomma-delimited list of subreddit names
 **/
fun OAuthClient.getRecommendSrSrnames(srnames: List<String>, omit: List<String>?) =
        retry(3) { requestApi("/api/recommend/sr/$srnames",
                "omit" to omit.csv(), 
                "srnames" to srnames.csv()).get().getResponse() }

/*
 * view code 
 * <https://github.com/reddit/reddit/blob/master/r2/r2/controllers/api.py#L4592># 
 * <https://www.reddit.com/dev/api#POST_api_search_reddit_names>POST 
 * /api/search_reddit_namesread <https://github.com/reddit/reddit/wiki/OAuth2>
 *  - List subreddit names that begin with a query string.
 *  - Subreddits whose names begin with query will be returned. If include_over_18 
 * is false, subreddits with over-18 content restrictions will be filtered from 
 * the results.
 *  - If exact is true, only an exact match will be returned.
 *  - exactboolean value
 *  - include_over_18boolean value
 *  - querya string up to 50 characters long, consisting of printable characters.
 **/
fun OAuthClient.postSearch_reddit_names(vararg urlParams: Pair<String, Any?>) =
        retry(3) { requestApi("/api/search_reddit_names").post(urlParams.multipart()).getResponse() }

/*
 * view code 
 * <https://github.com/reddit/reddit/blob/master/r2/r2/controllers/api.py#L2710># 
 * <https://www.reddit.com/dev/api#POST_api_site_admin>POST /api/site_admin
 * modconfig <https://github.com/reddit/reddit/wiki/OAuth2>
 *  - Create or configure a subreddit.
 *  - If sr is specified, the request will attempt to modify the specified 
 * subreddit. If not, a subreddit with namename will be created.
 *  - This endpoint expects all values to be supplied on every request. If 
 * modifying a subset of options, it may be useful to get the current settings from
 * /about/edit.json 
 * <https://www.reddit.com/dev/api#GET_r_%7Bsubreddit%7D_about_edit.json> first.
 *  - For backwards compatibility, description is the sidebar text and 
 * public_description is the publicly visible subreddit description.
 *  - Most of the parameters for this endpoint are identical to options visible in 
 * the user interface and their meanings are best explained there.
 *  - See also: /about/edit.json 
 * <https://www.reddit.com/dev/api#GET_r_%7Bsubreddit%7D_about_edit.json>.
 *  - allow_topboolean value
 *  - api_typethe string json
 *  - captchathe user's response to the CAPTCHA challenge
 *  - collapse_deleted_commentsboolean value
 *  - comment_score_hide_minsan integer between 0 and 1440 (default: 0)
 *  - descriptionraw markdown text
 *  - exclude_banned_modqueueboolean value
 *  - header-titlea string no longer than 500 characters
 *  - hide_adsboolean value
 *  - identhe identifier of the CAPTCHA challenge
 *  - langa valid IETF language tag (underscore separated)
 *  - link_typeone of (any, link, self)
 *  - namesubreddit name
 *  - over_18boolean value
 *  - public_descriptionraw markdown text
 *  - public_trafficboolean value
 *  - show_mediaboolean value
 *  - spam_commentsone of (low, high, all)
 *  - spam_linksone of (low, high, all)
 *  - spam_selfpostsone of (low, high, all)
 *  - srfullname <https://www.reddit.com/dev/api#fullnames> of a thing
 *  - submit_link_labela string no longer than 60 characters
 *  - submit_textraw markdown text
 *  - submit_text_labela string no longer than 60 characters
 *  - suggested_comment_sortone of (confidence, top, new, controversial, old, random
 * ,qa)
 *  - titlea string no longer than 100 characters
 *  - typeone of (gold_restricted, archived, restricted, gold_only, employees_only, 
 * private, public)
 *  - uh / X-Modhash headera modhash <https://www.reddit.com/dev/api#modhashes>
 *  - wiki_edit_agean integer greater than 0 (default: 0)
 *  - wiki_edit_karmaan integer greater than 0 (default: 0)
 *  - wikimodeone of (disabled, modonly, anyone)
 **/
fun OAuthClient.postSite_admin(vararg urlParams: Pair<String, Any?>) =
        retry(3) { requestApi("/api/site_admin").post(urlParams.multipart()).getResponse() }

/*
 * view code 
 * <https://github.com/reddit/reddit/blob/master/r2/r2/controllers/api.py#L417># 
 * <https://www.reddit.com/dev/api#GET_api_submit_text>GET [/r/subreddit
 * ]/api/submit_textsubmit <https://github.com/reddit/reddit/wiki/OAuth2>
 *  - Get the submission text for the subreddit.
 *  - This text is set by the subreddit moderators and intended to be displayed on 
 * the submission form.
 *  - See also: /api/site_admin <https://www.reddit.com/dev/api#POST_api_site_admin>
 * .
 **/
fun OAuthClient.getRSubredditSubmit_text(subreddit: String) =
        retry(3) { requestApi("/r/$subreddit/api/submit_text").get().readEntity(String::class.java) }

/*
 * view code 
 * <https://github.com/reddit/reddit/blob/master/r2/r2/controllers/api.py#L2364># 
 * <https://www.reddit.com/dev/api#POST_api_subreddit_stylesheet>POST [/r/subreddit
 * ]/api/subreddit_stylesheetmodconfig 
 * <https://github.com/reddit/reddit/wiki/OAuth2>
 *  - Update a subreddit's stylesheet.
 *  - op should be save to update the contents of the stylesheet.
 *  - api_typethe string json
 *  - opone of (save, preview)
 *  - reasona string up to 256 characters long, consisting of printable characters.
 *  - stylesheet_contentsthe new stylesheet content
 *  - uh / X-Modhash headera modhash <https://www.reddit.com/dev/api#modhashes>
 **/
fun OAuthClient.postRSubredditSubreddit_stylesheet(subreddit: String, vararg urlParams: Pair<String, Any?>) =
        retry(3) { requestApi("/r/$subreddit/api/subreddit_stylesheet").post(urlParams.multipart()).readEntity(String::class.java) }

/*
 * view code 
 * <https://github.com/reddit/reddit/blob/master/r2/r2/controllers/api.py#L4738># 
 * <https://www.reddit.com/dev/api#GET_api_subreddits_by_topic>GET 
 * /api/subreddits_by_topicread <https://github.com/reddit/reddit/wiki/OAuth2>
 *  - Return a list of subreddits that are relevant to a search query.
 *  - querya string no longer than 50 characters
 **/
fun OAuthClient.getSubreddits_by_topic(query: String?) =
        retry(3) { requestApi("/api/subreddits_by_topic",
                "query" to query).get().getResponse() }

/*
 * view code 
 * <https://github.com/reddit/reddit/blob/master/r2/r2/controllers/api.py#L3774># 
 * <https://www.reddit.com/dev/api#POST_api_subscribe>POST /api/subscribesubscribe 
 * <https://github.com/reddit/reddit/wiki/OAuth2>
 *  - Subscribe to or unsubscribe from a subreddit.
 *  - To subscribe, action should be sub. To unsubscribe, action should be unsub. 
 * The user must have access to the subreddit to be able to subscribe to it.
 *  - See also: /subreddits/mine/ 
 * <https://www.reddit.com/dev/api#GET_subreddits_mine_%7Bwhere%7D>.
 *  - actionone of (sub, unsub)
 *  - srthe name of a subreddit
 *  - uh / X-Modhash headera modhash <https://www.reddit.com/dev/api#modhashes>
 **/
fun OAuthClient.postSubscribe(vararg urlParams: Pair<String, Any?>) =
        retry(3) { requestApi("/api/subscribe").post(urlParams.multipart()).getResponse() }

/*
 * view code 
 * <https://github.com/reddit/reddit/blob/master/r2/r2/controllers/api.py#L2577># 
 * <https://www.reddit.com/dev/api#POST_api_upload_sr_img>POST [/r/subreddit
 * ]/api/upload_sr_imgmodconfig <https://github.com/reddit/reddit/wiki/OAuth2>
 *  - Add or replace a subreddit image, custom header logo, custom mobile icon, or 
 * custom mobile banner.
 *  * If the upload_type value is img, an image for use in the subreddit 
 * stylesheet is uploaded with the name specified inname. 
 *  * If the upload_type value is header then the image uploaded will be the 
 * subreddit's new logo andname will be ignored. 
 *  * If the upload_type value is icon then the image uploaded will be the 
 * subreddit's new mobile icon andname will be ignored. 
 *  * If the upload_type value is banner then the image uploaded will be the 
 * subreddit's new mobile banner andname will be ignored. For backwards 
 * compatibility, ifupload_type is not specified, the header field will be used 
 * instead:
 *  * If the header field has value 0, then upload_type is img. 
 *  * If the header field has value 1, then upload_type is header. The img_type 
 * field specifies whether to store the uploaded image as a PNG or JPEG.
 *  - Subreddits have a limited number of images that can be in use at any given 
 * time. If no image with the specified name already exists, one of the slots will 
 * be consumed.
 *  - If an image with the specified name already exists, it will be replaced. This 
 * does not affect the stylesheet immediately, but will take effect the next time 
 * the stylesheet is saved.
 *  - See also: /api/delete_sr_img 
 * <https://www.reddit.com/dev/api#POST_api_delete_sr_img>, /api/delete_sr_header 
 * <https://www.reddit.com/dev/api#POST_api_delete_sr_header>, /api/delete_sr_icon 
 * <https://www.reddit.com/dev/api#POST_api_delete_sr_icon>, and 
 * /api/delete_sr_banner <https://www.reddit.com/dev/api#POST_api_delete_sr_banner>
 * .
 *  - filefile upload with maximum size of 500 KiB
 *  - formid(optional) can be ignored
 *  - headeran integer between 0 and 1
 *  - img_typeone of png or jpg (default: png)
 *  - namea valid subreddit image name
 *  - uh / X-Modhash headera modhash <https://www.reddit.com/dev/api#modhashes>
 *  - upload_typeone of (img, header, icon, banner)
 **/
fun OAuthClient.postRSubredditUpload_sr_img(subreddit: String, vararg urlParams: Pair<String, Any?>) =
        retry(3) { requestApi("/r/$subreddit/api/upload_sr_img").post(urlParams.multipart()).readEntity(String::class.java) }

/*
 * view code 
 * <https://github.com/reddit/reddit/blob/master/r2/r2/controllers/front.py#L919># 
 * <https://www.reddit.com/dev/api#GET_r_{subreddit}_about>GET /r/subreddit/about
 * read <https://github.com/reddit/reddit/wiki/OAuth2>
 *  - Return information about the subreddit.
 *  - Data includes the subscriber count, description, and header image.
 **/
fun OAuthClient.getRSubredditAbout(subreddit: String) =
        retry(3) { requestApi("/r/$subreddit/about").get().readEntity(String::class.java) }

/*
 * view code 
 * <https://github.com/reddit/reddit/blob/master/r2/r2/controllers/front.py#L897># 
 * <https://www.reddit.com/dev/api#GET_r_{subreddit}_about_edit>GET /r/subreddit
 * /about/editmodconfig <https://github.com/reddit/reddit/wiki/OAuth2>
 *  - Get the current settings of a subreddit.
 *  - In the API, this returns the current settings of the subreddit as used by 
 * /api/site_admin <https://www.reddit.com/dev/api#POST_api_site_admin>. On the 
 * HTML site, it will display a form for editing the subreddit.
 *  - createdone of (true, false)
 *  - location
 **/
fun OAuthClient.getRSubredditAboutEdit(subreddit: String, created: Boolean?) =
        retry(3) { requestApi("/r/$subreddit/about/edit",
                "created" to created).get().readEntity(String::class.java) }

/*
 * view code 
 * <https://github.com/reddit/reddit/blob/master/r2/r2/controllers/front.py#L938># 
 * <https://www.reddit.com/dev/api#GET_rules>GET [/r/subreddit]/rulesread 
 * <https://github.com/reddit/reddit/wiki/OAuth2>
 *  - Get the rules for the current subreddit
 **/
fun OAuthClient.getRSubredditRules(subreddit: String) =
        retry(3) { requestApi("/r/$subreddit/rules").get().readEntity(String::class.java) }

/*
 * view code 
 * <https://github.com/reddit/reddit/blob/master/r2/r2/controllers/front.py#L931># 
 * <https://www.reddit.com/dev/api#GET_sidebar>GET [/r/subreddit]/sidebarread 
 * <https://github.com/reddit/reddit/wiki/OAuth2>
 *  - Get the sidebar for the current subreddit
 **/
fun OAuthClient.getRSubredditSidebar(subreddit: String) =
        retry(3) { requestApi("/r/$subreddit/sidebar").get().readEntity(String::class.java) }

/*
 * view code 
 * <https://github.com/reddit/reddit/blob/master/r2/r2/lib/validator/validator.py#L200>
 * # <https://www.reddit.com/dev/api#GET_sticky>GET [/r/subreddit]/stickyread 
 * <https://github.com/reddit/reddit/wiki/OAuth2>
 *  - Redirect to one of the posts stickied in the current subreddit
 *  - The "num" argument can be used to select a specific sticky, and will default 
 * to 1 (the top sticky) if not specified. Will 404 if there is not currently a 
 * sticky post in this subreddit.
 *  - numan integer between 1 and 2 (default: 1)
 **/
fun OAuthClient.getRSubredditSticky(subreddit: String, num: String?) =
        retry(3) { requestApi("/r/$subreddit/sticky",
                "num" to num).get().readEntity(String::class.java) }

/*
 * view code 
 * <https://github.com/reddit/reddit/blob/master/r2/r2/controllers/listingcontroller.py#L1626>
 * # <https://www.reddit.com/dev/api#GET_subreddits_mine_{where}>GET 
 * /subreddits/mine/wheremysubreddits 
 * <https://github.com/reddit/reddit/wiki/OAuth2>rss support 
 * <https://www.reddit.com/wiki/rss>
 *  * → /subreddits/mine/subscriber
 *  * → /subreddits/mine/contributor
 *  * → /subreddits/mine/moderatorGet subreddits the user has a relationship with.
 *  - The where parameter chooses which subreddits are returned as follows:
 *  * subscriber - subreddits the user is subscribed to 
 *  * contributor - subreddits the user is an approved submitter in 
 *  * moderator - subreddits the user is a moderator of See also: /api/subscribe 
 * <https://www.reddit.com/dev/api#POST_api_subscribe>, /api/friend 
 * <https://www.reddit.com/dev/api#POST_api_friend>, and 
 * /api/accept_moderator_invite 
 * <https://www.reddit.com/dev/api#POST_api_accept_moderator_invite>.
 *  - This endpoint is a listing <https://www.reddit.com/dev/api#listings>.
 *  - afterfullname <https://www.reddit.com/dev/api#fullnames> of a thing
 *  - beforefullname <https://www.reddit.com/dev/api#fullnames> of a thing
 *  - counta positive integer (default: 0)
 *  - limitthe maximum number of items desired (default: 25, maximum: 100)
 *  - show(optional) the string all
 *  - sr_detail(optional) expand subreddits
 **/
fun OAuthClient.getSubredditsMineWhere(where: String, after: String?, before: String?, count: Int?, limit: Int?, show: String?, srDetail: Boolean?) =
        retry(3) { requestApi("/subreddits/mine/$where",
                "after" to after, 
                "before" to before, 
                "count" to count, 
                "limit" to limit, 
                "show" to show, 
                "sr_detail" to srDetail).get().readEntity(String::class.java) }

/*
 * view code 
 * <https://github.com/reddit/reddit/blob/master/r2/r2/controllers/listingcontroller.py#L1626>
 * # <https://www.reddit.com/dev/api#GET_subreddits_mine_{where}>GET 
 * /subreddits/mine/wheremysubreddits 
 * <https://github.com/reddit/reddit/wiki/OAuth2>rss support 
 * <https://www.reddit.com/wiki/rss>
 *  * → /subreddits/mine/subscriber
 *  * → /subreddits/mine/contributor
 *  * → /subreddits/mine/moderatorGet subreddits the user has a relationship with.
 *  - The where parameter chooses which subreddits are returned as follows:
 *  * subscriber - subreddits the user is subscribed to 
 *  * contributor - subreddits the user is an approved submitter in 
 *  * moderator - subreddits the user is a moderator of See also: /api/subscribe 
 * <https://www.reddit.com/dev/api#POST_api_subscribe>, /api/friend 
 * <https://www.reddit.com/dev/api#POST_api_friend>, and 
 * /api/accept_moderator_invite 
 * <https://www.reddit.com/dev/api#POST_api_accept_moderator_invite>.
 *  - This endpoint is a listing <https://www.reddit.com/dev/api#listings>.
 *  - afterfullname <https://www.reddit.com/dev/api#fullnames> of a thing
 *  - beforefullname <https://www.reddit.com/dev/api#fullnames> of a thing
 *  - counta positive integer (default: 0)
 *  - limitthe maximum number of items desired (default: 25, maximum: 100)
 *  - show(optional) the string all
 *  - sr_detail(optional) expand subreddits
 **/
fun OAuthClient.getSubredditsMineSubscriber(after: String?, before: String?, count: Int?, limit: Int?, show: String?, srDetail: Boolean?) =
        retry(3) { requestApi("/subreddits/mine/subscriber",
                "after" to after, 
                "before" to before, 
                "count" to count, 
                "limit" to limit, 
                "show" to show, 
                "sr_detail" to srDetail).get().readEntity(String::class.java) }

/*
 * view code 
 * <https://github.com/reddit/reddit/blob/master/r2/r2/controllers/listingcontroller.py#L1626>
 * # <https://www.reddit.com/dev/api#GET_subreddits_mine_{where}>GET 
 * /subreddits/mine/wheremysubreddits 
 * <https://github.com/reddit/reddit/wiki/OAuth2>rss support 
 * <https://www.reddit.com/wiki/rss>
 *  * → /subreddits/mine/subscriber
 *  * → /subreddits/mine/contributor
 *  * → /subreddits/mine/moderatorGet subreddits the user has a relationship with.
 *  - The where parameter chooses which subreddits are returned as follows:
 *  * subscriber - subreddits the user is subscribed to 
 *  * contributor - subreddits the user is an approved submitter in 
 *  * moderator - subreddits the user is a moderator of See also: /api/subscribe 
 * <https://www.reddit.com/dev/api#POST_api_subscribe>, /api/friend 
 * <https://www.reddit.com/dev/api#POST_api_friend>, and 
 * /api/accept_moderator_invite 
 * <https://www.reddit.com/dev/api#POST_api_accept_moderator_invite>.
 *  - This endpoint is a listing <https://www.reddit.com/dev/api#listings>.
 *  - afterfullname <https://www.reddit.com/dev/api#fullnames> of a thing
 *  - beforefullname <https://www.reddit.com/dev/api#fullnames> of a thing
 *  - counta positive integer (default: 0)
 *  - limitthe maximum number of items desired (default: 25, maximum: 100)
 *  - show(optional) the string all
 *  - sr_detail(optional) expand subreddits
 **/
fun OAuthClient.getSubredditsMineContributor(after: String?, before: String?, count: Int?, limit: Int?, show: String?, srDetail: Boolean?) =
        retry(3) { requestApi("/subreddits/mine/contributor",
                "after" to after, 
                "before" to before, 
                "count" to count, 
                "limit" to limit, 
                "show" to show, 
                "sr_detail" to srDetail).get().readEntity(String::class.java) }

/*
 * view code 
 * <https://github.com/reddit/reddit/blob/master/r2/r2/controllers/listingcontroller.py#L1626>
 * # <https://www.reddit.com/dev/api#GET_subreddits_mine_{where}>GET 
 * /subreddits/mine/wheremysubreddits 
 * <https://github.com/reddit/reddit/wiki/OAuth2>rss support 
 * <https://www.reddit.com/wiki/rss>
 *  * → /subreddits/mine/subscriber
 *  * → /subreddits/mine/contributor
 *  * → /subreddits/mine/moderatorGet subreddits the user has a relationship with.
 *  - The where parameter chooses which subreddits are returned as follows:
 *  * subscriber - subreddits the user is subscribed to 
 *  * contributor - subreddits the user is an approved submitter in 
 *  * moderator - subreddits the user is a moderator of See also: /api/subscribe 
 * <https://www.reddit.com/dev/api#POST_api_subscribe>, /api/friend 
 * <https://www.reddit.com/dev/api#POST_api_friend>, and 
 * /api/accept_moderator_invite 
 * <https://www.reddit.com/dev/api#POST_api_accept_moderator_invite>.
 *  - This endpoint is a listing <https://www.reddit.com/dev/api#listings>.
 *  - afterfullname <https://www.reddit.com/dev/api#fullnames> of a thing
 *  - beforefullname <https://www.reddit.com/dev/api#fullnames> of a thing
 *  - counta positive integer (default: 0)
 *  - limitthe maximum number of items desired (default: 25, maximum: 100)
 *  - show(optional) the string all
 *  - sr_detail(optional) expand subreddits
 **/
fun OAuthClient.getSubredditsMineModerator(after: String?, before: String?, count: Int?, limit: Int?, show: String?, srDetail: Boolean?) =
        retry(3) { requestApi("/subreddits/mine/moderator",
                "after" to after, 
                "before" to before, 
                "count" to count, 
                "limit" to limit, 
                "show" to show, 
                "sr_detail" to srDetail).get().readEntity(String::class.java) }

/*
 * view code 
 * <https://github.com/reddit/reddit/blob/master/r2/r2/controllers/front.py#L1034>#
 *  <https://www.reddit.com/dev/api#GET_subreddits_search>GET /subreddits/search
 * read <https://github.com/reddit/reddit/wiki/OAuth2>rss support 
 * <https://www.reddit.com/wiki/rss>
 *  - Search subreddits by title and description.
 *  - This endpoint is a listing <https://www.reddit.com/dev/api#listings>.
 *  - afterfullname <https://www.reddit.com/dev/api#fullnames> of a thing
 *  - beforefullname <https://www.reddit.com/dev/api#fullnames> of a thing
 *  - counta positive integer (default: 0)
 *  - limitthe maximum number of items desired (default: 25, maximum: 100)
 *  - qa search query
 *  - show(optional) the string all
 *  - sortone of (relevance, activity)
 *  - sr_detail(optional) expand subreddits
 **/
fun OAuthClient.getSubredditsSearch(after: String?, before: String?, count: Int?, limit: Int?, q: String?, show: String?, sort: String?, srDetail: Boolean?) =
        retry(3) { requestApi("/subreddits/search",
                "after" to after, 
                "before" to before, 
                "count" to count, 
                "limit" to limit, 
                "q" to q, 
                "show" to show, 
                "sort" to sort, 
                "sr_detail" to srDetail).get().readEntity(String::class.java) }

/*
 * view code 
 * <https://github.com/reddit/reddit/blob/master/r2/r2/controllers/listingcontroller.py#L1507>
 * # <https://www.reddit.com/dev/api#GET_subreddits_{where}>GET /subreddits/where
 * read <https://github.com/reddit/reddit/wiki/OAuth2>rss support 
 * <https://www.reddit.com/wiki/rss>
 *  * → /subreddits/popular
 *  * → /subreddits/new
 *  * → /subreddits/gold
 *  * → /subreddits/defaultGet all subreddits.
 *  - The where parameter chooses the order in which the subreddits are displayed. 
 * popular sorts on the activity of the subreddit and the position of the 
 * subreddits can shift around.new sorts the subreddits based on their creation 
 * date, newest first.
 *  - This endpoint is a listing <https://www.reddit.com/dev/api#listings>.
 *  - afterfullname <https://www.reddit.com/dev/api#fullnames> of a thing
 *  - beforefullname <https://www.reddit.com/dev/api#fullnames> of a thing
 *  - counta positive integer (default: 0)
 *  - limitthe maximum number of items desired (default: 25, maximum: 100)
 *  - show(optional) the string all
 *  - sr_detail(optional) expand subreddits
 **/
fun OAuthClient.getSubredditsWhere(where: String, after: String?, before: String?, count: Int?, limit: Int?, show: String?, srDetail: Boolean?) =
        retry(3) { requestApi("/subreddits/$where",
                "after" to after, 
                "before" to before, 
                "count" to count, 
                "limit" to limit, 
                "show" to show, 
                "sr_detail" to srDetail).get().readEntity(String::class.java) }

/*
 * view code 
 * <https://github.com/reddit/reddit/blob/master/r2/r2/controllers/listingcontroller.py#L1507>
 * # <https://www.reddit.com/dev/api#GET_subreddits_{where}>GET /subreddits/where
 * read <https://github.com/reddit/reddit/wiki/OAuth2>rss support 
 * <https://www.reddit.com/wiki/rss>
 *  * → /subreddits/popular
 *  * → /subreddits/new
 *  * → /subreddits/gold
 *  * → /subreddits/defaultGet all subreddits.
 *  - The where parameter chooses the order in which the subreddits are displayed. 
 * popular sorts on the activity of the subreddit and the position of the 
 * subreddits can shift around.new sorts the subreddits based on their creation 
 * date, newest first.
 *  - This endpoint is a listing <https://www.reddit.com/dev/api#listings>.
 *  - afterfullname <https://www.reddit.com/dev/api#fullnames> of a thing
 *  - beforefullname <https://www.reddit.com/dev/api#fullnames> of a thing
 *  - counta positive integer (default: 0)
 *  - limitthe maximum number of items desired (default: 25, maximum: 100)
 *  - show(optional) the string all
 *  - sr_detail(optional) expand subreddits
 **/
fun OAuthClient.getSubredditsPopular(after: String?, before: String?, count: Int?, limit: Int?, show: String?, srDetail: Boolean?) =
        retry(3) { requestApi("/subreddits/popular",
                "after" to after, 
                "before" to before, 
                "count" to count, 
                "limit" to limit, 
                "show" to show, 
                "sr_detail" to srDetail).get().readEntity(String::class.java) }

/*
 * view code 
 * <https://github.com/reddit/reddit/blob/master/r2/r2/controllers/listingcontroller.py#L1507>
 * # <https://www.reddit.com/dev/api#GET_subreddits_{where}>GET /subreddits/where
 * read <https://github.com/reddit/reddit/wiki/OAuth2>rss support 
 * <https://www.reddit.com/wiki/rss>
 *  * → /subreddits/popular
 *  * → /subreddits/new
 *  * → /subreddits/gold
 *  * → /subreddits/defaultGet all subreddits.
 *  - The where parameter chooses the order in which the subreddits are displayed. 
 * popular sorts on the activity of the subreddit and the position of the 
 * subreddits can shift around.new sorts the subreddits based on their creation 
 * date, newest first.
 *  - This endpoint is a listing <https://www.reddit.com/dev/api#listings>.
 *  - afterfullname <https://www.reddit.com/dev/api#fullnames> of a thing
 *  - beforefullname <https://www.reddit.com/dev/api#fullnames> of a thing
 *  - counta positive integer (default: 0)
 *  - limitthe maximum number of items desired (default: 25, maximum: 100)
 *  - show(optional) the string all
 *  - sr_detail(optional) expand subreddits
 **/
fun OAuthClient.getSubredditsNew(after: String?, before: String?, count: Int?, limit: Int?, show: String?, srDetail: Boolean?) =
        retry(3) { requestApi("/subreddits/new",
                "after" to after, 
                "before" to before, 
                "count" to count, 
                "limit" to limit, 
                "show" to show, 
                "sr_detail" to srDetail).get().readEntity(String::class.java) }

/*
 * view code 
 * <https://github.com/reddit/reddit/blob/master/r2/r2/controllers/listingcontroller.py#L1507>
 * # <https://www.reddit.com/dev/api#GET_subreddits_{where}>GET /subreddits/where
 * read <https://github.com/reddit/reddit/wiki/OAuth2>rss support 
 * <https://www.reddit.com/wiki/rss>
 *  * → /subreddits/popular
 *  * → /subreddits/new
 *  * → /subreddits/gold
 *  * → /subreddits/defaultGet all subreddits.
 *  - The where parameter chooses the order in which the subreddits are displayed. 
 * popular sorts on the activity of the subreddit and the position of the 
 * subreddits can shift around.new sorts the subreddits based on their creation 
 * date, newest first.
 *  - This endpoint is a listing <https://www.reddit.com/dev/api#listings>.
 *  - afterfullname <https://www.reddit.com/dev/api#fullnames> of a thing
 *  - beforefullname <https://www.reddit.com/dev/api#fullnames> of a thing
 *  - counta positive integer (default: 0)
 *  - limitthe maximum number of items desired (default: 25, maximum: 100)
 *  - show(optional) the string all
 *  - sr_detail(optional) expand subreddits
 **/
fun OAuthClient.getSubredditsGold(after: String?, before: String?, count: Int?, limit: Int?, show: String?, srDetail: Boolean?) =
        retry(3) { requestApi("/subreddits/gold",
                "after" to after, 
                "before" to before, 
                "count" to count, 
                "limit" to limit, 
                "show" to show, 
                "sr_detail" to srDetail).get().readEntity(String::class.java) }

/*
 * view code 
 * <https://github.com/reddit/reddit/blob/master/r2/r2/controllers/listingcontroller.py#L1507>
 * # <https://www.reddit.com/dev/api#GET_subreddits_{where}>GET /subreddits/where
 * read <https://github.com/reddit/reddit/wiki/OAuth2>rss support 
 * <https://www.reddit.com/wiki/rss>
 *  * → /subreddits/popular
 *  * → /subreddits/new
 *  * → /subreddits/gold
 *  * → /subreddits/defaultGet all subreddits.
 *  - The where parameter chooses the order in which the subreddits are displayed. 
 * popular sorts on the activity of the subreddit and the position of the 
 * subreddits can shift around.new sorts the subreddits based on their creation 
 * date, newest first.
 *  - This endpoint is a listing <https://www.reddit.com/dev/api#listings>.
 *  - afterfullname <https://www.reddit.com/dev/api#fullnames> of a thing
 *  - beforefullname <https://www.reddit.com/dev/api#fullnames> of a thing
 *  - counta positive integer (default: 0)
 *  - limitthe maximum number of items desired (default: 25, maximum: 100)
 *  - show(optional) the string all
 *  - sr_detail(optional) expand subreddits
 **/
fun OAuthClient.getSubredditsDefault(after: String?, before: String?, count: Int?, limit: Int?, show: String?, srDetail: Boolean?) =
        retry(3) { requestApi("/subreddits/default",
                "after" to after, 
                "before" to before, 
                "count" to count, 
                "limit" to limit, 
                "show" to show, 
                "sr_detail" to srDetail).get().readEntity(String::class.java) }

/*
 * view code 
 * <https://github.com/reddit/reddit/blob/master/r2/r2/controllers/api.py#L1020># 
 * <https://www.reddit.com/dev/api#POST_api_friend>POST [/r/subreddit]/api/friend
 * any <https://github.com/reddit/reddit/wiki/OAuth2>
 *  - Create a relationship between a user and another user or subreddit
 *  - OAuth2 use requires appropriate scope based on the 'type' of the relationship:
 *  * moderator: Use "moderator_invite" 
 *  * moderator_invite: modothers 
 *  * contributor: modcontributors 
 *  * banned: modcontributors 
 *  * muted: modcontributors 
 *  * wikibanned: modcontributors and modwiki 
 *  * wikicontributor: modcontributors and modwiki 
 *  * friend: Use /api/v1/me/friends/{username} 
 * <https://www.reddit.com/dev/api#PUT_api_v1_me_friends_%7Busername%7D> 
 *  * enemy: Use /api/block <https://www.reddit.com/dev/api#POST_api_block> 
 * Complement toPOST_unfriend <https://www.reddit.com/dev/api#POST_api_unfriend>
 *  - api_typethe string json
 *  - ban_messageraw markdown text
 *  - ban_reasona string no longer than 100 characters
 *  - containerdurationan integer between 1 and 999
 *  - namethe name of an existing user
 *  - notea string no longer than 300 characters
 *  - permissionstypeone of (friend, moderator, moderator_invite, contributor, 
 * banned, muted, wikibanned, wikicontributor)
 *  - uh / X-Modhash headera modhash <https://www.reddit.com/dev/api#modhashes>
 **/
fun OAuthClient.postRSubredditFriend(subreddit: String, vararg urlParams: Pair<String, Any?>) =
        retry(3) { requestApi("/r/$subreddit/api/friend").post(urlParams.multipart()).readEntity(String::class.java) }

/*
 * view code 
 * <https://github.com/reddit/reddit/blob/master/r2/r2/controllers/api.py#L974># 
 * <https://www.reddit.com/dev/api#POST_api_setpermissions>POST [/r/subreddit
 * ]/api/setpermissionsmodothers <https://github.com/reddit/reddit/wiki/OAuth2>
 *  - api_typethe string json
 *  - namethe name of an existing user
 *  - permissionstypeuh / X-Modhash headera modhash 
 * <https://www.reddit.com/dev/api#modhashes>
 **/
fun OAuthClient.postRSubredditSetpermissions(subreddit: String, vararg urlParams: Pair<String, Any?>) =
        retry(3) { requestApi("/r/$subreddit/api/setpermissions").post(urlParams.multipart()).readEntity(String::class.java) }

/*
 * view code 
 * <https://github.com/reddit/reddit/blob/master/r2/r2/controllers/api.py#L868># 
 * <https://www.reddit.com/dev/api#POST_api_unfriend>POST [/r/subreddit
 * ]/api/unfriendany <https://github.com/reddit/reddit/wiki/OAuth2>
 *  - Remove a relationship between a user and another user or subreddit
 *  - The user can either be passed in by name (nuser) or by fullname 
 * <https://www.reddit.com/dev/api#fullnames> (iuser). If type is friend or enemy, 
 * 'container' MUST be the current user's fullname; for other types, the subreddit 
 * must be set via URL (e.g.,/r/funny/api/unfriend 
 * <https://www.reddit.com/r/funny/api/unfriend>)
 *  - OAuth2 use requires appropriate scope based on the 'type' of the relationship:
 *  * moderator: modothers 
 *  * moderator_invite: modothers 
 *  * contributor: modcontributors 
 *  * banned: modcontributors 
 *  * muted: modcontributors 
 *  * wikibanned: modcontributors and modwiki 
 *  * wikicontributor: modcontributors and modwiki 
 *  * friend: Use /api/v1/me/friends/{username} 
 * <https://www.reddit.com/dev/api#DELETE_api_v1_me_friends_%7Busername%7D> 
 *  * enemy: privatemessages Complement to POST_friend 
 * <https://www.reddit.com/dev/api#POST_api_friend>
 *  - containeridfullname <https://www.reddit.com/dev/api#fullnames> of a thing
 *  - namethe name of an existing user
 *  - typeone of (friend, enemy, moderator, moderator_invite, contributor, banned, 
 * muted, wikibanned, wikicontributor)
 *  - uh / X-Modhash headera modhash <https://www.reddit.com/dev/api#modhashes>
 **/
fun OAuthClient.postRSubredditUnfriend(subreddit: String, vararg urlParams: Pair<String, Any?>) =
        retry(3) { requestApi("/r/$subreddit/api/unfriend").post(urlParams.multipart()).readEntity(String::class.java) }

/*
 * view code 
 * <https://github.com/reddit/reddit/blob/master/r2/r2/controllers/api.py#L238># 
 * <https://www.reddit.com/dev/api#GET_api_username_available>GET 
 * /api/username_available
 *  - Check whether a username is available for registration.
 *  - usera valid, unused, username
 **/
fun OAuthClient.getUsername_available(user: String?) =
        retry(3) { requestApi("/api/username_available",
                "user" to user).get().getResponse() }

/*
 * view code 
 * <https://github.com/reddit/reddit/blob/master/r2/r2/controllers/apiv1/user.py#L202>
 * # <https://www.reddit.com/dev/api#DELETE_api_v1_me_friends_{username}>DELETE 
 * /api/v1/me/friends/usernamesubscribe 
 * <https://github.com/reddit/reddit/wiki/OAuth2>
 *  - Stop being friends with a user.
 *  - idA valid, existing reddit username
 **/
fun OAuthClient.deleteV1MeFriendsUsername(username: String, vararg urlParams: Pair<String, Any?>) =
        retry(3) { requestApi("/api/v1/me/friends/$username").method("delete").getResponse() }

/*
 * view code 
 * <https://github.com/reddit/reddit/blob/master/r2/r2/controllers/apiv1/user.py#L191>
 * # <https://www.reddit.com/dev/api#GET_api_v1_me_friends_{username}>GET 
 * /api/v1/me/friends/usernamemysubreddits 
 * <https://github.com/reddit/reddit/wiki/OAuth2>
 *  - Get information about a specific 'friend', such as notes.
 *  - idA valid, existing reddit username
 **/
fun OAuthClient.getV1MeFriendsUsername(username: String, id: String?) =
        retry(3) { requestApi("/api/v1/me/friends/$username",
                "id" to id).get().getResponse() }

/*
 * view code 
 * <https://github.com/reddit/reddit/blob/master/r2/r2/controllers/apiv1/user.py#L146>
 * # <https://www.reddit.com/dev/api#PUT_api_v1_me_friends_{username}>PUT 
 * /api/v1/me/friends/usernamesubscribe 
 * <https://github.com/reddit/reddit/wiki/OAuth2>
 *  - Create or update a "friend" relationship.
 *  - This operation is idempotent. It can be used to add a new friend, or update 
 * an existing friend (e.g., add/change the note on that friend)
 *  - This endpoint expects JSON data of this format{ "name": A valid, existing 
 * reddit username, "note": a string no longer than 300 characters, } 
 **/
fun OAuthClient.putV1MeFriendsUsername(username: String, vararg urlParams: Pair<String, Any?>) =
        retry(3) { requestApi("/api/v1/me/friends/$username").method("put").getResponse() }

/*
 * view code 
 * <https://github.com/reddit/reddit/blob/master/r2/r2/controllers/apiv1/user.py#L82>
 * # <https://www.reddit.com/dev/api#GET_api_v1_user_{username}_trophies>GET 
 * /api/v1/user/username/trophiesread 
 * <https://github.com/reddit/reddit/wiki/OAuth2>
 *  - Return a list of trophies for the a given user.
 *  - idA valid, existing reddit username
 **/
fun OAuthClient.getV1UserUsernameTrophies(username: String, id: String?) =
        retry(3) { requestApi("/api/v1/user/$username/trophies",
                "id" to id).get().getResponse() }

/*
 * view code 
 * <https://github.com/reddit/reddit/blob/master/r2/r2/controllers/listingcontroller.py#L1015>
 * # <https://www.reddit.com/dev/api#GET_user_{username}_about>GET /user/username
 * /aboutread <https://github.com/reddit/reddit/wiki/OAuth2>
 *  - Return information about the user, including karma and gold status.
 *  - usernamethe name of an existing user
 **/
fun OAuthClient.getUserUsernameAbout(username: String) =
        retry(3) { requestApi("/user/$username/about",
                "username" to username).get().readEntity(String::class.java) }

/*
 * view code 
 * <https://github.com/reddit/reddit/blob/master/r2/r2/controllers/listingcontroller.py#L890>
 * # <https://www.reddit.com/dev/api#GET_user_{username}_{where}>GET /user/username
 * /wherehistory <https://github.com/reddit/reddit/wiki/OAuth2>rss support 
 * <https://www.reddit.com/wiki/rss>
 *  * → /user/username/overview
 *  * → /user/username/submitted
 *  * → /user/username/comments
 *  * → /user/username/upvoted
 *  * → /user/username/downvoted
 *  * → /user/username/hidden
 *  * → /user/username/saved
 *  * → /user/username/gildedThis endpoint is a listing 
 * <https://www.reddit.com/dev/api#listings>.
 *  - showone of (given)
 *  - sortone of (hot, new, top, controversial)
 *  - tone of (hour, day, week, month, year, all)
 *  - usernamethe name of an existing user
 *  - afterfullname <https://www.reddit.com/dev/api#fullnames> of a thing
 *  - beforefullname <https://www.reddit.com/dev/api#fullnames> of a thing
 *  - counta positive integer (default: 0)
 *  - limitthe maximum number of items desired (default: 25, maximum: 100)
 *  - sr_detail(optional) expand subreddits
 **/
fun OAuthClient.getUserUsernameWhere(username: String, where: String, show: String?, sort: String?, t: String?, after: String?, before: String?, count: Int?, limit: Int?, srDetail: Boolean?) =
        retry(3) { requestApi("/user/$username/$where",
                "show" to show, 
                "sort" to sort, 
                "t" to t, 
                "username" to username, 
                "after" to after, 
                "before" to before, 
                "count" to count, 
                "limit" to limit, 
                "sr_detail" to srDetail).get().readEntity(String::class.java) }

/*
 * view code 
 * <https://github.com/reddit/reddit/blob/master/r2/r2/controllers/listingcontroller.py#L890>
 * # <https://www.reddit.com/dev/api#GET_user_{username}_{where}>GET /user/username
 * /wherehistory <https://github.com/reddit/reddit/wiki/OAuth2>rss support 
 * <https://www.reddit.com/wiki/rss>
 *  * → /user/username/overview
 *  * → /user/username/submitted
 *  * → /user/username/comments
 *  * → /user/username/upvoted
 *  * → /user/username/downvoted
 *  * → /user/username/hidden
 *  * → /user/username/saved
 *  * → /user/username/gildedThis endpoint is a listing 
 * <https://www.reddit.com/dev/api#listings>.
 *  - showone of (given)
 *  - sortone of (hot, new, top, controversial)
 *  - tone of (hour, day, week, month, year, all)
 *  - usernamethe name of an existing user
 *  - afterfullname <https://www.reddit.com/dev/api#fullnames> of a thing
 *  - beforefullname <https://www.reddit.com/dev/api#fullnames> of a thing
 *  - counta positive integer (default: 0)
 *  - limitthe maximum number of items desired (default: 25, maximum: 100)
 *  - sr_detail(optional) expand subreddits
 **/
fun OAuthClient.getUserUsernameOverview(username: String, show: String?, sort: String?, t: String?, after: String?, before: String?, count: Int?, limit: Int?, srDetail: Boolean?) =
        retry(3) { requestApi("/user/$username/overview",
                "show" to show, 
                "sort" to sort, 
                "t" to t, 
                "username" to username, 
                "after" to after, 
                "before" to before, 
                "count" to count, 
                "limit" to limit, 
                "sr_detail" to srDetail).get().readEntity(String::class.java) }

/*
 * view code 
 * <https://github.com/reddit/reddit/blob/master/r2/r2/controllers/listingcontroller.py#L890>
 * # <https://www.reddit.com/dev/api#GET_user_{username}_{where}>GET /user/username
 * /wherehistory <https://github.com/reddit/reddit/wiki/OAuth2>rss support 
 * <https://www.reddit.com/wiki/rss>
 *  * → /user/username/overview
 *  * → /user/username/submitted
 *  * → /user/username/comments
 *  * → /user/username/upvoted
 *  * → /user/username/downvoted
 *  * → /user/username/hidden
 *  * → /user/username/saved
 *  * → /user/username/gildedThis endpoint is a listing 
 * <https://www.reddit.com/dev/api#listings>.
 *  - showone of (given)
 *  - sortone of (hot, new, top, controversial)
 *  - tone of (hour, day, week, month, year, all)
 *  - usernamethe name of an existing user
 *  - afterfullname <https://www.reddit.com/dev/api#fullnames> of a thing
 *  - beforefullname <https://www.reddit.com/dev/api#fullnames> of a thing
 *  - counta positive integer (default: 0)
 *  - limitthe maximum number of items desired (default: 25, maximum: 100)
 *  - sr_detail(optional) expand subreddits
 **/
fun OAuthClient.getUserUsernameSubmitted(username: String, show: String?, sort: String?, t: String?, after: String?, before: String?, count: Int?, limit: Int?, srDetail: Boolean?) =
        retry(3) { requestApi("/user/$username/submitted",
                "show" to show, 
                "sort" to sort, 
                "t" to t, 
                "username" to username, 
                "after" to after, 
                "before" to before, 
                "count" to count, 
                "limit" to limit, 
                "sr_detail" to srDetail).get().readEntity(String::class.java) }

/*
 * view code 
 * <https://github.com/reddit/reddit/blob/master/r2/r2/controllers/listingcontroller.py#L890>
 * # <https://www.reddit.com/dev/api#GET_user_{username}_{where}>GET /user/username
 * /wherehistory <https://github.com/reddit/reddit/wiki/OAuth2>rss support 
 * <https://www.reddit.com/wiki/rss>
 *  * → /user/username/overview
 *  * → /user/username/submitted
 *  * → /user/username/comments
 *  * → /user/username/upvoted
 *  * → /user/username/downvoted
 *  * → /user/username/hidden
 *  * → /user/username/saved
 *  * → /user/username/gildedThis endpoint is a listing 
 * <https://www.reddit.com/dev/api#listings>.
 *  - showone of (given)
 *  - sortone of (hot, new, top, controversial)
 *  - tone of (hour, day, week, month, year, all)
 *  - usernamethe name of an existing user
 *  - afterfullname <https://www.reddit.com/dev/api#fullnames> of a thing
 *  - beforefullname <https://www.reddit.com/dev/api#fullnames> of a thing
 *  - counta positive integer (default: 0)
 *  - limitthe maximum number of items desired (default: 25, maximum: 100)
 *  - sr_detail(optional) expand subreddits
 **/
fun OAuthClient.getUserUsernameComments(username: String, show: String?, sort: String?, t: String?, after: String?, before: String?, count: Int?, limit: Int?, srDetail: Boolean?) =
        retry(3) { requestApi("/user/$username/comments",
                "show" to show, 
                "sort" to sort, 
                "t" to t, 
                "username" to username, 
                "after" to after, 
                "before" to before, 
                "count" to count, 
                "limit" to limit, 
                "sr_detail" to srDetail).get().readEntity(String::class.java) }

/*
 * view code 
 * <https://github.com/reddit/reddit/blob/master/r2/r2/controllers/listingcontroller.py#L890>
 * # <https://www.reddit.com/dev/api#GET_user_{username}_{where}>GET /user/username
 * /wherehistory <https://github.com/reddit/reddit/wiki/OAuth2>rss support 
 * <https://www.reddit.com/wiki/rss>
 *  * → /user/username/overview
 *  * → /user/username/submitted
 *  * → /user/username/comments
 *  * → /user/username/upvoted
 *  * → /user/username/downvoted
 *  * → /user/username/hidden
 *  * → /user/username/saved
 *  * → /user/username/gildedThis endpoint is a listing 
 * <https://www.reddit.com/dev/api#listings>.
 *  - showone of (given)
 *  - sortone of (hot, new, top, controversial)
 *  - tone of (hour, day, week, month, year, all)
 *  - usernamethe name of an existing user
 *  - afterfullname <https://www.reddit.com/dev/api#fullnames> of a thing
 *  - beforefullname <https://www.reddit.com/dev/api#fullnames> of a thing
 *  - counta positive integer (default: 0)
 *  - limitthe maximum number of items desired (default: 25, maximum: 100)
 *  - sr_detail(optional) expand subreddits
 **/
fun OAuthClient.getUserUsernameUpvoted(username: String, show: String?, sort: String?, t: String?, after: String?, before: String?, count: Int?, limit: Int?, srDetail: Boolean?) =
        retry(3) { requestApi("/user/$username/upvoted",
                "show" to show, 
                "sort" to sort, 
                "t" to t, 
                "username" to username, 
                "after" to after, 
                "before" to before, 
                "count" to count, 
                "limit" to limit, 
                "sr_detail" to srDetail).get().readEntity(String::class.java) }

/*
 * view code 
 * <https://github.com/reddit/reddit/blob/master/r2/r2/controllers/listingcontroller.py#L890>
 * # <https://www.reddit.com/dev/api#GET_user_{username}_{where}>GET /user/username
 * /wherehistory <https://github.com/reddit/reddit/wiki/OAuth2>rss support 
 * <https://www.reddit.com/wiki/rss>
 *  * → /user/username/overview
 *  * → /user/username/submitted
 *  * → /user/username/comments
 *  * → /user/username/upvoted
 *  * → /user/username/downvoted
 *  * → /user/username/hidden
 *  * → /user/username/saved
 *  * → /user/username/gildedThis endpoint is a listing 
 * <https://www.reddit.com/dev/api#listings>.
 *  - showone of (given)
 *  - sortone of (hot, new, top, controversial)
 *  - tone of (hour, day, week, month, year, all)
 *  - usernamethe name of an existing user
 *  - afterfullname <https://www.reddit.com/dev/api#fullnames> of a thing
 *  - beforefullname <https://www.reddit.com/dev/api#fullnames> of a thing
 *  - counta positive integer (default: 0)
 *  - limitthe maximum number of items desired (default: 25, maximum: 100)
 *  - sr_detail(optional) expand subreddits
 **/
fun OAuthClient.getUserUsernameDownvoted(username: String, show: String?, sort: String?, t: String?, after: String?, before: String?, count: Int?, limit: Int?, srDetail: Boolean?) =
        retry(3) { requestApi("/user/$username/downvoted",
                "show" to show, 
                "sort" to sort, 
                "t" to t, 
                "username" to username, 
                "after" to after, 
                "before" to before, 
                "count" to count, 
                "limit" to limit, 
                "sr_detail" to srDetail).get().readEntity(String::class.java) }

/*
 * view code 
 * <https://github.com/reddit/reddit/blob/master/r2/r2/controllers/listingcontroller.py#L890>
 * # <https://www.reddit.com/dev/api#GET_user_{username}_{where}>GET /user/username
 * /wherehistory <https://github.com/reddit/reddit/wiki/OAuth2>rss support 
 * <https://www.reddit.com/wiki/rss>
 *  * → /user/username/overview
 *  * → /user/username/submitted
 *  * → /user/username/comments
 *  * → /user/username/upvoted
 *  * → /user/username/downvoted
 *  * → /user/username/hidden
 *  * → /user/username/saved
 *  * → /user/username/gildedThis endpoint is a listing 
 * <https://www.reddit.com/dev/api#listings>.
 *  - showone of (given)
 *  - sortone of (hot, new, top, controversial)
 *  - tone of (hour, day, week, month, year, all)
 *  - usernamethe name of an existing user
 *  - afterfullname <https://www.reddit.com/dev/api#fullnames> of a thing
 *  - beforefullname <https://www.reddit.com/dev/api#fullnames> of a thing
 *  - counta positive integer (default: 0)
 *  - limitthe maximum number of items desired (default: 25, maximum: 100)
 *  - sr_detail(optional) expand subreddits
 **/
fun OAuthClient.getUserUsernameHidden(username: String, show: String?, sort: String?, t: String?, after: String?, before: String?, count: Int?, limit: Int?, srDetail: Boolean?) =
        retry(3) { requestApi("/user/$username/hidden",
                "show" to show, 
                "sort" to sort, 
                "t" to t, 
                "username" to username, 
                "after" to after, 
                "before" to before, 
                "count" to count, 
                "limit" to limit, 
                "sr_detail" to srDetail).get().readEntity(String::class.java) }

/*
 * view code 
 * <https://github.com/reddit/reddit/blob/master/r2/r2/controllers/listingcontroller.py#L890>
 * # <https://www.reddit.com/dev/api#GET_user_{username}_{where}>GET /user/username
 * /wherehistory <https://github.com/reddit/reddit/wiki/OAuth2>rss support 
 * <https://www.reddit.com/wiki/rss>
 *  * → /user/username/overview
 *  * → /user/username/submitted
 *  * → /user/username/comments
 *  * → /user/username/upvoted
 *  * → /user/username/downvoted
 *  * → /user/username/hidden
 *  * → /user/username/saved
 *  * → /user/username/gildedThis endpoint is a listing 
 * <https://www.reddit.com/dev/api#listings>.
 *  - showone of (given)
 *  - sortone of (hot, new, top, controversial)
 *  - tone of (hour, day, week, month, year, all)
 *  - usernamethe name of an existing user
 *  - afterfullname <https://www.reddit.com/dev/api#fullnames> of a thing
 *  - beforefullname <https://www.reddit.com/dev/api#fullnames> of a thing
 *  - counta positive integer (default: 0)
 *  - limitthe maximum number of items desired (default: 25, maximum: 100)
 *  - sr_detail(optional) expand subreddits
 **/
fun OAuthClient.getUserUsernameSaved(username: String, show: String?, sort: String?, t: String?, after: String?, before: String?, count: Int?, limit: Int?, srDetail: Boolean?) =
        retry(3) { requestApi("/user/$username/saved",
                "show" to show, 
                "sort" to sort, 
                "t" to t, 
                "username" to username, 
                "after" to after, 
                "before" to before, 
                "count" to count, 
                "limit" to limit, 
                "sr_detail" to srDetail).get().readEntity(String::class.java) }

/*
 * view code 
 * <https://github.com/reddit/reddit/blob/master/r2/r2/controllers/listingcontroller.py#L890>
 * # <https://www.reddit.com/dev/api#GET_user_{username}_{where}>GET /user/username
 * /wherehistory <https://github.com/reddit/reddit/wiki/OAuth2>rss support 
 * <https://www.reddit.com/wiki/rss>
 *  * → /user/username/overview
 *  * → /user/username/submitted
 *  * → /user/username/comments
 *  * → /user/username/upvoted
 *  * → /user/username/downvoted
 *  * → /user/username/hidden
 *  * → /user/username/saved
 *  * → /user/username/gildedThis endpoint is a listing 
 * <https://www.reddit.com/dev/api#listings>.
 *  - showone of (given)
 *  - sortone of (hot, new, top, controversial)
 *  - tone of (hour, day, week, month, year, all)
 *  - usernamethe name of an existing user
 *  - afterfullname <https://www.reddit.com/dev/api#fullnames> of a thing
 *  - beforefullname <https://www.reddit.com/dev/api#fullnames> of a thing
 *  - counta positive integer (default: 0)
 *  - limitthe maximum number of items desired (default: 25, maximum: 100)
 *  - sr_detail(optional) expand subreddits
 **/
fun OAuthClient.getUserUsernameGilded(username: String, show: String?, sort: String?, t: String?, after: String?, before: String?, count: Int?, limit: Int?, srDetail: Boolean?) =
        retry(3) { requestApi("/user/$username/gilded",
                "show" to show, 
                "sort" to sort, 
                "t" to t, 
                "username" to username, 
                "after" to after, 
                "before" to before, 
                "count" to count, 
                "limit" to limit, 
                "sr_detail" to srDetail).get().readEntity(String::class.java) }

/*
 * view code 
 * <https://github.com/reddit/reddit/blob/master/r2/r2/controllers/wiki.py#L452># 
 * <https://www.reddit.com/dev/api#POST_api_wiki_alloweditor_{act}>POST [/r/
 * subreddit]/api/wiki/alloweditor/actmodwiki 
 * <https://github.com/reddit/reddit/wiki/OAuth2>
 *  * → [/r/subreddit]/api/wiki/alloweditor/del
 *  * → [/r/subreddit]/api/wiki/alloweditor/addAllow/deny username to edit this 
 * wikipage
 *  - actone of (del, add)
 *  - pagethe name of an existing wiki page
 *  - uh / X-Modhash headera modhash <https://www.reddit.com/dev/api#modhashes>
 *  - usernamethe name of an existing user
 **/
fun OAuthClient.postRSubredditWikiAlloweditorAct(subreddit: String, act: String, vararg urlParams: Pair<String, Any?>) =
        retry(3) { requestApi("/r/$subreddit/api/wiki/alloweditor/$act").post(urlParams.multipart()).readEntity(String::class.java) }

/*
 * view code 
 * <https://github.com/reddit/reddit/blob/master/r2/r2/controllers/wiki.py#L452># 
 * <https://www.reddit.com/dev/api#POST_api_wiki_alloweditor_{act}>POST [/r/
 * subreddit]/api/wiki/alloweditor/actmodwiki 
 * <https://github.com/reddit/reddit/wiki/OAuth2>
 *  * → [/r/subreddit]/api/wiki/alloweditor/del
 *  * → [/r/subreddit]/api/wiki/alloweditor/addAllow/deny username to edit this 
 * wikipage
 *  - actone of (del, add)
 *  - pagethe name of an existing wiki page
 *  - uh / X-Modhash headera modhash <https://www.reddit.com/dev/api#modhashes>
 *  - usernamethe name of an existing user
 **/
fun OAuthClient.postRSubredditWikiAlloweditorDel(subreddit: String, vararg urlParams: Pair<String, Any?>) =
        retry(3) { requestApi("/r/$subreddit/api/wiki/alloweditor/del").post(urlParams.multipart()).readEntity(String::class.java) }

/*
 * view code 
 * <https://github.com/reddit/reddit/blob/master/r2/r2/controllers/wiki.py#L452># 
 * <https://www.reddit.com/dev/api#POST_api_wiki_alloweditor_{act}>POST [/r/
 * subreddit]/api/wiki/alloweditor/actmodwiki 
 * <https://github.com/reddit/reddit/wiki/OAuth2>
 *  * → [/r/subreddit]/api/wiki/alloweditor/del
 *  * → [/r/subreddit]/api/wiki/alloweditor/addAllow/deny username to edit this 
 * wikipage
 *  - actone of (del, add)
 *  - pagethe name of an existing wiki page
 *  - uh / X-Modhash headera modhash <https://www.reddit.com/dev/api#modhashes>
 *  - usernamethe name of an existing user
 **/
fun OAuthClient.postRSubredditWikiAlloweditorAdd(subreddit: String, vararg urlParams: Pair<String, Any?>) =
        retry(3) { requestApi("/r/$subreddit/api/wiki/alloweditor/add").post(urlParams.multipart()).readEntity(String::class.java) }

/*
 * view code 
 * <https://github.com/reddit/reddit/blob/master/r2/r2/controllers/wiki.py#L376># 
 * <https://www.reddit.com/dev/api#POST_api_wiki_edit>POST [/r/subreddit
 * ]/api/wiki/editwikiedit <https://github.com/reddit/reddit/wiki/OAuth2>
 *  - Edit a wiki page
 *  - contentpagethe name of an existing page or a new page to create
 *  - previousthe starting point revision for this edit
 *  - reasona string up to 256 characters long, consisting of printable characters.
 *  - uh / X-Modhash headera modhash <https://www.reddit.com/dev/api#modhashes>
 **/
fun OAuthClient.postRSubredditWikiEdit(subreddit: String, vararg urlParams: Pair<String, Any?>) =
        retry(3) { requestApi("/r/$subreddit/api/wiki/edit").post(urlParams.multipart()).readEntity(String::class.java) }

/*
 * view code 
 * <https://github.com/reddit/reddit/blob/master/r2/r2/controllers/wiki.py#L493># 
 * <https://www.reddit.com/dev/api#POST_api_wiki_hide>POST [/r/subreddit
 * ]/api/wiki/hidemodwiki <https://github.com/reddit/reddit/wiki/OAuth2>
 *  - Toggle the public visibility of a wiki page revision
 *  - pagethe name of an existing wiki page
 *  - revisiona wiki revision ID
 *  - uh / X-Modhash headera modhash <https://www.reddit.com/dev/api#modhashes>
 **/
fun OAuthClient.postRSubredditWikiHide(subreddit: String, vararg urlParams: Pair<String, Any?>) =
        retry(3) { requestApi("/r/$subreddit/api/wiki/hide").post(urlParams.multipart()).readEntity(String::class.java) }

/*
 * view code 
 * <https://github.com/reddit/reddit/blob/master/r2/r2/controllers/wiki.py#L508># 
 * <https://www.reddit.com/dev/api#POST_api_wiki_revert>POST [/r/subreddit
 * ]/api/wiki/revertmodwiki <https://github.com/reddit/reddit/wiki/OAuth2>
 *  - Revert a wiki page to revision
 *  - pagethe name of an existing wiki page
 *  - revisiona wiki revision ID
 *  - uh / X-Modhash headera modhash <https://www.reddit.com/dev/api#modhashes>
 **/
fun OAuthClient.postRSubredditWikiRevert(subreddit: String, vararg urlParams: Pair<String, Any?>) =
        retry(3) { requestApi("/r/$subreddit/api/wiki/revert").post(urlParams.multipart()).readEntity(String::class.java) }

/*
 * view code 
 * <https://github.com/reddit/reddit/blob/master/r2/r2/lib/validator/validator.py#L200>
 * # <https://www.reddit.com/dev/api#GET_wiki_discussions_{page}>GET [/r/subreddit
 * ]/wiki/discussions/pagewikiread <https://github.com/reddit/reddit/wiki/OAuth2>
 *  - Retrieve a list of discussions about this wiki page
 *  - This endpoint is a listing <https://www.reddit.com/dev/api#listings>.
 *  - afterfullname <https://www.reddit.com/dev/api#fullnames> of a thing
 *  - beforefullname <https://www.reddit.com/dev/api#fullnames> of a thing
 *  - counta positive integer (default: 0)
 *  - limitthe maximum number of items desired (default: 25, maximum: 100)
 *  - pagethe name of an existing wiki page
 *  - show(optional) the string all
 *  - sr_detail(optional) expand subreddits
 **/
fun OAuthClient.getRSubredditWikiDiscussionsPage(subreddit: String, page: String, after: String?, before: String?, count: Int?, limit: Int?, show: String?, srDetail: Boolean?) =
        retry(3) { requestApi("/r/$subreddit/wiki/discussions/$page",
                "after" to after, 
                "before" to before, 
                "count" to count, 
                "limit" to limit, 
                "page" to page, 
                "show" to show, 
                "sr_detail" to srDetail).get().readEntity(String::class.java) }

/*
 * view code 
 * <https://github.com/reddit/reddit/blob/master/r2/r2/controllers/wiki.py#L258># 
 * <https://www.reddit.com/dev/api#GET_wiki_pages>GET [/r/subreddit]/wiki/pages
 * wikiread <https://github.com/reddit/reddit/wiki/OAuth2>
 *  - Retrieve a list of wiki pages in this subreddit
 **/
fun OAuthClient.getRSubredditWikiPages(subreddit: String) =
        retry(3) { requestApi("/r/$subreddit/wiki/pages").get().readEntity(String::class.java) }

/*
 * view code 
 * <https://github.com/reddit/reddit/blob/master/r2/r2/lib/validator/validator.py#L200>
 * # <https://www.reddit.com/dev/api#GET_wiki_revisions>GET [/r/subreddit
 * ]/wiki/revisionswikiread <https://github.com/reddit/reddit/wiki/OAuth2>
 *  - Retrieve a list of recently changed wiki pages in this subreddit
 *  - afterfullname <https://www.reddit.com/dev/api#fullnames> of a thing
 *  - beforefullname <https://www.reddit.com/dev/api#fullnames> of a thing
 *  - counta positive integer (default: 0)
 *  - limitthe maximum number of items desired (default: 25, maximum: 100)
 *  - show(optional) the string all
 *  - sr_detail(optional) expand subreddits
 **/
fun OAuthClient.getRSubredditWikiRevisions(subreddit: String, after: String?, before: String?, count: Int?, limit: Int?, show: String?, srDetail: Boolean?) =
        retry(3) { requestApi("/r/$subreddit/wiki/revisions",
                "after" to after, 
                "before" to before, 
                "count" to count, 
                "limit" to limit, 
                "show" to show, 
                "sr_detail" to srDetail).get().readEntity(String::class.java) }

/*
 * view code 
 * <https://github.com/reddit/reddit/blob/master/r2/r2/lib/validator/validator.py#L200>
 * # <https://www.reddit.com/dev/api#GET_wiki_revisions_{page}>GET [/r/subreddit
 * ]/wiki/revisions/pagewikiread <https://github.com/reddit/reddit/wiki/OAuth2>
 *  - Retrieve a list of revisions of this wiki page
 *  - This endpoint is a listing <https://www.reddit.com/dev/api#listings>.
 *  - afterfullname <https://www.reddit.com/dev/api#fullnames> of a thing
 *  - beforefullname <https://www.reddit.com/dev/api#fullnames> of a thing
 *  - counta positive integer (default: 0)
 *  - limitthe maximum number of items desired (default: 25, maximum: 100)
 *  - pagethe name of an existing wiki page
 *  - show(optional) the string all
 *  - sr_detail(optional) expand subreddits
 **/
fun OAuthClient.getRSubredditWikiRevisionsPage(subreddit: String, page: String, after: String?, before: String?, count: Int?, limit: Int?, show: String?, srDetail: Boolean?) =
        retry(3) { requestApi("/r/$subreddit/wiki/revisions/$page",
                "after" to after, 
                "before" to before, 
                "count" to count, 
                "limit" to limit, 
                "page" to page, 
                "show" to show, 
                "sr_detail" to srDetail).get().readEntity(String::class.java) }

/*
 * view code 
 * <https://github.com/reddit/reddit/blob/master/r2/r2/lib/validator/validator.py#L200>
 * # <https://www.reddit.com/dev/api#GET_wiki_settings_{page}>GET [/r/subreddit
 * ]/wiki/settings/pagemodwiki <https://github.com/reddit/reddit/wiki/OAuth2>
 *  - Retrieve the current permission settings for page
 *  - pagethe name of an existing wiki page
 **/
fun OAuthClient.getRSubredditWikiSettingsPage(subreddit: String, page: String) =
        retry(3) { requestApi("/r/$subreddit/wiki/settings/$page",
                "page" to page).get().readEntity(String::class.java) }

/*
 * view code 
 * <https://github.com/reddit/reddit/blob/master/r2/r2/lib/validator/validator.py#L200>
 * # <https://www.reddit.com/dev/api#POST_wiki_settings_{page}>POST [/r/subreddit
 * ]/wiki/settings/pagemodwiki <https://github.com/reddit/reddit/wiki/OAuth2>
 *  - Update the permissions and visibility of wiki page
 *  - listedboolean value
 *  - pagethe name of an existing wiki page
 *  - permlevelan integer
 *  - uh / X-Modhash headera modhash <https://www.reddit.com/dev/api#modhashes>
 **/
fun OAuthClient.postRSubredditWikiSettingsPage(subreddit: String, page: String, vararg urlParams: Pair<String, Any?>) =
        retry(3) { requestApi("/r/$subreddit/wiki/settings/$page").post(urlParams.multipart()).readEntity(String::class.java) }

/*
 * view code 
 * <https://github.com/reddit/reddit/blob/master/r2/r2/lib/validator/validator.py#L200>
 * # <https://www.reddit.com/dev/api#GET_wiki_{page}>GET [/r/subreddit]/wiki/page
 * wikiread <https://github.com/reddit/reddit/wiki/OAuth2>
 *  - Return the content of a wiki page
 *  - If v is given, show the wiki page as it was at that version If both v and v2 
 * are given, show a diff of the two
 *  - pagethe name of an existing wiki page
 *  - va wiki revision ID
 *  - v2a wiki revision ID
 **/
fun OAuthClient.getRSubredditWikiPage(subreddit: String, page: String, v: String?, v2: String?) =
        retry(3) { requestApi("/r/$subreddit/wiki/$page",
                "page" to page, 
                "v" to v, 
                "v2" to v2).get().readEntity(String::class.java) }

// Generated API end.

