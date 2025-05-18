package com.example.expensetracker.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "transactions")
data class Transaction(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "transaction_id")
    val id: Int = 0,
    @ColumnInfo(name = "transaction_name")
    val name: String,
    @ColumnInfo(name = "transaction_amount")
    val amount: Double,
    @ColumnInfo(name = "transaction_date")
    val date: String,
    @ColumnInfo(name = "transaction_category")
    val category: String,
    @ColumnInfo(name = "transaction_note")
    val note: String,
    val userId: String
) : Parcelable
