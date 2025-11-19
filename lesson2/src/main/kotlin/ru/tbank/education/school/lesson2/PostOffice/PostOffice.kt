package ru.tbank.education.school.lesson2.PostOffice

import ru.tbank.education.school.lesson2.PostOffice.*

class PostOffice(
    val name: String,
    val address: String
) {
    private val clients: MutableList<Client> = mutableListOf()
    private val mailItems: MutableList<MailItem> = mutableListOf()

    fun registerClient(fullName: String, phoneNumber: String): Client {
        val newClient = Client(fullName, phoneNumber)
        clients.add(newClient)
        println("New client registered: ${newClient.fullName} (ID: ${newClient.id})")
        return newClient
    }

    fun acceptMailItem(mailItem: MailItem): Boolean {
        return try {
            mailItem.statusDetail = "Accepted at '$name'"
            mailItems.add(mailItem)
            println("Accepted item ID: ${mailItem.id} from ${mailItem.recipientName}")
            true
        } catch (e: Exception) {
            println("Error accepting item: ${e.message}")
            false
        }
    }

    fun deliverMailItem(mailItemId: String): Boolean {
        val item = mailItems.find { it.id == mailItemId }
        return if (item != null) {
            item.statusDetail = "Delivered to recipient"
            println("Item $mailItemId delivered!")
            true
        } else {
            println("Item with ID $mailItemId not found")
            false
        }
    }

    fun displayAllMail() {
        println("\n=== All items in '$name' ===")
        if (mailItems.isEmpty()) {
            println("No items")
        } else {
            mailItems.forEach { it.getDescription() }
        }
    }

    val allClients: List<Client>
        get() = clients.toList()
}