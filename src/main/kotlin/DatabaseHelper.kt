import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException
import java.time.LocalDate

class DatabaseHelper {

    private val url = "jdbc:sqlite:bank.db"

    // Method to open a connection to the database
    fun connect(): Connection? {
        return try {
            val conn = DriverManager.getConnection(url)
            println("SQLite connection established.")
            conn
        } catch (e: SQLException) {
            println("Connection error: ${e.message}")
            null
        }
    }

    // Method to close the connection to the database
    fun closeConnection(conn: Connection) {
        try {
            conn.close()
            println("Connection closed.")
        } catch (e: SQLException) {
            println("Error while closing the connection: ${e.message}")
        }
    }

    // Method to create the 'users' and 'transactions' tables
    fun createTables() {
        val sqlUsers = """
            CREATE TABLE IF NOT EXISTS users (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                name TEXT NOT NULL,
                balance REAL NOT NULL,
                pin TEXT NOT NULL
            );
        """

        val sqlTransactions = """
            CREATE TABLE IF NOT EXISTS transactions (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                user_id INTEGER,
                amount REAL NOT NULL,
                date TEXT NOT NULL,
                type TEXT NOT NULL,
                FOREIGN KEY(user_id) REFERENCES users(id)
            );
        """

        val conn = connect()
        conn?.let {
            try {
                val stmt = conn.createStatement()
                stmt.execute(sqlUsers) // Creation of the users table
                stmt.execute(sqlTransactions) // Creation of the transactions table
                println("Tables created successfully.")
            } catch (e: SQLException) {
                println("Error while creating the tables: ${e.message}")
            } finally {
                closeConnection(conn) // Close the connection
            }
        }
    }

    fun insertUser(name: String, balance: Double, pin: String) {
        val sqlInsertUser = "INSERT INTO users(name, balance, pin) VALUES (?, ?, ?)"

        val conn = connect()
        conn?.let {
            try {
                val pstmt = conn.prepareStatement(sqlInsertUser)
                pstmt.setString(1, name)
                pstmt.setDouble(2, balance)
                pstmt.setString(3, pin)
                pstmt.executeUpdate()
                println("User $name successfully inserted.")
            } catch (e: SQLException) {
                println("Error while inserting the user: ${e.message}")
            } finally {
                closeConnection(conn)
            }
        }
    }

    // Method to insert a transaction
    fun insertTransaction(userId: Int, amount: Double, date: String, type: String) {
        val sqlInsertTransaction = "INSERT INTO transactions(user_id, amount, date, type) VALUES (?, ?, ?, ?)"

        val conn = connect()
        conn?.let {
            try {
                val pstmt = conn.prepareStatement(sqlInsertTransaction)
                pstmt.setInt(1, userId)
                pstmt.setDouble(2, amount)
                pstmt.setString(3, date)
                pstmt.setString(4, type)
                pstmt.executeUpdate()
                println("Transaction successfully inserted.")
            } catch (e: SQLException) {
                println("Error while inserting the transaction: ${e.message}")
            } finally {
                closeConnection(conn)
            }
        }
    }


    // Method to retrieve a user from the database
    fun getUserById(userId: Int): BankUserAccount1? {
        val query = "SELECT * FROM users WHERE id = ?"
        var user: BankUserAccount1? = null

        val conn = connect()
        conn?.let {
            try {
                val stmt = conn.prepareStatement(query)
                stmt.setInt(1, userId)
                val resultSet = stmt.executeQuery()

                if (resultSet.next()) {
                    val id = resultSet.getInt("id")
                    val name = resultSet.getString("name")
                    val balance = resultSet.getDouble("balance")
                    user = BankUserAccount1(
                        id.toString(),
                        balance,
                        mutableListOf()
                    )  // Initially load without transactions
                }
            } catch (e: SQLException) {
                println("Error while retrieving the user: ${e.message}")
            } finally {
                closeConnection(conn)
            }
        }
        return user
    }

    // Method to retrieve all transactions of a user
    fun getTransactionsByUserId(userId: Int): List<Transaction> {
        val query = "SELECT * FROM transactions WHERE user_id = ?"
        val transactions = mutableListOf<Transaction>()

        val conn = connect()
        conn?.let {
            try {
                val stmt = conn.prepareStatement(query)
                stmt.setInt(1, userId)
                val resultSet = stmt.executeQuery()

                while (resultSet.next()) {
                    val id = resultSet.getString("id")
                    val amount = resultSet.getDouble("amount")
                    val date = LocalDate.parse(resultSet.getString("date"))
                    val type = TransactionType.valueOf(resultSet.getString("type"))
                    val transaction = Transaction(date, id, type, amount)
                    transactions.add(transaction)
                }
            } catch (e: SQLException) {
                println("Error while retrieving transactions: ${e.message}")
            } finally {
                closeConnection(conn)
            }
        }
        return transactions
    }


}

