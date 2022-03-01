package responces

import kotlinx.html.*
import kotlinx.html.stream.createHTML
import model.ModelMaster
import model.fromRubles

fun indexPage(products: List<ModelMaster.Product>, values: ModelMaster.Values): String =
    createHTML().html {
        body {
            table {
                tr {
                    th {
                        text("Name")
                    }
                    th {
                        text("Price")
                    }
                    th {
                        text("From")
                    }
                }
                products.forEach {
                    tr {
                        td {
                            text(it.name)
                        }
                        td {
                            text(values.fromRubles(it.rubles))
                        }
                        th {
                            text(it.seller)
                        }
                    }
                }
            }
        }
    }

fun registerPage(): String =
    createHTML().html {
        body {
            form(
                action = "/register",
                method = FormMethod.post
            ) {
                p {
                    +"Login"
                    textInput(name = "login")
                }
                p {
                    +"Password"
                    textInput(name = "password")
                }
                select {
                    name = "values"
                    for (v in ModelMaster.Values.values()) {
                        option {
                            value = v.name
                            text(v.name)
                        }
                    }
                }
                submitInput(classes = "pure-button pure-button-primary") {
                    value = "Register"
                }
            }
        }
    }

fun loginPage(): String =
    createHTML().html {
        body {
            form(
                action = "/login",
                method = FormMethod.post
            ) {
                p {
                    +"Login"
                    textInput(name = "login")
                }
                p {
                    +"Password"
                    textInput(name = "password")
                }
                submitInput(classes = "pure-button pure-button-primary") {
                    value = "Login"
                }
            }
        }
    }

fun addProductPage(): String =
    createHTML().html {
        body {
            form(
                action = "/addProduct",
                method = FormMethod.post
            ) {
                p {
                    +"Name"
                    textInput(name = "name")
                }
                p {
                    +"Rubles"
                    textInput(name = "rubles")
                }
                submitInput(classes = "pure-button pure-button-primary") {
                    value = "Add"
                }
            }
        }
    }