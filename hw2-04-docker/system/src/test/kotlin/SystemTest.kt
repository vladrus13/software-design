import io.ktor.http.*
import io.ktor.server.testing.*
import org.junit.jupiter.api.Test
import ru.vladrus13.common.utils.result
import ru.vladrus13.common.utils.successful
import ru.vladrus13.system.routing.module
import kotlin.test.assertEquals

class SystemTest {

    private fun assertResponse(response: TestApplicationResponse, code: HttpStatusCode, content: String?) {
        assertEquals(response.status(), code)
        if (content != null) {
            assertEquals(response.content, content)
        }
    }

    private fun TestApplicationEngine.requestWithAssert(
        url: String,
        code: HttpStatusCode,
        content: String? = null
    ) {
        this.handleRequest(HttpMethod.Get, url).apply {
            assertResponse(response, code, content)
        }
    }

    @Test
    fun empty() {
        withTestApplication({ module() }) {

        }
    }

    @Test
    fun `get promotions empty`() {
        withTestApplication({ module() }) {
            requestWithAssert("/", HttpStatusCode.OK, "[ ]")
        }
    }

    @Test
    fun `add promotion`() {
        withTestApplication({ module() }) {
            requestWithAssert("promotion/register?name=heh&amount=100&price=100", HttpStatusCode.OK, result("0"))
            requestWithAssert("promotion/amount?id=0&amount=10", HttpStatusCode.OK, successful())
            requestWithAssert("promotion/price?id=0&price=200", HttpStatusCode.OK, successful())
            requestWithAssert(
                "/", HttpStatusCode.OK, "[ \"{\\n" +
                        "  \\\"name\\\" : \\\"heh\\\",\\n" +
                        "  \\\"amount\\\" : 110,\\n" +
                        "  \\\"price\\\" : 200.0\\n" +
                        "}\" ]"
            )
        }
    }
}