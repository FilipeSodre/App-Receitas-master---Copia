package com.example.appreceitas

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthRegistrar
import com.google.firebase.auth.auth

class MainActivity : AppCompatActivity() {

    lateinit var user: EditText
    lateinit var pass: EditText
    lateinit var loginButton: Button
    lateinit var cadastroButton: Button

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
            if (user.text.isEmpty() || pass.text.isEmpty()) {
                val snackbar = Snackbar.make(it, "Preencha todos os campos", Snackbar.LENGTH_LONG)
                snackbar.setBackgroundTint(Color.RED)
                snackbar.show()
            } else {
                // Corrigido: passando o texto dos EditText
                auth.signInWithEmailAndPassword(user.text.toString(), pass.text.toString())
                    .addOnCompleteListener { autenticacao ->
                        if (autenticacao.isSuccessful) {
                            val snackbar =
                                Snackbar.make(it, "Sucesso ao cadastrar", Snackbar.LENGTH_LONG)
                            snackbar.setBackgroundTint(Color.BLUE)
                            snackbar.show()

                            val intent = Intent(applicationContext, TelaPrincipal::class.java)
                            startActivity(intent)
                        } else {
                            val snackbar = Snackbar.make(
                                it,
                                "Erro ao cadastrar usuário: ${autenticacao.exception?.message}",
                                Snackbar.LENGTH_LONG
                            )
                            snackbar.setBackgroundTint(Color.RED)
                            snackbar.show()
                        }
                    }
            }

        }
        cadastroButton.setOnClickListener {
            // Redireciona para a tela de cadastro
            Toast.makeText(this, "Botão de cadastro clicado", Toast.LENGTH_SHORT).show()
            val intent = Intent(applicationContext, TelaCadastro::class.java)
            startActivity(intent)
        }
    }
}