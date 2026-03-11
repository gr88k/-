object CreateThreads {
    fun run(): List<Thread> {
        val threads = listOf("Thread-A", "Thread-B", "Thread-C").map { name ->
            object : Thread(name) {
                override fun run() {
                    repeat(5) { counter ->
                        println("$name: вывод ${counter + 1}")
                        Thread.sleep(500)
                    }
                }
            }.apply { start() }
        }

        return threads
    }
}

println("Запуск программы...")

val threads = CreateThreads.run()

println("Потоки запущены, ожидаем их завершения...")

threads.forEachIndexed { index, thread ->
    thread.join()
    println("Поток ${thread.name} завершился") }

println("Все потоки завершили работу. Программа завершена.")