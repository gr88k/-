object SynchronizedCounter {
    fun run(): Int {
        var counter = 0
        val lock = Any()

        val threads = List(10) {
            Thread {
                repeat(1000) {
                    synchronized(lock) {
                        counter++
                    }
                    Thread.sleep(1)
                }
            }.apply { start() }
        }
        threads.forEach { it.join() }
        return counter
    }
}


val syncResults = mutableListOf<Int>()
repeat(5) { i ->
    val result = SynchronizedCounter.run()
    syncResults.add(result)
    println("Тест ${i+1}: $result (потеряно ${10000 - result})")
}