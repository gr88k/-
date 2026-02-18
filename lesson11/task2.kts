object RaceCondition {
    fun run(): Int {
        var counter = 0
        val threads = List(10) {
            Thread {
                repeat(1000) {
                    counter++
                    Thread.sleep(1)}
            }.apply { start() }
        }
        threads.forEach { it.join() }
        return counter
    }
}

val results = mutableListOf<Int>()
repeat(5) { i ->
    val result = RaceCondition.run()
    results.add(result)
    println("Тест ${i+1}: $result (потеряно ${10000 - result})")
}

println("\nРезультаты: ${results.joinToString()}")