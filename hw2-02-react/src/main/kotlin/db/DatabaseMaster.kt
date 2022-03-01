package db

import com.mongodb.client.model.Filters.eq
import com.mongodb.rx.client.MongoClients
import com.mongodb.rx.client.Success
import model.ModelMaster
import rx.Observable

val database = MongoClients.create("mongodb://localhost:27017").getDatabase("hw2-02-react")

val users = database.getCollection("Users")

fun ModelMaster.User.save(): Observable<Success> =
    users.insertOne(this.toTable())

fun findUser(login: String): Observable<ModelMaster.User> =
    users.find(eq("login", login)).toObservable().first().map { ModelMaster.User(it) }

val products = database.getCollection("Products")

fun ModelMaster.Product.save(): Observable<Success> =
    products.insertOne(this.toTable())

fun getProducts(): Observable<ModelMaster.Product> =
    products.find().toObservable().map { ModelMaster.Product(it) }
