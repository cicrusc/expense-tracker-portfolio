import java.time.LocalDate
import java.time.Month
import java.time.format.DateTimeFormatter

data class Expense(
    val amount: Double,
    val date: LocalDate,
    val description: String
)

class Portfolio(private var balance: Double = 0.0) {
    private val expenseList = mutableListOf<Expense>()
    private val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")


    //Function helper
    private fun readAmount(message: String): Double {
        while (true) {
            println(message)
            readln().toDoubleOrNull()?.let {return it}
            println("Invalid amount. Insert a number (e.g. 100.0)")
        }
    }

    //Function helper
    private fun readValidDate(message: String): LocalDate {
        while (true) {
            println(message)
            try {
                return (LocalDate.parse(readln(), dateFormatter))
            } catch (e: Exception) {
                println("Invalid date format. Please use yyyy-mm-dd.")
            }
        }
    }

    //Function to add a new expense
    fun addExpense() {
        println("-ADD EXPENSE-")

        println("Enter description(e.g., television):")
        val description = readln().uppercase()

        val amount = readAmount("Enter the amount (e.g., 100.0):")
        val date = readValidDate("Insert the date (yyyy-mm-dd):")

        val newExpense = Expense(amount, date, description)
        expenseList.add(newExpense)
        balance += newExpense.amount

        println("Expense added successfully!")
    }

    //Function to modify existing expense
    fun modifyExpense() {
        println("-MODIFY-")
        if (expenseList.isEmpty()) {
            println("No expenses available to modify.")
            return
        }

        viewLastExpenses()
        val index = readAmount("Enter the number of the expense to modify:").toInt() - 1

        if (index !in expenseList.indices) {
            println("Invalid expense number.")
            return

        }

        val oldExpense = expenseList[index]
        balance -= oldExpense.amount


        println("Enter the new details (press Enter to keep the current value):")

        println("Current description: ${oldExpense.description}")
        val newDescription = readln().let { if (it.isEmpty()) oldExpense.description else it.uppercase() }

        val newDate = try {
            println("Current date: ${oldExpense.date}")
            val input = readln()
            if (input.isEmpty()) oldExpense.date else LocalDate.parse(input, dateFormatter)
        } catch (e: Exception) {
            println("Invalid date, keeping the previous one.")
            oldExpense.date
        }

        println("Current amount: ${oldExpense.amount}")
        val newAmount = readln().toDoubleOrNull() ?: oldExpense.amount
        balance += newAmount

        expenseList[index] = Expense(newAmount, newDate, newDescription)
        println("Expense successfully modified!")
    }

    fun viewLastExpenses() {
        println("-LAST EXPENSES-")
        if (expenseList.isEmpty()) {
            println("No expenses recorded.")
            return
        }

        expenseList.takeLast(10).forEachIndexed { index, expense ->
            println("${index + 1}. Date: ${expense.date}, Description: ${expense.description}, Amount: ${expense.amount}€")
        }
    }


    //Function to search list of expenses by month
    fun searchExpenseByMonth() {
        println("-SEARCH-")
        println("Select a month(e.g. January):")

        try {
            val month = Month.valueOf(readln().uppercase())
            val filteredExpenses = expenseList.filter { it.date.month == month }

            if (filteredExpenses.isEmpty()) {
                println("No expenses found for the selected month.")
                return
            }

            println("\nExpenses for $month:")
            filteredExpenses.forEach { expense ->
                println("Date: ${expense.date}, Description: ${expense.description}, Amount: ${expense.amount}€")
            }

            val totalAmount = filteredExpenses.sumOf { it.amount }
            println("\nTotal expenses for $month: $totalAmount€")

        } catch (e: IllegalArgumentException) {
            println("Invalid month. Enter a valid month in English (e.g., JANUARY)")
        }
    }


    //Function to view total expenses amount by month
    fun viewMonthlyExpense() {
        println("-MONTHLY TOTAL EXPENSE-")
        if (expenseList.isEmpty()) {
            println("No expenses available.")
            return
        }

        expenseList
            .groupBy { it.date.month }
            .forEach { (month, expenses)  -> val total = expenses.sumOf { it.amount }
        println("$month: $total€")
    }
}

    fun viewBalance() {
        println("-BALANCE-")
        println("Current balance: $balance€")
    }

//Principal menu
fun menu() {


    while (true) {
        // Multi-line menu
        println(
            """
            MENU
            Select an option:
            1. Add
            2. Modify
            3. Last 
            4. Search
            5. Monthly total
            6. Balance
            0. Exit
            """.trimIndent()
        )

        when (readln().toIntOrNull()) {
            1 -> addExpense() // Add
            2 -> modifyExpense()
            3 -> viewLastExpenses() //Last
            4 -> searchExpenseByMonth() // Search
            5 -> viewMonthlyExpense()
            6 -> viewBalance()
            0 -> break
            else -> println("Invalid option. Please try again.")
        }
    }
}

}

fun main() {

    val myPortfolio = Portfolio()
    myPortfolio.menu()

}