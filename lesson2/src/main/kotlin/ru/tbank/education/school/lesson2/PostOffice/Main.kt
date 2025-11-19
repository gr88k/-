package ru.tbank.education.school.lesson2.PostOffice

import ru.tbank.education.school.lesson2.PostOffice.MailItem
import ru.tbank.education.school.lesson2.PostOffice.PostOffice


fun main() {
    println("=== POST OFFICE SYSTEM START ===\n")

    val centralPostOffice = PostOffice("Central Office", "Lenina st., 1")
    println("Opened: ${centralPostOffice.name} at ${centralPostOffice.address}")

    println("\n--- Client Registration ---")
    val client1 = centralPostOffice.registerClient("Ivanov Ivan", "+7-900-123-45-67")
    val client2 = centralPostOffice.registerClient("Petrova Maria", "+7-900-765-43-21")

    println("\n--- Creating Mail Items ---")

    val simpleLetter = Letter(
        id = "LTR001",
        recipientAddress = "Pushkina st., 10, apt 25",
        recipientName = "Sidorov A.V.",
        isRegistered = false,
        weight = 25
    )

    val registeredLetter = Letter(
        id = "LTR002",
        recipientAddress = "Mira ave., 15, apt 3",
        recipientName = "Kuznetsova O.L.",
        isRegistered = true,
        weight = 50
    )

    val standardParcel = Parcel(
        id = "PCL001",
        recipientAddress = "Sadovaya st., 5",
        recipientName = "Frolov P.S.",
        weight = 2.5,
        dimensions = Triple(30, 20, 10),
        isFragile = false
    )

    val fragileParcel = Parcel(
        id = "PCL002",
        recipientAddress = "Centralnaya st., 100",
        recipientName = "Vasilyeva E.K.",
        weight = 1.2,
        dimensions = Triple(15, 15, 15),
        isFragile = true
    )

    println("\n--- Accepting Mail Items ---")
    centralPostOffice.acceptMailItem(simpleLetter)
    centralPostOffice.acceptMailItem(registeredLetter)
    centralPostOffice.acceptMailItem(standardParcel)
    centralPostOffice.acceptMailItem(fragileParcel)

    println("\n--- System Demonstration ---")

    centralPostOffice.displayAllMail()

    println("\n--- Specific Item Information ---")
    simpleLetter.getDescription()
    println("---")
    fragileParcel.getDescription()

    println("\n--- Delivery Process ---")
    centralPostOffice.deliverMailItem("LTR001")
    centralPostOffice.deliverMailItem("PCL001")

    println("\n--- Status After Delivery ---")
    centralPostOffice.displayAllMail()

    println("\n--- Error Handling ---")
    val result = centralPostOffice.deliverMailItem("NON_EXISTENT")
    if (!result) {
        println("Operation failed - item not found")
    }

    println("\n=== SYSTEM WORK COMPLETED ===")
}