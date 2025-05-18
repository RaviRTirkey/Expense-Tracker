package com.example.expensetracker.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface TransactionDAO
{
    @Insert
    suspend fun insertTransaction(transaction: Transaction)

    @Update
    suspend fun updateTransaction(transaction: Transaction)

    @Delete
    suspend fun deleteTransaction(transaction: Transaction)

    @Query("SELECT * FROM transactions WHERE userId = :userId ORDER BY transaction_date DESC")
    fun getAllDetails(userId: String): LiveData<List<Transaction>>

    @Query("""
    SELECT * FROM transactions 
    WHERE userId = :userId 
    AND (transaction_name LIKE '%' || :searchQuery || '%' OR transaction_note LIKE '%' || :searchQuery || '%')
    ORDER BY transaction_date DESC
""")
    fun searchTransactions(searchQuery: String, userId: String): LiveData<List<Transaction>>


}