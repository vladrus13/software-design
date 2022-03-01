package model

import org.bson.Document

fun ModelMaster.Values.fromRubles(price: Double): Double {
    return when (this) {
        ModelMaster.Values.USD -> price * 99
        ModelMaster.Values.EUR -> price * 104
        ModelMaster.Values.RUB -> price
    }
}

class ModelMaster {

    enum class Values {
        USD, EUR, RUB
    }

    data class User(val login: String, val password: String, val values: Values) {

        constructor(document: Document) : this(
            document.getString("login"),
            document.getString("password"),
            Values.valueOf(document.getString("values"))
        )

        fun toTable(): Document = Document("login", login).append("password", password).append("values", values.name)


    }

    data class Product(val seller: String, val name: String, val description: String, val rubles: Double) {

        constructor(document: Document) : this(
            document.getString("seller"),
            document.getString("name"),
            document.getString("description"),
            document.getDouble("rubles")
        )

        fun toTable(): Document =
            Document("seller", seller)
                .append("name", name)
                .append("description", description)
                .append("rubles", rubles)
    }
}