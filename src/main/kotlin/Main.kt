import java.time.LocalDate
import java.util.*

    enum class TransactionType {
        DEPOSIT, WITHDRAWAL
    }

    class BankUserAccount1(val accountID: String, var balance: Double, val transaction: MutableList<Transaction>) {

        // Method to handle deposits
        fun deposit(transaction: Transaction) {
            balance += transaction.amount
        }

        // Method to handle withdrawals
        fun withdraw(transaction: Transaction) {
            if (transaction.amount <= balance) {
                balance -= transaction.amount
            } else {
                println("Insufficient funds.")
            }
        }

        // Method to display the transaction summary, ordered by date
        fun transactionSummary() {

            Thread.sleep(1200)
            createEmptySpace(5)
            println("TRANSACTION HISTORY:")
            createEmptySpace(1)


            // Print table header with aligned columns
            println(String.format("%-15s %-10s %10s", "Date", "Type", "Amount"))
            println("-".repeat(40))  // Create a line separator

            // Sort the transactions by date (descending) and print them with formatting
            val sortedTransactions = transaction.sortedByDescending { it.date }
            sortedTransactions.forEach { transaction ->
                println(String.format("%-15s %-10s %10.2f$", transaction.date, transaction.type, transaction.amount))


            }
            Thread.sleep(1500)
            createEmptySpace(1)
            println("BALANCE: %.2f$".format(Locale.US, balance))
            createEmptySpace(1)

        }
    }


    class Transaction(val date: LocalDate, val id: String, val type: TransactionType, val amount: Double) {
        // Override toString to format how the transaction details are printed
        override fun toString(): String {
            return "$date|$type|${if (type == TransactionType.WITHDRAWAL) "-" else ""}$amount$"
        }
    }

    // Function to display the banner at the top
    fun displayBanner() {
        val width = 40
        val title = "BON"
        val subtitle = "Bank Of Nothing"
        println("-".repeat(width))
        println("|" + " ".repeat((width - title.length) / 2 - 1) + title + " ".repeat((width - title.length) / 2 - 1) + "|")
        println("|" + " ".repeat((width - subtitle.length) / 2 - 1) + subtitle + " ".repeat((width - subtitle.length) / 2 - 1) + "|")
        println("-".repeat(width))
    }

    // Function to simulate PIN entry
    fun simulatePinEntry(): String {
        println("Enter your 4-digit PIN:")
        repeat(4) {
            Thread.sleep(1200)
            print("* ")  // Display * for each digit entered
        }
        Thread.sleep(800)
        println("\n[CANCEL] [CLEAR] [OK]") // Display options for the user
        val pin = "1234"
        return pin
    }

    // Function to create empty space in the terminal for better visual layout
    fun createEmptySpace(lines: Int) {
        repeat(lines) {
            println()
        }
    }

fun menu(user: BankUserAccount1, dbHelper: DatabaseHelper) {
    var exit = false

    while (!exit) {
        println("Please select the type of operation you would like to perform:")
        createEmptySpace(1)
        Thread.sleep(1200)
        println("1. Deposit")
        println("2. Withdraw")
        println("3. History")
        println("4. Exit")
        createEmptySpace(1)
        println("BALANCE: %.2f$".format(user.balance))

        val choice = readln()?.toIntOrNull()

        when (choice) {
            1 -> {
                createEmptySpace(20)
                println("Enter the deposit amount:")
                val amount = readln()?.toDoubleOrNull()
                if (amount != null && amount > 0) {
                    val depositTransaction = Transaction(LocalDate.now(), "#NEWDEP", TransactionType.DEPOSIT, amount)
                    user.deposit(depositTransaction)
                    dbHelper.insertTransaction(user.accountID.toInt(), amount, LocalDate.now().toString(), "DEPOSIT")
                    println("Processing your request...")
                    Thread.sleep(1500)
                    createEmptySpace(15)
                    println("Transaction completed!")
                } else {
                    println("Invalid amount. Please enter a positive number.")
                }
            }

            2 -> {
                createEmptySpace(20)
                println("Enter the withdrawal amount:")
                val amount = readln()?.toDoubleOrNull()
                if (amount != null && amount > 0) {
                    if (amount <= user.balance) {
                        val withdrawalTransaction =
                            Transaction(LocalDate.now(), "#NEWWWITH", TransactionType.WITHDRAWAL, amount)
                        user.withdraw(withdrawalTransaction)
                        dbHelper.insertTransaction(user.accountID.toInt(), amount, LocalDate.now().toString(), "WITHDRAWAL")
                        println("Processing your request...")
                        Thread.sleep(1500)
                        createEmptySpace(15)
                        println("Transaction completed!")
                    } else {
                        println("Insufficient funds.")
                    }
                } else {
                    println("Invalid amount.")
                }
            }

            3 -> {
                // Retrieve and display transactions from the database
                val transactions = dbHelper.getTransactionsByUserId(user.accountID.toInt())
                transactions.forEach { transaction -> println(transaction) }
            }

            4 -> {
                println("Exiting...")
                exit = true
            }

            else -> {
                println("Invalid option. Please try again.")
            }
        }
    }
}

fun main() {
       val dbHelper = DatabaseHelper()  // Create an instance of DatabaseHelper

        dbHelper.createTables()  // Create the tables if they don't already exist

        // Insert some sample users and transactions if needed
        dbHelper.insertUser("John Doe", 10000.0, "1234")
        dbHelper.insertUser("Jane Doe", 5000.0, "5678")
        dbHelper.insertUser("Mark Toney", 17000.0, "7845")
        dbHelper.insertUser("Cassidy Pillow", 28000.0, "6358")
        dbHelper.insertUser("Mike Sullivan", 9000.0, "9314")
        dbHelper.insertUser("Randy Roadhouse", 55000.0, "4267")

        dbHelper.insertTransaction(1, 1000.0, "2024-10-22", "DEPOSIT")
        dbHelper.insertTransaction(1, 500.0, "2024-10-23", "WITHDRAWAL")
        dbHelper.insertTransaction(2, 2000.0, "2024-10-22", "DEPOSIT")
        dbHelper.insertTransaction(2, 500.0, "2024-10-23", "WITHDRAWAL")

        // Request the user ID to authenticate the user
        println("Enter user ID:")
        val userId = readln().toIntOrNull()

        // Retrieve the user from the database
        if (userId != null) {
            val user = dbHelper.getUserById(userId)  // Retrieve the user from the DB
            if (user != null) {
                // If the user exists, start the menu
                displayBanner()
                simulatePinEntry()  // Simulate PIN insertion
                menu(user, dbHelper)           // Start the menu for this user
            } else {
                println("User not found.")
            }
        } else {
            println("Invalid user ID.")
        }
    }
