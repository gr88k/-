object Deadlock {
    val lock1 = Object()
    val lock2 = Object()

    fun runDeadlock() {
        val thread1 = Thread {
            synchronized(lock1) {
                Thread.sleep(100)
                synchronized(lock2) {
                    println("Thread 1")
                }
            }
        }.apply { name = "Thread-1" }

        val thread2 = Thread {
            synchronized(lock2) {
                Thread.sleep(100)
                synchronized(lock1) {
                    println("Thread 2")
                }
            }
        }.apply { name = "Thread-2" }

        thread1.start()
        thread2.start()
        thread1.join()
        thread2.join()
    }

    fun runFixed(): Boolean {
        val thread1 = Thread {
            synchronized(lock1) {
                println("Поток 1: заблокировал ресурс A")
                Thread.sleep(100)

                synchronized(lock2) {
                    println("Поток 1: заблокировал ресурс B")
                }
            }
            println("Поток 1: завершил работу")
        }.apply { name = "Thread-1" }

        val thread2 = Thread {
            synchronized(lock1) {
                println("Поток 2: заблокировал ресурс A")
                Thread.sleep(100)

                synchronized(lock2) {
                    println("Поток 2: заблокировал ресурс B")
                }
            }
            println("Поток 2: завершил работу")
        }.apply { name = "Thread-2" }

        thread1.start()
        thread2.start()

        thread1.join()
        thread2.join()
        return true
    }
}

fun main() {
    //Deadlock.runDeadlock()
    Deadlock.runFixed()
}