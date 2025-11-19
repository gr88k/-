package ru.tbank.education.school.lesson2.PostOffice

abstract class MailItem(
    val id: String,
    val recipientAddress: String,
    val recipientName: String,
    private var _status: String = "Created"
) {
    val status: String
        get() = "Current status: $_status"

    var statusDetail: String = _status
        set(value) {
            println("Item $id: Status changing from '$field' to '$value'")
            field = value
            _status = value
        }

    abstract val deliveryTimeDays: Int

    abstract fun calculateCost(): Double

    open fun getDescription() {
        println("Mail Item ID: $id")
        println("Recipient: $recipientName, $recipientAddress")
        println(status)
    }
}

class Letter(
    id: String,
    recipientAddress: String,
    recipientName: String,
    val isRegistered: Boolean,
    val weight: Int
) : MailItem(id, recipientAddress, recipientName) {

    override val deliveryTimeDays: Int
        get() = 5

    override fun calculateCost(): Double {
        var cost = 50.0
        cost += (weight / 10) * 5.0
        if (isRegistered) cost += 100.0
        return cost
    }

    override fun getDescription() {
        super.getDescription()
        println("Type: Letter ${if (isRegistered) "Registered" else "Simple"}")
        println("Weight: ${weight}g")
        println("Cost: ${calculateCost()} rub")
        println("Estimated delivery: $deliveryTimeDays days\n")
    }
}

class Parcel(
    id: String,
    recipientAddress: String,
    recipientName: String,
    val weight: Double,
    val dimensions: Triple<Int, Int, Int>,
    val isFragile: Boolean
) : MailItem(id, recipientAddress, recipientName) {

    override val deliveryTimeDays: Int
        get() = 7

    override fun calculateCost(): Double {
        var cost = 200.0
        cost += weight * 50.0
        val volume = dimensions.first * dimensions.second * dimensions.third / 1000.0
        cost += volume * 10.0
        if (isFragile) cost += 150.0
        return cost
    }

    override fun getDescription() {
        super.getDescription()
        println("Type: Parcel ${if (isFragile) "Fragile" else ""}")
        println("Weight: ${weight}kg")
        println("Dimensions: ${dimensions.first}x${dimensions.second}x${dimensions.third} cm")
        println("Cost: ${calculateCost()} rub")
        println("Estimated delivery: $deliveryTimeDays days\n")
    }
}