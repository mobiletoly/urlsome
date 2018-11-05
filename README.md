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
If you don't want to encode path component then use `*` operator instead of '/'. You can mix both
operators if needed, e.g. `("api" / "user" * "/hello")` will result in `"api/user//hello"`. 

After you done passing path components - use square brackets to pass pairs of key/values for
query parameters. If by some reasons you need to add fragment component - use `#` infix function. 
Last step is to call `url.toString()` to build URL and converts it to a string.

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

Take a look at [unit tests](src/test/kotlin/UrlsomeTest.kt) for more examples.
