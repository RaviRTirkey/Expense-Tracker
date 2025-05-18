package com.example.expensetracker

import android.app.Application
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth

class ThisApplication: Application() {
    lateinit var auth: FirebaseAuth

    override fun onCreate(){
        super.onCreate()

        // Initialize Firebase
        FirebaseApp.initializeApp(this)

        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance()
    }
}