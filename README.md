# URLSome
URLSome - URL builder library for Kotlin

# Add to gradle build

The easiest way to add this library to your gradle build is to use [jitpack](https://jitpack.io/)

```
repositories {
    mavenCentral()
    jcenter()
    maven { url 'https://jitpack.io' }
    // ...
}

dependencies {
    // ...
    implementation group: "com.github.mobiletoly", name: "urlsome", version: "0.4-SNAPSHOT"
}

```

# How to use

To build this URL:

`http://127.0.0.1:8081/api/v1/users/Toly+Pochkin/report?sort=firstName&country=US#main`

use this code:
```kotlin
val reportUser = "Toly Pochkin"
val baseUrl = Urlsome("http://127.0.0.1:8081/api/v1")
val url = (baseUrl/"users"/reportUser/"report") [
    "sort" to "firstName",
    "country" to "US"
] `#` "main"
println(url.toString())
```

`Urlsome()` accepts base URL that not going to be URL encoded. Then `/` is used to append
and path components. This path components will be URL encoded,
e.g. if you perform `("api"/"user"/"/hello")` then resulting URL will be `"api/user/%2Fhello"`.
If you don't want to encode path component then use `*` operator instead of `/`. You can mix both
operators if needed, e.g. `("api"/"user"*"/hello")` will result in `"api/user//hello"`. 

After you done passing path components - use square brackets to pass pairs of key/values for
query parameters. If by some reasons you need to add fragment component - use `#` infix function. 
Last step is to call `url.toString()` to build URL and convert it to a string.

Another way to get the same URL is:

```kotlin
val url = (
     (baseUrl/"users"/reportUser/"report")
    `?` ("sort" to "firstName")
    `?` ("country" to "US")
    `#` "main"
)
```

Or even more fancy:
```kotlin
val url = ((baseUrl/"users"/reportUser/"report")
    `?` ("sort" to "firstName") `&` ("country" to "US") `#` "main")
```

Actually there is no difference between `?` and `&`, both of them can be used to append query
parameters, but `&` a little more readable when passing multiple parameters, because it looks
like a typical query parameters separator.

Take a look at [unit tests](src/test/kotlin/UrlsomeTest.kt) for more examples.

# Usage with other libraries

We don't really need integration with any of the existing HTTP libraries, by the end of the
day `Urlsome` class always gets transformed to `String`. But feel free to simplify your use
of your favorite HTTP library by using extension functions to support `Urlsome` instances.
For example, let's take a look at popular library called [Fuel](https://github.com/kittinunf/Fuel).
Your typical call with Fuel by using `Urlsome` instance will look like this:

```kotlin
private val serviceUrl = Urlsome("https://......")
// ...
suspend fun getSomething(): Something {
    return Fuel.get((serviceUrl/"admin"/"users" ["payGrade" to 7]).toString())
        .awaitObject(SomeDeserializer)
}
```

This is boring since you have to append `.toString()` call all the time. So just create
a few extension functions instead, e.g.

```kotlin
fun Fuel.Companion.get(url: Urlsome) = get(url.toString())
fun Fuel.Companion.post(url: Urlsome) = post(url.toString())
// ...
```

much better now:

```kotlin
suspend fun getSomething(): Something {
    return Fuel.get(serviceUrl/"admin"/"users" ["payGrade" to 7])
        .awaitObject(SomeDeserializer)
}
```
