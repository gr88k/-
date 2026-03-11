
object CreateThreads {
    fun run(): List<Thread> {
        return listOf("Thread-A", "Thread-B", "Thread-C").map { name ->
            Thread {
                repeat(5) {
                    println(name)
                    Thread.sleep(500)
                }
            }.apply { this.name = name }
        }
    }
}

fun main() {
    val threads = CreateThreads.run()
    threads.forEach { it.start() }

    threads.forEach { it.join() }

    println("Все потоки завершили работу")
}