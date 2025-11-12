package ru.tbank.education.school.lesson1

/**
 * Сумма четных чисел.
 */
fun sumEvenNumbers(numbers: Array<Int>): Int {
    var sum = 0
    for (i in numbers) {
        if (i % 2 == 0) {
            sum = sum + i
        }
    }

    return sum
}

fun main() {
    val a = arrayOf(1, 2, 3, 4)
    println(sumEvenNumbers(a))
}