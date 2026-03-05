import java.net.HttpURLConnection
import java.net.URL
import java.util.Base64

// ===========================================
// Задача 3. JWT — авторизация
// ===========================================
// Цель: понять структуру JWT, собрать и декодировать токен, отправить запрос с Bearer-авторизацией.
// API: https://httpbin.org/bearer (возвращает 200 если есть Bearer, 401 если нет)
//
// TODO 1: Собрать JWT из трёх частей (header, payload, signature) в Base64URL
// TODO 2: Декодировать JWT обратно — вывести header и payload как JSON
// TODO 3: Отправить GET https://httpbin.org/bearer с заголовком Authorization: Bearer <token>
// TODO 4: Отправить тот же запрос БЕЗ токена — убедиться, что вернулся 401
// TODO 5: Подменить payload (role: student → admin), объяснить почему сервер отвергнет
//
// Подсказки:
//   Base64.getUrlEncoder().withoutPadding().encodeToString(bytes) — кодирование
//   Base64.getUrlDecoder().decode(string)                        — декодирование
//   JWT = base64(header) + "." + base64(payload) + "." + base64(signature)
//
// Вопросы после выполнения:
//   - Из каких 3 частей состоит JWT?
//   - Можно ли подменить payload и использовать токен? Почему нет?
//   - Что такое access token и refresh token?
//
fun main() {
    val encoder = Base64.getUrlEncoder().withoutPadding()

    // TODO 1: Собрать JWT
    println("=== Сборка JWT ===")
    val header = """{"alg":"HS256","typ":"JWT"}"""
    val payload = """{"sub":"1","name":"Ivan Petrov","role":"student","iat":1234567890}"""
    val fakeSignature = "dummysignature"
    val encodedHeader = encoder.encodeToString(header.toByteArray())
    val encodedPayload = encoder.encodeToString(payload.toByteArray())
    val encodedSignature = encoder.encodeToString(fakeSignature.toByteArray())

    val token = "$encodedHeader.$encodedPayload.$encodedSignature"
    println("Собранный JWT токен:")
    println(token)

    // TODO 2: Декодировать JWT
    println("\n=== Декодирование JWT ===")
    val decoder = Base64.getUrlDecoder()

    val parts = token.split(".")
    val decodedHeader = String(decoder.decode(parts[0]))
    val decodedPayload = String(decoder.decode(parts[1]))
    val decodedSignature = String(decoder.decode(parts[2]))

    println("Header: $decodedHeader")
    println("Payload: $decodedPayload")
    println("Signature: $decodedSignature")

    // TODO 3: GET /bearer с токеном
    println("\n=== GET /bearer (с токеном) ===")
    val responseWithToken = sendGetRequest("https://httpbin.org/bearer", token)
    println("Код ответа: ${responseWithToken.first}")
    println("Тело ответа: ${responseWithToken.second}")

    // TODO 4: GET /bearer без токена
    println("\n=== GET /bearer (без токена) ===")
    val responseWithoutToken = sendGetRequest("https://httpbin.org/bearer", null)
    println("Код ответа: ${responseWithoutToken.first}")
    println("Тело ответа: ${responseWithoutToken.second}")

    // TODO 5: Подмена payload
    println("\n=== Подмена payload ===")
    val tamperedPayload = """{"sub":"1","name":"Ivan Petrov","role":"admin","iat":1234567890}"""
    val encodedTamperedPayload = encoder.encodeToString(tamperedPayload.toByteArray())
    val tamperedToken = "$encodedHeader.$encodedTamperedPayload.$encodedSignature"

    println("Токен с подменённым payload (student → admin):")
    println(tamperedToken)

    println("\nОтправка запроса с поддельным токеном:")
    val responseWithTampered = sendGetRequest("https://httpbin.org/bearer", tamperedToken)
    println("Код ответа: ${responseWithTampered.first}")
    println("Тело ответа: ${responseWithTampered.second}")
}

fun sendGetRequest(urlString: String, token: String?): Pair<Int, String> {
    return try {
        val url = URL(urlString)
        val connection = url.openConnection() as HttpURLConnection
        connection.requestMethod = "GET"

        if (token != null) {
            connection.setRequestProperty("Authorization", "Bearer $token")
        }

        val responseCode = connection.responseCode
        val response = if (responseCode == 200) {
            connection.inputStream.bufferedReader().readText()
        } else {
            connection.errorStream?.bufferedReader()?.readText() ?: "Ошибка запроса"
        }

        connection.disconnect()
        Pair(responseCode, response)
    } catch (e: Exception) {
        println("Ошибка: ${e.message}")
        Pair(500, "Ошибка подключения: ${e.message}")
    }
}