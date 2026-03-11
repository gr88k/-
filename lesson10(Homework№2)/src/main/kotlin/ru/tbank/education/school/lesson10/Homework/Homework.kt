import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import kotlin.math.abs

data class LogEntry(
    val dt: String,
    val id: Int,
    val status: String
)

data class ProcessingResult(
    val validLogs: List<LogEntry>,
    val brokenLogs: List<String>,
    val incompleteIds: List<Int>,
    val timeErrorIds: List<Int>,
    val deliveryTimes: Map<Int, Long>,
    val violations: List<Int>,
    val longestDelivery: Pair<Int, Long>?
)

fun normalize(line: String): LogEntry? {
    val trimmedLine = line.trim()

    val patternA = """(\d{4}-\d{2}-\d{2})\s+(\d{2}:\d{2})\s*\|\s*ID\s*:\s*(\d+)\s*\|\s*STATUS\s*:\s*(\w+)""".toRegex(RegexOption.IGNORE_CASE)
    val patternB = """TS\s*=\s*(\d{2}/\d{2}/\d{4})-(\d{2}:\d{2})\s*;\s*status\s*=\s*(\w+)\s*;\s*#(\d+)""".toRegex(RegexOption.IGNORE_CASE)
    val patternC = """\[(\d{2}\.\d{2}\.\d{4})\s+(\d{2}:\d{2})\]\s+(\w+)\s*\(id\s*:\s*(\d+)\)""".toRegex(RegexOption.IGNORE_CASE)

    return when {
        patternA.matches(trimmedLine) -> {
            val matchResult = patternA.find(trimmedLine)
            val (date, time, idStr, statusStr) = matchResult!!.destructured
            LogEntry("$date $time", idStr.toInt(), statusStr.lowercase())
        }
        patternB.matches(trimmedLine) -> {
            val matchResult = patternB.find(trimmedLine)
            val (date, time, statusStr, idStr) = matchResult!!.destructured
            val normalizedDate = convertDateFormat(date, "dd/MM/yyyy", "yyyy-MM-dd")
            LogEntry("$normalizedDate $time", idStr.toInt(), statusStr.lowercase())
        }
        patternC.matches(trimmedLine) -> {
            val matchResult = patternC.find(trimmedLine)
            val (date, time, statusStr, idStr) = matchResult!!.destructured
            val normalizedDate = convertDateFormat(date, "dd.MM.yyyy", "yyyy-MM-dd")
            LogEntry("$normalizedDate $time", idStr.toInt(), statusStr.lowercase())
        }
        else -> null
    }
}

fun convertDateFormat(dateStr: String, inputFormat: String, outputFormat: String): String {
    val inputFormatter = DateTimeFormatter.ofPattern(inputFormat)
    val outputFormatter = DateTimeFormatter.ofPattern(outputFormat)
    val date = java.time.LocalDate.parse(dateStr, inputFormatter)
    return date.format(outputFormatter)
}

fun calculateDeliveryTimes(logs: List<String>): ProcessingResult {
    val validLogs = mutableListOf<LogEntry>()
    val brokenLogs = mutableListOf<String>()

    logs.forEach { line ->
        val normalized = normalize(line)
        if (normalized != null) {
            validLogs.add(normalized)
        } else {
            brokenLogs.add(line)
        }
    }

    val groupedById = validLogs.groupBy { it.id }

    val incompleteIds = mutableListOf<Int>()
    val timeErrorIds = mutableListOf<Int>()
    val deliveryTimes = mutableMapOf<Int, Long>()

    val dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")

    groupedById.forEach { (id, entries) ->
        val sentEvents = entries.filter { it.status == "sent" }
        val deliveredEvents = entries.filter { it.status == "delivered" }

        if (sentEvents.isEmpty() || deliveredEvents.isEmpty()) {
            incompleteIds.add(id)
            return@forEach
        }

        val latestSent = sentEvents.maxByOrNull {
            LocalDateTime.parse(it.dt, dateTimeFormatter)
        }
        val latestDelivered = deliveredEvents.maxByOrNull {
            LocalDateTime.parse(it.dt, dateTimeFormatter)
        }

        if (latestSent != null && latestDelivered != null) {
            val sentTime = LocalDateTime.parse(latestSent.dt, dateTimeFormatter)
            val deliveredTime = LocalDateTime.parse(latestDelivered.dt, dateTimeFormatter)


            if (deliveredTime.isBefore(sentTime)) {
                timeErrorIds.add(id)
            } else {
                val duration = ChronoUnit.MINUTES.between(sentTime, deliveredTime)
                deliveryTimes[id] = duration
            }
        }
    }

    val violations = deliveryTimes.filter { it.value > 20 }.keys.toList()

    val longestDelivery = deliveryTimes.maxByOrNull { it.value }?.let {
        Pair(it.key, it.value)
    }

    return ProcessingResult(
        validLogs = validLogs,
        brokenLogs = brokenLogs,
        incompleteIds = incompleteIds,
        timeErrorIds = timeErrorIds,
        deliveryTimes = deliveryTimes,
        violations = violations,
        longestDelivery = longestDelivery
    )
}

fun printReport(result: ProcessingResult) {
    println("\nОТЧЕТ О ДОСТАВКЕ")

    println("\n1. Длительность доставки по ID:")
    if (result.deliveryTimes.isNotEmpty()) {
        result.deliveryTimes.entries
            .sortedByDescending { it.value }
            .forEach { (id, minutes) ->
                println("   ID:$id - ${minutes} минут")
            }
    } else {
        println("   Нет данных о времени доставки")
    }

    println("\n2. Самый долгий заказ:")
    if (result.longestDelivery != null) {
        println("   ID:${result.longestDelivery.first} - ${result.longestDelivery.second} минут")
    } else {
        println("   Нет данных")
    }

    println("\n3. Доставка дольше 20 минут:")
    if (result.violations.isNotEmpty()) {
        result.violations.forEach { id ->
            val time = result.deliveryTimes[id]
            println("   ID:$id - ${time} минут")
        }
    } else {
        println("   Нарушителей не обнаружено")
    }

    if (result.incompleteIds.isNotEmpty()) {
        println("\n4. Неполные заказы:")
        result.incompleteIds.forEach { println("   ID:$it") }
    }

    if (result.timeErrorIds.isNotEmpty()) {
        println("\n5. Ошибки времени:")
        result.timeErrorIds.forEach { println("   ID:$it") }
    }

    if (result.brokenLogs.isNotEmpty()) {
        println("\n6. Битые строки:")
        result.brokenLogs.forEachIndexed { index, line ->
            println("   ${index + 1}. \"$line\"")
        }
    }

    println("\nСТАТИСТИКА")
    println("Всего строк логов: ${result.validLogs.size + result.brokenLogs.size}")
    println("Успешно обработано: ${result.validLogs.size}")
    println("Битых строк: ${result.brokenLogs.size}")
    println("Успешных доставок: ${result.deliveryTimes.size}")
    println("Неполных заказов: ${result.incompleteIds.size}")
    println("Ошибок времени: ${result.timeErrorIds.size}")
    println("Нарушителей: ${result.violations.size}")
}

fun main() {
    val logs = listOf(
        "2026-01-22 09:14 | ID:042 | STATUS:sent",
        "TS=22/01/2026-09:27; status=delivered; #042",
        "2026-01-22 09:10 | ID:043 | STATUS:sent",
        "2026-01-22 09:18 | ID:043 | STATUS:delivered",
        "TS=22/01/2026-09:05; status=sent; #044",
        "[22.01.2026 09:40] delivered (id:044)",
        "2026-01-22 09:20 | ID:045 | STATUS:sent",
        "[22.01.2026 09:33] delivered (id:045)",
        "   ts=22/01/2026-09:50; STATUS=Sent; #046   ",
        " [22.01.2026 10:05]   DELIVERED   (ID:046) "
    )

    println("Входные данные:")
    logs.forEachIndexed { index, log ->
        println("${index + 1}. \"$log\"")
    }

    val result = calculateDeliveryTimes(logs)
    printReport(result)

    println("Нормализованные логи:")
    result.validLogs.forEachIndexed { index, log ->
        println("${index + 1}. $log")
    }
}