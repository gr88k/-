import java.time.LocalDate
import java.time.Month
import java.time.format.DateTimeFormatter

fun main() {
    task4()
    task5()
    task6()
    task7()
    task8()
}

/*
1) Строки + регулярные выражения
["Name: Ivan, score=17", ...]
Извлечь имя и score, собрать пары, вывести победителя.
*/
fun task1() {
    val lines = listOf(
        "Name: Ivan, score=17",
        "Name: Olga, score=23",
        "Name: Max, score=5"
    )

    val re = Regex("""^Name:\s*([A-Za-z]+)\s*,\s*score=(\d+)\s*$""")

    val pairs: List<Pair<String, Int>> = lines.mapNotNull { s ->
        val m = re.find(s) ?: return@mapNotNull null
        val name = m.groupValues[1]
        val score = m.groupValues[2].toInt()
        name to score
    }

    println("Task 1 pairs: $pairs")

    val best = pairs.maxByOrNull { it.second }
    if (best != null) {
        println("Task 1 best: ${best.first} (${best.second})")
    } else {
        println("Task 1: no valid lines")
    }
}

/*
2) Даты + коллекции
["2026-01-22", ...]
Преобразовать в даты, отсортировать, посчитать сколько в январе 2026.
*/
fun task2() {
    val dateStrings = listOf(
        "2026-01-22",
        "2026-02-01",
        "2025-12-31",
        "2026-01-05"
    )

    val fmt = DateTimeFormatter.ISO_LOCAL_DATE

    val dates = dateStrings.map { LocalDate.parse(it, fmt) }.sorted()

    println("Task 2 sorted dates: ${dates.joinToString { it.format(fmt) }}")

    val countJan2026 = dates.count { it.year == 2026 && it.month == Month.JANUARY }
    println("Task 2 count in Jan 2026: $countJan2026")
}

/*
3) Коллекции + строки
"apple orange apple banana orange apple"
Частоты слов, вывести слова с частотой > 1 по алфавиту.
*/
fun task3() {
    val text = "apple orange apple banana orange apple"

    val words = text.trim().split(Regex("""\s+""")).filter { it.isNotEmpty() }

    val freq = mutableMapOf<String, Int>()
    for (w in words) {
        freq[w] = (freq[w] ?: 0) + 1
    }

    println("Task 3 freq: $freq")

    val repeated = freq
        .filter { (_, c) -> c > 1 }
        .keys
        .sorted()

    println("Task 3 repeated words: ${repeated.joinToString(", ")}")
}

fun task4() {
    val strings = listOf("A-123", "B-7", "AA-12", "C-001", "D-99x")

    val regex = Regex("^[A-Z]-\\d{1,3}$")

    val filtered = strings.filter { regex.matches(it) }

    println("Исходный список: $strings")
    println("Отфильтрованный список: $filtered")
}

fun task5() {
        val strings = listOf(" Hello world ", "A B C", " one")

        println("Исходные строки:")
        var i = 0
        strings.forEach { str ->
            println("[$i]: '$str'")
            i += 1
        }

        println("\nНормализованные строки:")

        i = 0
        strings.forEach { str ->
            val normalized = str.trim().replace(Regex("\\s+"), " ")
            println("[$i]: '$normalized'")
            i += 1
    }
}

fun task6() {
    val datePairs = listOf(
        Pair("2026-01-01", "2026-01-10"),
        Pair("2025-12-31", "2026-01-01"),
        Pair("2026-02-01", "2026-01-22")
    )

    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

    val differences = mutableListOf<Long>()

    println("Разница в днях (вторая дата - первая дата):")

    for ((firstStr, secondStr) in datePairs) {
        val firstDate = LocalDate.parse(firstStr, formatter)
        val secondDate = LocalDate.parse(secondStr, formatter)

        val firstMonth: Month = firstDate.month
        val secondMonth: Month = secondDate.month

        val daysDifference = firstDate.until(secondDate).days.toLong()

        differences.add(daysDifference)

        println("$secondStr ($secondMonth ${secondDate.dayOfMonth}) - $firstStr ($firstMonth ${firstDate.dayOfMonth}) = $daysDifference дней")
    }

    println("\nИтоговый список разниц: $differences")
}

fun task7 () {
    val data = listOf(
        "math:Ivan", "bio:Olga", "math:Max",
        "bio:Ivan", "cs:Olga"
    )

    val result = mutableMapOf<String, MutableList<String>>()

    for (item in data) {
        val parts = item.split(":")
        val subject = parts[0]
        val student = parts[1]

        if (!result.containsKey(subject)) {
            result[subject] = mutableListOf()
        }

        result[subject]?.add(student)
    }

    println("Словарь (предмет - список учеников):")
    for ((subject, students) in result) {
        println("$subject: $students")
    }

    println("\nВ одну строку: $result")
}

fun task8() {
    val strings = listOf(
        "Start at 2026/01/22 09:14",
        "No time here",
        "End: 22-01-2026 18:05"
    )

    println("Исходные строки:")
    strings.forEach { println("  '$it'") }

    println("\nНайденные даты в формате YYYY-MM-DD HH:MM:")

    val regex1 = Regex("""\d{4}/\d{2}/\d{2}\s+\d{2}:\d{2}""")
    val regex2 = Regex("""\d{2}-\d{2}-\d{4}\s+\d{2}:\d{2}""")

    for (str in strings) {
        val match1 = regex1.find(str)
        if (match1 != null) {
            val dateTimeStr = match1.value

            //val parser = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm")

            val parts = dateTimeStr.split(" ")
            val dateStr = parts[0]
            val timeStr = parts[1]

            val dateParser = DateTimeFormatter.ofPattern("yyyy/MM/dd")
            val date = LocalDate.parse(dateStr, dateParser)

            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
            val result = "${date.format(formatter)} $timeStr"

            println("  '$str' -> $result")
            continue
        }

        val match2 = regex2.find(str)
        if (match2 != null) {
            val dateTimeStr = match2.value

            val parts = dateTimeStr.split(" ")
            val dateStr = parts[0]
            val timeStr = parts[1]

            val dateParser = DateTimeFormatter.ofPattern("dd-MM-yyyy")
            val date = LocalDate.parse(dateStr, dateParser)

            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
            val result = "${date.format(formatter)} $timeStr"

            println("  '$str' -> $result")
            continue
        }
    }
}