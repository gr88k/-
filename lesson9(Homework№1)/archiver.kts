import java.io.File
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream

fun createZip(sourceDir: String, outputZip: String, allowedExt: Set<String> = emptySet()) {
    val dir = File(sourceDir)
    if (!dir.isDirectory) {
        println("Неверный каталог: $sourceDir")
        return
    }

    ZipOutputStream(File(outputZip).outputStream()).use { zip ->
        dir.walkTopDown()
            .filter { it.isFile }
            .filter { allowedExt.isEmpty() || it.extension.lowercase() in allowedExt }
            .forEach { file ->
                val entryName = file.relativeTo(dir).invariantSeparatorsPath
                zip.putNextEntry(ZipEntry(entryName))
                file.inputStream().use { it.copyTo(zip) }
                zip.closeEntry()
                println("$entryName - ${file.length()} bytes")
            }
    }
    println("Архив сохранён как $outputZip")
}

print("Папка для архивации [.]: ")
val folder = readLine()?.takeIf { it.isNotBlank() } ?: "."

print("Имя архива [archive.zip]: ")
val archive = readLine()?.takeIf { it.isNotBlank() } ?: "archive.zip"

print("Расширения (через запятую, Enter - все файлы): ")
val extInput = readLine()?.trim()

val extensions = if (extInput.isNullOrBlank()) {
    emptySet<String>()
} else {
    extInput.split(",").map { it.trim().trimStart('.') }.toSet()
}

println("\nСоздаю архив...")
createZip(folder, archive, extensions)