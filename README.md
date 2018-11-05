# URLSome
URLSome - URL builder library for Kotlin

# How to use

To build this URL:

`http://127.0.0.1:8081/api/v1/users/Toly+Pochkin/report?sort=firstName&country=US#main`

use this code:
```kotlin
val reportUser = "Toly Pochkin"
val baseUrl = Urlsome("http://127.0.0.1:8081/api/v1")
val url = (baseUrl / "users" / reportUser / "report") [
    "sort" to "firstName",
    "country" to "US"
] `#` "main"
println(url.toString())
```

`Urlsome()` accepts base URL that not going to be URL encoded. Then `/` is used to append
and path components. This path components will be URL encoded,
e.g. if you perform `("api" / "user" / "/hello")` then resulting URL will be `"api/user/%2Fhello"`.
If you don't want to encode path component then use `*` operator instead of `/`. You can mix both
operators if needed, e.g. `("api" / "user" * "/hello")` will result in `"api/user//hello"`. 

After you done passing path components - use square brackets to pass pairs of key/values for
query parameters. If by some reasons you need to add fragment component - use `#` infix function. 
Last step is to call `url.toString()` to build URL and convert it to a string.

Another way to get the same URL is:

```kotlin
val url = (
     (baseUrl / "users" / reportUser / "report")
    `?` ("sort" to "firstName")
    `?` ("country" to "US")
    `#` "main"
)
```

Or even more fancy:
```kotlin
val url = ((baseUrl / "users" / reportUser / "report")
    `?` ("sort" to "firstName") `&` ("country" to "US") `#` "main")
```

Actually there is no difference between `?` and `&`, both of them can be used to append query
parameters, but `&` a little more readable when passing multiple parameters, because it looks
like a typical query parameters separator.

Take a look at [unit tests](src/test/kotlin/UrlsomeTest.kt) for more examples.

# Usage with other libraries

We don't really need integration with any of the existing HTTP libraries, by the end of the
day `Urlsome` class always get transformed to `String`. But feel free to simplify your use
of your favorite HTTP library by using extension functions to support Urlsome format
E.g. let's take a look at popular library called [Fuel](https://github.com/kittinunf/Fuel).
You typical call with Fuel for Urlsome URL will look like this:

```
private val serviceUrl = Urlsome("https://......")
// ...
suspend fun getSomething(): Something {
    return Fuel.get((serviceUrl / "admin" / "users" ["payGrade" to 7]).toString())
        .awaitObject(SomeDeserializer)
}
```

This is boring, since you have to add up that `.toString()` call all the time. Just create
a few extension functions instead, e.g.

```kotlin
fun Fuel.Companion.get(url: Urlsome) = get(url.toString())
fun Fuel.Companion.post(url: Urlsome) = post(url.toString())
// ...
```

much better now:

```
suspend fun getSomething(): Something {
    return Fuel.get(serviceUrl / "admin" / "users" ["payGrade" to 7])
        .awaitObject(SomeDeserializer)
}
```
