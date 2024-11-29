package com.example.appreceitas

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {

    private lateinit var user: EditText
    private lateinit var pass: EditText
    private lateinit var loginButton: Button
    private lateinit var cadastroButton: Button

    private val auth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        user = findViewById(R.id.userid)
        pass = findViewById(R.id.password)
        loginButton = findViewById(R.id.loginButton)
        cadastroButton = findViewById(R.id.buttonSignUp)


        loginButton.setOnClickListener {
            if (validateInputs()) {
                loginUser()
            }
        }

        cadastroButton.setOnClickListener {
            val intent = Intent(applicationContext, TelaCadastro::class.java)
            startActivity(intent)
        }
    }

    private fun validateInputs(): Boolean {
        val email = user.text.toString().trim()
        val password = pass.text.toString().trim()

        if (email.isEmpty() || password.isEmpty()) {
            showMessage("Preencha todos os campos", isError = true)
            return false
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            showMessage("Digite um email válido", isError = true)
            return false
        }

        if (password.length < 6) {
            showMessage("A senha deve ter pelo menos 6 caracteres", isError = true)
            return false
        }

        return true
    }

    private fun loginUser() {

        auth.signInWithEmailAndPassword(user.text.toString(), pass.text.toString())
            .addOnCompleteListener { task ->

                if (task.isSuccessful) {
                    showMessage("Login realizado com sucesso", isError = false)
                    val intent = Intent(applicationContext, TelaPrincipal::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    showMessage("Erro ao fazer login: ${task.exception?.message}", isError = true)
                }
            }
    }


    private fun showMessage(message: String, isError: Boolean) {
        val snackbar = Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG)
        snackbar.setBackgroundTint(if (isError) Color.RED else Color.BLUE)
        snackbar.show()
    }
}

