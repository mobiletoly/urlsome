import com.github.mobiletoly.urlsome.Urlsome
import kotlin.test.Test
import kotlin.test.assertEquals

class UrlsomeTest {

    @Test fun `base URL`() {
        val url = Urlsome("http://localhost:8081")
        assertEquals("http://localhost:8081", url.toString())
    }

    @Test fun `path components`() {
        val url = Urlsome("http://localhost:8081") / "root" / "user"
        assertEquals("http://localhost:8081/root/user", url.toString())
    }

    @Test fun `base URL with trailing slash`() {
        val url = Urlsome("http://localhost:8081/") / "root" / "user"
        assertEquals("http://localhost:8081/root/user", url.toString())
    }

    @Test fun `path encoding`() {
        val url = Urlsome("http://localhost:8081/") / "root" / "some/path" / "user"
        assertEquals("http://localhost:8081/root/some%2Fpath/user", url.toString())
    }

    @Test fun `path without encoding`() {
        val url = Urlsome("http://localhost:8081/") / "root" * "some/path" / "user"
        assertEquals("http://localhost:8081/root/some/path/user", url.toString())
    }

    @Test fun `query components`() {
        val url = (Urlsome("http://localhost:8081/") / "root") [
            "param1" to "value1",
            "param2" to "value2"
        ]
        assertEquals("http://localhost:8081/root?param1=value1&param2=value2", url.toString())
    }

    @Test fun `query with null value on default options`() {
        val url = (Urlsome("http://localhost:8081/") / "root") [
            "param1" to null,
            "param2" to "value2"
        ]
        assertEquals("http://localhost:8081/root?param2=value2", url.toString())
    }

    @Test fun `query with null value on excludeNullQueryValues=false option`() {
        val url = (Urlsome(
            baseUrl = "http://localhost:8081/",
            options = Urlsome.Options(excludeNullQueryValues = false)
        ) / "root") [
            "param1" to null,
            "param2" to "value2"
        ]
        assertEquals("http://localhost:8081/root?param1&param2=value2", url.toString())
    }

    @Test fun `query shorthand syntax`() {
        val url = ((Urlsome("http://localhost:8081/") / "root")
            `?` ("param1" to "value1")
            `?` ("param2" to "value2")
        )
        assertEquals("http://localhost:8081/root?param1=value1&param2=value2", url.toString())
    }

    @Test fun `fragment components`() {
        val url = ((Urlsome("http://localhost:8081/") / "root")
            `?` ("param1" to "value1")
            `?` ("param2" to "value2")
            `#` ("x" to 1)
            `#` ("y" to 2)
        )
        assertEquals("http://localhost:8081/root?param1=value1&param2=value2#x=1&y=2", url.toString())
    }

    @Test fun `fragment with null value on default option`() {
        val url = ((Urlsome("http://localhost:8081/") / "root")
            `?` ("param1" to "value1")
            `?` ("param2" to "value2")
            `#` ("main" to null)
        )
        assertEquals("http://localhost:8081/root?param1=value1&param2=value2#main", url.toString())
    }

    @Test fun `fragment with null value on excludeNullFragmentValues=true`() {
        val url = ((
            Urlsome(
                baseUrl = "http://localhost:8081/",
                options = Urlsome.Options(excludeNullFragmentValues = true)) / "root"
            )
            `?` ("param1" to "value1")
            `?` ("param2" to "value2")
            `#` ("main" to null)
        )
        assertEquals("http://localhost:8081/root?param1=value1&param2=value2", url.toString())
    }
}
