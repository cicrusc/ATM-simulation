![Kotlin](https://img.shields.io/badge/Kotlin-7F52FF?style=for-the-badge&logo=kotlin&logoColor=white)
![SQLite](https://img.shields.io/badge/SQLite-003B57?style=for-the-badge&logo=sqlite&logoColor=white)
![Git](https://img.shields.io/badge/Git-F05032?style=for-the-badge&logo=git&logoColor=white)
![OOP](https://img.shields.io/badge/OOP-Object_Oriented_Programming-FF6F00?style=for-the-badge&logo=java&logoColor=white)
![CLI](https://img.shields.io/badge/CLI-Console_Application-4EAA25?style=for-the-badge&logo=gnu-bash&logoColor=white)
---
# ATM Simulation

This project simulates an ATM system where users can perform basic banking operations such as deposits, withdrawals, and balance inquiries. It features a simulated PIN entry for user authentication and transaction logging. The system integrates with an SQLite database to store user data and transaction history.

## Features

- **Simulated PIN Authentication**: The user must enter a 4-digit PIN to access their account.
- **Deposit**: Users can deposit money into their account.
- **Withdraw**: Users can withdraw money from their account (with balance checks).
- **Check Balance**: Users can view their current account balance.
- **Transaction History**: Users can view their past transactions (deposits/withdrawals) in a sorted list.
- **SQLite Database**: User data and transactions are stored in a local SQLite database.

## Setup

To set up this project:

1. Clone the repository:
```bash
git clone https://github.com/yourusername/atm-simulation.git
```

3. Open the project in your preferred IDE (e.g., IntelliJ IDEA).

4. Run the `Main.kt` file to start the simulation.

5. Ensure that the SQLite database file (`bank.db`) is located in the project directory.

## How It Works

1. When you start the simulation, you will be prompted to enter a **user ID**.
2. After entering a valid user ID, the system will simulate **PIN entry** for authentication (PIN is simulated as `1234`).
3. The user will then be able to:
- **Deposit** money into the account.
- **Withdraw** money (with balance check).
- **Check the balance** of the account.
- **View transaction history** (all previous deposits and withdrawals).
- **Exit** the application.

## Database

The system uses an SQLite database with two tables:

- **users**: Stores user account information (ID, name, balance, PIN).
- **transactions**: Stores transaction details (ID, user ID, amount, date, type of transaction).

## Future Enhancements

- Add encryption for the PIN and sensitive data.
- Implement support for multiple users with unique PINs and accounts.
- Include more complex features such as transfer between accounts or generating account statements.

---
