import java.io.File
import java.net.URL
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import kotlin.system.measureTimeMillis

object ImageDownloader {
    fun run(urls: List<String>, outputDir: String): Pair<Int, Int> {
        File(outputDir).mkdirs()

        val executor = Executors.newFixedThreadPool(4)
        var success = 0
        var errors = 0
        val lock = Any()

        urls.forEachIndexed { index, url ->
            executor.submit {
                try {
                    val file = File(outputDir, "image_${index + 1}.jpg")
                    URL(url).openStream().use { it.copyTo(file.outputStream()) }

                    synchronized(lock) {
                        success++
                        println("Загружено $success/${urls.size}")
                    }
                } catch (e: Exception) {
                    synchronized(lock) {
                        errors++
                        println("Ошибка ${index + 1}: ${e.message}")
                    }
                }
            }
        }

        executor.shutdown()
        executor.awaitTermination(1, TimeUnit.MINUTES)

        return Pair(success, errors)
    }
}

val urls = List(10) { "https://httpbin.org/image/jpeg?${it + 1}" }
val outputDir = "downloads"

println("Скачиваю 10 изображений...\n")

val time = measureTimeMillis {
    val (success, errors) = ImageDownloader.run(urls, outputDir)
    println("\nУспешно: $success")
    println("Ошибок: $errors")
}

println("⏱ Время: ${time}ms")