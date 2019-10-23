package sample.base

class Hello constructor() {
    var obj: String = "World"

    fun sayHello() {
        println("Hello$obj")
    }

    constructor(obj: String) : this() {
        this.obj = obj
    }

}

fun main(args: Array<String>) {
    val helloWorld = Hello();
    helloWorld.sayHello()

    val helloKotlin = Hello("Kotlin")
    helloKotlin.sayHello()
}