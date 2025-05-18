package com.example.expensetracker.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Transaction::class], version = 1)
abstract class TransactionDatabase : RoomDatabase() {

    abstract val transactionDAO: TransactionDAO

    //singleton design pattern
    companion object{
        @Volatile
        private var INSTANCE: TransactionDatabase?=null

        fun getInstance(context: Context): TransactionDatabase{
            synchronized(this) {
                var instance = INSTANCE
                if(instance ==null){
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        TransactionDatabase::class.java,
                        "transaction_database"
                    ).fallbackToDestructiveMigration().build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }

}