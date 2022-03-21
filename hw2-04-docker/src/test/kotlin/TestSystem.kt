import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.server.testing.*
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.testcontainers.containers.FixedHostPortGenericContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import ru.vladrus13.common.utils.result
import ru.vladrus13.common.utils.successful
import ru.vladrus13.users.rounting.module
import kotlin.test.assertEquals
import kotlin.test.fail

@Testcontainers
class TestSystem {

    class SystemContainer :
        FixedHostPortGenericContainer<SystemContainer>("system:latest")

    @Container
    val container = SystemContainer()
        .withFixedExposedPort(8080, 8080)

    private fun assertResponse(response: TestApplicationResponse, code: HttpStatusCode, content: String?) {
        assertEquals(code, response.status())
        if (content != null) {
            assertEquals(content, response.content)
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

    private val client = HttpClient(CIO) {
    }

    private suspend inline fun <reified T> sendWithRequest(
        url: String,
        content: T
    ) {
        try {
            assertEquals(content, client.get("http://localhost:8080$url"))
        } catch (e: Exception) {
            fail("Exception found", e)
        }
    }

    @BeforeEach
    fun setUp() {
        container.start()
    }

    @AfterEach
    fun setDown() {
        container.stop()
    }

    @Test
    fun empty() {
    }

    @Test
    fun `register user`() {
        withTestApplication({ module() }) {
            requestWithAssert("/users/register", HttpStatusCode.OK, result("0"))
        }
    }

    @Test
    fun `register and buy`() {
        Thread.sleep(500)
        withTestApplication({ module() }) {
            runBlocking {
                sendWithRequest("/promotion/register?name=heh&amount=5&price=10", result("0"))
            }
            requestWithAssert("/users/register", HttpStatusCode.OK, result("0"))
            requestWithAssert("/users/add?id=0&amount=100", HttpStatusCode.OK, successful())
            requestWithAssert("/users/process?userId=0&promotionId=0&amount=1", HttpStatusCode.OK, successful())
            requestWithAssert("/users/process?userId=0&promotionId=0&amount=4", HttpStatusCode.OK, successful())
            requestWithAssert(
                "/users/process?userId=0&promotionId=0&amount=1",
                HttpStatusCode.OK,
                ru.vladrus13.common.utils.error()
            )
        }
    }

    @Test
    fun `perfect profit locking`() {
        Thread.sleep(500)
        withTestApplication({ module() }) {
            runBlocking {
                sendWithRequest("/promotion/register?name=AnyRussianPromotion&amount=5000&price=1000", result("0"))
            }
            requestWithAssert("/users/register", HttpStatusCode.OK, result("0"))
            requestWithAssert("/users/add?id=0&amount=1000000", HttpStatusCode.OK, successful())
            requestWithAssert("/users/process?userId=0&promotionId=0&amount=100", HttpStatusCode.OK, successful())
            requestWithAssert("/user/money?userId=0", HttpStatusCode.OK, result((100 * 1000.0).toString()))
            runBlocking {
                sendWithRequest("/promotion/price?id=0&price=1", successful())
            }
            // oh :(
            requestWithAssert("/user/money?userId=0", HttpStatusCode.OK, result((100 * 1.0).toString()))
        }
    }
}