
@file:DependsOn("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")

import kotlinx.coroutines.*

object CoroutineLaunch {
    fun run(): List<String> = runBlocking {
        val results = mutableListOf<String>()

        val jobs = List(3) { index ->
            val name = "Coroutine-${'A' + index}"
            launch {
                repeat(5) { i ->
                    delay(500)
                    results.add("$name: вывод ${i + 1}")
                }
            }
        }

        jobs.joinAll()
        results
    }
}

CoroutineLaunch.run().forEach { println(it) }