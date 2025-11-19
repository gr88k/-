package ru.tbank.education.school.lesson2.PostOffice

data class Client(
    val id: String,
    val fullName: String,
    val phoneNumber: String
) {
    constructor(fullName: String, phoneNumber: String) : this(
        id = "CL${(1000..9999).random()}",
        fullName = fullName,
        phoneNumber = phoneNumber
    )
}