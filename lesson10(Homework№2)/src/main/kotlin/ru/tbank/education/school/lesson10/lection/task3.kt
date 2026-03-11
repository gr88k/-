
object SynchronizedCounter{
    var counter = 0
    val lock = Object()

    fun run(): Int {
        val threads = List(10) { threadId ->
            Thread {
                repeat(1000) {
                    synchronized(lock) {
                        counter++
                    }
                }
                println("Поток $threadId завершил работу")
            }
        }

        threads.forEach { it.start() }
        threads.forEach { it.join() }

        return counter
    }
}

fun main() {
    val finalValue = SynchronizedCounter.run()
    println("counter: $finalValue")
    println(10000 - finalValue)
}