
class VisibilityProblem {

    @Volatile
    private var running = true

    fun startWriter(): Thread {
        return Thread {
            repeat(100) {
                Thread.sleep(10)
                Thread.yield()
            }

            running = false
            println("Writer: установил running = false")
        }
    }

    fun startReader(): Thread {
        return Thread {
            println("Reader: начал работу (ждет running = false)")

            var iterations = 0
            while (running) {
                iterations++
                if (iterations % 100_000 == 0) {
                    println("Reader: все еще работает...")
                }
            }

            println("Reader: завершил работу (увидел running = false после $iterations итераций)")
        }
    }
}