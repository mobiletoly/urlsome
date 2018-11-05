package com.github.mobiletoly.urlsome

import java.net.URLEncoder

/**
 * Hassle-free URL builder for Kotlin.
 *
 * [baseUrl] used as it is, it will not be encoded.
 * [paths] - list of path components. Be aware that components will be encoded if you use "/" operator,
 * e.g. if you perform ("user" / "/hello") then resulting URL will be "user/%2Fhello". If you don't want
 * to encode path components - use "*" operator instead, e.g. ("user * "/hello") will result in "user//hello".
 * [queries] - list of query components. Will be properly encoded.
 *
 * For example to get this URL:
 * "http://127.0.0.1:8081/api/v1/use%23rs/Toly+Pochkin/report?sort=firstName&country=US#main"
 * you can use this code:
 *
 * val userId = "Toly Pochkin"
 * val baseUrl = Urlsome("http://127.0.0.1:8081/api/v1")
 * val url = (baseUrl / "users" / userId / "report") [
 *     "sort" to "firstName",
 *     "country" to "US"
 * ] `#` "main"
 *
 * Another way to get the same URL is:
 *
 * val baseUrl = Urlsome("http://127.0.0.1:8081/api/v1")
 * val url = (
 *      (baseUrl / "users" / userId / "report")
 *     `?` ("sort" to "firstName")
 *     `?` ("country" to "US")
 *     `#` "main"
 * )
 *
 * or even be more fancy:
 * val url = ((baseUrl / "users" / userId / "report")
 *     `?` ("sort" to "firstName") `&` ("country" to "US") `#` "main")
 */
class Urlsome constructor(
    private val baseUrl: String,
    private val options: Options,
    private val paths: List<String> = emptyList(),
    private val queries: List<Pair<String, Any?>> = emptyList(),
    private val fragments: List<Pair<String, Any?>> = emptyList()
) {
    constructor(
        baseUrl: String,
        options: Options = Options()
    ) : this(
        baseUrl = baseUrl,
        options = options,
        paths = emptyList(),
        queries = emptyList(),
        fragments = emptyList()
    )

    operator fun div(path: String) = appendPath(path, encode = true)
    operator fun div(paths: Collection<String>) = appendPath(*paths.toTypedArray(), encode = true)
    operator fun div(paths: Array<String>) = appendPath(*paths, encode = true)

    operator fun times(path: String) = appendPath(path, encode = false)
    operator fun times(paths: Collection<String>) = appendPath(*paths.toTypedArray(), encode = false)
    operator fun times(paths: Array<String>) = appendPath(*paths, encode = false)

    operator fun get(params: Collection<Pair<String, Any?>>) = appendQuery(*params.toTypedArray())
    operator fun get(vararg params: Pair<String, Any?>) = appendQuery(*params)
    operator fun get(params: Map<String, Any?>) = appendQuery(*params.toList().toTypedArray())
    infix fun `?`(params: Collection<Pair<String, Any?>>) = get(params)
    infix fun `?`(params: Pair<String, Any?>) = get(params)
    infix fun `?`(params: Map<String, Any?>) = get(params)
    infix fun `&`(params: Collection<Pair<String, Any?>>) = get(params)
    infix fun `&`(params: Pair<String, Any?>) = get(params)
    infix fun `&`(params: Map<String, Any?>) = get(params)

    infix fun `#`(params: Collection<Pair<String, Any?>>) = appendFragment(*params.toTypedArray())
    infix fun `#`(params: Pair<String, Any?>) = appendFragment(params)
    infix fun `#`(params: Map<String, Any?>) = appendFragment(*params.toList().toTypedArray())
    infix fun `#`(param: String) = appendFragment(param to null)

    fun appendPath(vararg path: String, encode: Boolean = true) = Urlsome(
        baseUrl = this.baseUrl,
        options = this.options,
        paths = this.paths + path.map {
            if (encode) {
                URLEncoder.encode(it, "UTF-8")
            } else {
                it
            }
        },
        queries = this.queries,
        fragments = this.fragments
    )

    fun appendQuery(vararg queries: Pair<String, Any?>) = Urlsome(
        baseUrl = this.baseUrl,
        options = this.options,
        paths = this.paths,
        queries = this.queries + queries,
        fragments = this.fragments
    )

    fun appendFragment(vararg fragments: Pair<String, Any?>) = Urlsome(
        baseUrl = this.baseUrl,
        options = this.options,
        paths = this.paths,
        queries = this.queries,
        fragments = this.fragments + fragments
    )

    override fun toString() = build()

    private fun build(): String {
        val fullPath = concatPathComponents()
        val fullQuery = concatComponents(components = this.queries, excludeNullValues = options.excludeNullQueryValues)
        val fullFragment = concatComponents(components = this.fragments, excludeNullValues = options.excludeNullFragmentValues)
        val sb = StringBuilder(fullPath)
        if (sb.isNotEmpty() && fullQuery.isNotEmpty()) {
            sb.append("?")
        }
        sb.append(fullQuery)
        if (fullFragment.isNotEmpty()) {
            sb.append('#')
            sb.append(fullFragment)
        }
        return sb.toString()
    }

    private fun concatPathComponents(): String {
        val sb = StringBuilder(baseUrl)
        if (! sb.endsWith('/') && paths.isNotEmpty()) {
            sb.append('/')
        }
        sb.append(paths.joinToString("/") { it })
        return sb.toString()
    }

    private fun concatComponents(components: List<Pair<String, Any?>>, excludeNullValues: Boolean) = components
        .mapNotNull {
            if (excludeNullValues && it.second == null) {
                null
            }
            else {
                val encodedKey = URLEncoder.encode(it.first, "UTF-8")
                if (it.second == null) {
                    encodedKey
                } else {
                    encodedKey + '=' + URLEncoder.encode("${it.second}", "UTF-8")
                }
            }
        }
        .joinToString("&") { it }

    data class Options(
        val excludeNullQueryValues: Boolean = true,
        val excludeNullFragmentValues: Boolean = false
    )
}
