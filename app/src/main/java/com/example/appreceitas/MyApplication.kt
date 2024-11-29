package com.example.appreceitas

import android.app.Application
import com.google.firebase.FirebaseApp

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        // inicializa o firebase quando a aplicação é criada
        FirebaseApp.initializeApp(this)
    }
}