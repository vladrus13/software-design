import io.netty.handler.codec.http.HttpMethod
import io.reactivex.netty.protocol.http.server.HttpServer
import responces.GetResponceMaster
import responces.PostResponceMaster

fun main() {
    HttpServer.newServer(8080).start { request, response ->
        when (request.httpMethod) {
            HttpMethod.GET -> GetResponceMaster.request(request, response)
            HttpMethod.POST -> PostResponceMaster.request(request, response)
            else -> throw IllegalStateException()
        }
    }
}