package com.example.expensetracker.room

import androidx.lifecycle.LiveData

class TransactionRepository(private val dao: TransactionDAO, private val userId: String) {

    val transaction = dao.getAllDetails(userId)

    suspend fun insert(transaction: Transaction) {
        return dao.insertTransaction(transaction)
    }

    suspend fun update(transaction: Transaction){
        return dao.updateTransaction(transaction)
    }

    suspend fun delete(transaction: Transaction){
        return dao.deleteTransaction(transaction)
    }

    fun getSearchResult(searchQuery: String): LiveData<List<Transaction>> {
        val trimmedQuery = searchQuery.trim()
        return if (trimmedQuery.isEmpty()) {
            dao.getAllDetails(userId) // Or return empty LiveData
        } else {
             dao.searchTransactions("%$trimmedQuery%", userId)
        }
    }

}