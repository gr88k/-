import java.net.HttpURLConnection
import java.net.URL
import java.security.cert.X509Certificate
import javax.net.ssl.HttpsURLConnection
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager

// ===========================================
// Задача 1. HTTP-запросы через HttpURLConnection
// ===========================================
// Цель: научиться отправлять GET и POST запросы, читать ответ и статус-код.
// API: https://jsonplaceholder.typicode.com
//
// TODO 1: Отправить GET /posts/1, вывести статус-код и тело ответа
// TODO 2: Отправить POST /posts с JSON-телом, вывести статус-код и тело
// TODO 3: Отправить GET /posts/9999, обработать ошибку (код != 2xx)
//
// Подсказки:
//   val connection = URL("...").openConnection() as HttpURLConnection
//   connection.requestMethod = "GET"             — задать метод
//   connection.doOutput = true                   — разрешить отправку тела
//   connection.setRequestProperty("Content-Type", "application/json") — заголовок
//   connection.outputStream.write(json.toByteArray())                 — записать тело
//   connection.responseCode                      — получить статус-код
//   connection.inputStream.bufferedReader().readText()  — прочитать тело ответа
//   connection.errorStream                       — поток ошибок (при коде 4xx/5xx)
//   connection.disconnect()                      — закрыть соединение

val BASE_URL = "https://jsonplaceholder.typicode.com/posts"

fun sendRequest(urlStr: String, method: String, body: String? = null): Pair<Int, String> {
    // TODO 1: открыть соединение, выставить метод,
    //   если body != null — записать тело и заголовок Content-Type,
    //   прочитать ответ (inputStream при 2xx, errorStream иначе),
    //   вернуть пару (код, тело)
    TODO("Реализуй sendRequest")
    val connection = URL(urlStr).openConnection() as HttpURLConnection
    connection.requestMethod = method
    if (body != null) {
        connection.doOutput = true
        connection.setRequestProperty("Content-Type", "application/json")
        connection.outputStream.write(body.toByteArray())
    }
    val code = connection.responseCode
    val response = (if (code in 200..299) connection.inputStream else connection.errorStream)
        ?.bufferedReader()?.readText() ?: ""
    connection.disconnect()
    return code to response
}

/** GET /posts — получить все посты */
fun getPosts(urlStr: String): String {
    // TODO 2a
    TODO("Реализуй getPosts")
    val connection = URL(urlStr).openConnection() as HttpURLConnection

    val getBody = connection.inputStream.bufferedReader().readText()
    return getBody.toString()
}

/** GET /posts/{id} — получить пост по ID */
fun getPost(id: Int, urlStr: String): String {
    // TODO 2b
    TODO("Реализуй getPost")
    val url = urlStr + "/" + id.toString()
    val connection = URL(url).openConnection() as HttpURLConnection

    val getBody = connection.inputStream.bufferedReader().readText()
    return getBody.toString()
}

/** POST /posts — создать новый пост. Тело: {"title":"...", "body":"...", "userId":1} */
fun createPost(json: String): String {
    // TODO 2c
    TODO("Реализуй createPost")
}

/** PUT /posts/{id} — полностью обновить пост */
fun updatePost(id: Int, json: String): String {
    // TODO 2d
    TODO("Реализуй updatePost")
}

/** DELETE /posts/{id} — удалить пост, вернуть статус-код */
fun deletePost(id: Int): Int {
    // TODO 2e
    TODO("Реализуй deletePost")
}

fun main() {
    disableSslVerification()

    // TODO 3: вызвать каждую функцию и вывести результат
    println("=== GET ALL ===")

    println("\n=== GET ONE ===")

    println("\n=== CREATE ===")

    println("\n=== UPDATE ===")
}