package com.example.expensetracker.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.expensetracker.room.Transaction
import com.example.expensetracker.room.TransactionRepository
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

class TransactionViewModel(private val transactionRepository: TransactionRepository) : ViewModel() {

    // Live data for observing all transactions
    val allTransaction: LiveData<List<Transaction>> = transactionRepository.transaction

    // Live data for exposing a single selected transaction
    private val _selectedTransaction = MutableLiveData<Transaction>()
    val selectedTransaction: LiveData<Transaction> = _selectedTransaction

    private val selectedMonth = MutableLiveData<String>("All Months")
    private val _filteredTransactions = MediatorLiveData<List<Transaction>>()
    val filteredTransactions: LiveData<List<Transaction>> get() = _filteredTransactions

    init {
        _filteredTransactions.addSource(allTransaction) { filterTransactions() }
        _filteredTransactions.addSource(selectedMonth) { filterTransactions() }
    }

    // Insert a transaction
    fun insert(transaction: Transaction) = viewModelScope.launch {
        transactionRepository.insert(transaction)
    }

    // Update a transaction
    fun update(transaction: Transaction) = viewModelScope.launch {
        transactionRepository.update(transaction)
    }

    // Delete a transaction
    fun delete(transaction: Transaction) = viewModelScope.launch {
        transactionRepository.delete(transaction)
    }

    // Search a transaction
    fun searchTransactions(searchQuery: String): LiveData<List<Transaction>> {
        return transactionRepository.getSearchResult(searchQuery)
    }

    fun setMonthFilter(month: String) {
        selectedMonth.value = month
    }

    private fun filterTransactions() {
        val all = allTransaction.value ?: return
        val month = selectedMonth.value ?: "All Months"

        if (month == "All Months") {
            _filteredTransactions.value = all
            return
        }

        val monthIndex = getMonthIndex(month) // 1-based (January = 1)
        _filteredTransactions.value = all.filter {
            try {
                val date = LocalDate.parse(it.date, DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.getDefault()))
                date.monthValue == monthIndex
            } catch (e: Exception) {
                false // Skip malformed dates
            }
        }
    }



}

private fun getMonthIndex(month: String): Int {
    return when (month.lowercase(Locale.getDefault())) {
        "january" -> 1
        "february" -> 2
        "march" -> 3
        "april" -> 4
        "may" -> 5
        "june" -> 6
        "july" -> 7
        "august" -> 8
        "september" -> 9
        "october" -> 10
        "november" -> 11
        "december" -> 12
        else -> -1
    }
}
