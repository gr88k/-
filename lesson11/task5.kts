import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

object ExecutorServiceExample {
    fun run(): List<String> {
        val results = mutableListOf<String>()
        val executor = Executors.newFixedThreadPool(4)

        for (i in 1..20) {
            executor.submit {
                val threadName = Thread.currentThread().name
                val output = "Задача $i выполнена в потоке $threadName"
                results.add(output)
                Thread.sleep(200)
            }
        }

        executor.shutdown()
        executor.awaitTermination(10, TimeUnit.SECONDS)

        return results.sorted()
    }
}


val results = ExecutorServiceExample.run()
results.forEach { println(it) }

println("\nВсего задач: ${results.size}")