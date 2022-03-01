package responces

import db.findUser
import db.getProducts
import db.save
import io.netty.buffer.ByteBuf
import io.netty.handler.codec.http.HttpResponseStatus
import io.netty.handler.codec.http.cookie.DefaultCookie
import io.reactivex.netty.protocol.http.server.HttpServerRequest
import io.reactivex.netty.protocol.http.server.HttpServerResponse
import io.reactivex.netty.protocol.http.server.ResponseContentWriter
import model.ModelMaster
import rx.Observable
import rx.Subscriber
import java.nio.charset.Charset

fun HttpServerRequest<ByteBuf>.getArguments() =
    this.content.map {
        val data = it.readCharSequence(contentLength.toInt(), Charset.defaultCharset())
        data.split("&").associate {
            val sp = it.split("=")
            sp[0] to sp[1]
        }
    }

object GetResponceMaster {
    fun request(
        request: HttpServerRequest<ByteBuf>,
        response: HttpServerResponse<ByteBuf>
    ): ResponseContentWriter<ByteBuf> {
        return when (request.decodedPath) {
            "/" -> response.writeString(
                getProducts().toList().map {
                    val oldValues = request.cookies["values"]
                    val values = if (oldValues == null || oldValues.size == 0) {
                        ModelMaster.Values.RUB
                    } else {
                        ModelMaster.Values.valueOf(oldValues.first().value())
                    }
                    indexPage(it, values)
                }
            )
            "/register" -> response.writeString(
                Observable.fromCallable {
                    registerPage()
                }
            )
            "/login" -> response.writeString(
                Observable.fromCallable {
                    loginPage()
                }
            )
            "/addProduct" -> response.writeString(
                Observable.fromCallable {
                    addProductPage()
                }
            )
            else -> throw IllegalStateException()
        }
    }
}

fun writeStringFromCallableUnsafeSubscribe(
    response: HttpServerResponse<ByteBuf>,
    message: String,
    subscriber: Subscriber<in Void>
) =
    response.apply {
        status = HttpResponseStatus.OK
        writeString(Observable.just(message)).unsafeSubscribe(subscriber)
    }

object PostResponceMaster {
    fun request(
        request: HttpServerRequest<ByteBuf>,
        response: HttpServerResponse<ByteBuf>
    ): Observable<Void> {
        return when (request.decodedPath) {
            "/register" -> object : Observable<Void>({ subscribe ->
                subscribe.add(
                    request.getArguments().subscribe {
                        val login = it["login"]!!
                        val password = it["password"]!!
                        val values = it["values"]!!
                        subscribe.add(
                            findUser(login).subscribe({
                                writeStringFromCallableUnsafeSubscribe(response, "Already exists", subscribe)
                            }) {
                                ModelMaster.User(login, password, ModelMaster.Values.valueOf(values)).save().subscribe()
                                response.addCookie(DefaultCookie("login", login))
                                response.addCookie(DefaultCookie("values", values))
                                writeStringFromCallableUnsafeSubscribe(response, "OK!", subscribe)
                            }
                        )
                    }
                )
            }
            ) {}
            "/login" -> object : Observable<Void>({ subscribe ->
                subscribe.add(
                    request.getArguments().subscribe {
                        val login = it["login"]!!
                        val password = it["password"]!!
                        subscribe.add(
                            findUser(login).subscribe({
                                if (it.password == password) {
                                    response.addCookie(DefaultCookie("login", login))
                                    response.addCookie(DefaultCookie("values", it.values.name))
                                    writeStringFromCallableUnsafeSubscribe(response, "OK!", subscribe)
                                } else {
                                    writeStringFromCallableUnsafeSubscribe(
                                        response,
                                        "Wrong login or password",
                                        subscribe
                                    )
                                }
                            }) {
                                writeStringFromCallableUnsafeSubscribe(response, "Login not found", subscribe)
                            }
                        )
                    }
                )
            }) {}
            "/addProduct" ->
                object : Observable<Void>({ subscribe ->
                    subscribe.add(
                        request.getArguments().subscribe {
                            val login = request.cookies["login"]
                            if (login == null || login.size == 0) {
                                writeStringFromCallableUnsafeSubscribe(response, "Please, login", subscribe)
                            } else {
                                val name = it["name"]!!
                                val rubles = it["rubles"]!!.toDouble()
                                ModelMaster.Product(login.first().value(), name, "", rubles).save().subscribe()
                                writeStringFromCallableUnsafeSubscribe(response, "OK!", subscribe)
                            }
                        }
                    )
                }) {}
            else -> throw IllegalStateException()
        }
    }
}