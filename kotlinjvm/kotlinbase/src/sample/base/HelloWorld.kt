package sample.base

class Hello constructor() {
    var obj: String = "World"
    var sign: String = ""

    fun sayHello() {
        if (!"".equals(sign)) {
            println("Hello $obj by $sign")
        } else {
            println("Hello $obj")
        }
    }

    constructor(obj: String) : this() {
        this.obj = obj
    }

}

fun main(args: Array<String>) {
    val helloWorld = Hello();
    helloWorld.sayHello()
    helloWorld.sign = "yukiti"
    helloWorld.sayHello()

    val helloKotlin = Hello("Kotlin")
    helloKotlin.sayHello()
    helloKotlin.sign = "yukiti"
    helloKotlin.sayHello()
}