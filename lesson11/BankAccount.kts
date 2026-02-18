package homework

class BankAccount(val id: String, var balance: Int) {

    fun transfer(to: BankAccount, amount: Int) {
        val firstLock = if (this.id < to.id) this else to
        val secondLock = if (this.id < to.id) to else this

        synchronized(firstLock) {
            synchronized(secondLock) {
                if (this.balance >= amount) {
                    this.balance -= amount
                    to.balance += amount
                }
            }
        }
    }
}