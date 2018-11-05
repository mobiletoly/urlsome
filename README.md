# URLSome
URLSome - URL builder library for Kotlin

# How to use

To build this URL:
`http://127.0.0.1:8081/api/v1/use%23rs/Toly+Pochkin/report?sort=firstName&country=US#main`

you can use this code:
```kotlin
val userId = "Toly Pochkin"
val baseUrl = Urlsome("http://127.0.0.1:8081/api/v1")
val url = (baseUrl / "users" / userId / "report") [
    "sort" to "firstName",
    "country" to "US"
] `#` "main"
```

`Urlsome()` accepts base URL that not going to be URL encoded. Then '/' is used to append
and path components (this components will be URL encoded, e.g. if you pass "users/report" as
a single string - it will be encoded as "users%2Freport"). After you done passing path
components - use square brackets to pass pairs of key/values for query parameters. If by some
reasons you need to add fragment component - use `#` infix function. 

Another way to get the same URL is:

```kotlin
val baseUrl = Urlsome("http://127.0.0.1:8081/api/v1")
val url = (
     (baseUrl / "users" / userId / "report")
    `?` ("sort" to "firstName")
    `?` ("country" to "US")
    `#` "main"
)
```

Or even more fancy:
```kotlin
val url = ((baseUrl / "users" / userId / "report")
    `?` ("sort" to "firstName") `&` ("country" to "US") `#` "main")
```
