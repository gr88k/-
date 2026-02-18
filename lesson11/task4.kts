object Deadlock {
    private val lock1 = Object()
    private val lock2 = Object()

    fun runDeadlock() {
        val t1 = Thread {
            synchronized(lock1) {
                println("Поток 1: захватил lock1, ждет lock2")
                Thread.sleep(100)
                synchronized(lock2) { println("Поток 1: завершен") }
            }
        }

        val t2 = Thread {
            synchronized(lock2) {
                println("Поток 2: захватил lock2, ждет lock1")
                Thread.sleep(100)
                synchronized(lock1) { println("Поток 2: завершен") }
            }
        }

        t1.start()
        t2.start()

        t1.join(500)
        t2.join(500)

        println("\nСтатус: Поток1=${t1.state}, Поток2=${t2.state}")
        println("${if (t1.state == Thread.State.BLOCKED) "DEADLOCK" else ""}")
    }

    fun runFixed(): Boolean {
        val t1 = Thread {
            synchronized(lock1) {
                println("Поток 1: захватил lock1")
                Thread.sleep(100)
                synchronized(lock2) { println("Поток 1: завершен") }
            }
        }

        val t2 = Thread {
            synchronized(lock1) { // Одинаковый порядок!
                println("Поток 2: захватил lock1")
                Thread.sleep(100)
                synchronized(lock2) { println("Поток 2: завершен") }
            }
        }

        t1.start()
        t2.start()

        t1.join()
        t2.join()

        println("\nВсе потоки завершены")
        return true
    }
}

Deadlock.runDeadlock()
Thread.sleep(1000)
Deadlock.runFixed()