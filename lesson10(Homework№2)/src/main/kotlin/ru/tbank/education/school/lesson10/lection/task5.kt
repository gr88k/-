import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

object ExecutorServiceExample {
    fun run(): List<String> {
        val executor = Executors.newFixedThreadPool(4)
        val results = mutableListOf<String>()
        val lock = Object()

        for (i in 1..20) {
            executor.submit {
                val message = "Задача $i выполняется в потоке: ${Thread.currentThread().name}"
                println(message)

                synchronized(lock) {
                    results.add(message)
                }

                Thread.sleep(200)
            }
        }

        executor.shutdown()

        val terminated = executor.awaitTermination(10, TimeUnit.SECONDS)
        if (!terminated) {
            println("Не все задачи завершились в течение 10 секунд")
            executor.shutdownNow()
        }

        return results
    }
}