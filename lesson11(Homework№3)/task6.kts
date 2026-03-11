import java.util.concurrent.Executors
import java.util.concurrent.Callable
import java.math.BigInteger

object FutureFactorial {
    fun run(): Map<Int, BigInteger> {
        val executor = Executors.newFixedThreadPool(4)
        val futures = mutableListOf<Pair<Int, java.util.concurrent.Future<BigInteger>>>()

        for (n in 1..10) {
            val future = executor.submit(Callable<BigInteger> {
                var result = BigInteger.ONE
                for (i in 2..n) {
                    result = result.multiply(BigInteger.valueOf(i.toLong()))
                }
                result
            })
            futures.add(n to future)
        }

        val results = mutableMapOf<Int, BigInteger>()
        futures.forEach { (n, future) ->
            results[n] = future.get()
        }

        executor.shutdown()
        return results
    }
}

val results = FutureFactorial.run()
results.toSortedMap().forEach { (n, fact) ->
    println("$n! = $fact")
}