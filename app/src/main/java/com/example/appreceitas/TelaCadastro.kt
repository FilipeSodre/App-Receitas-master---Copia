package com.example.appreceitas

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth



class TelaCadastro : AppCompatActivity() {

    lateinit var cadastrar: Button
    lateinit var user: EditText
    lateinit var pass: EditText
    lateinit var voltarButton: ImageButton

    private val auth = FirebaseAuth.getInstance()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_tela_cadastro)

        user = findViewById(R.id.userEmail)
        pass = findViewById(R.id.UserPass)
        cadastrar = findViewById(R.id.cadastrar)
        voltarButton=findViewById(R.id.voltar)

        cadastrar.setOnClickListener { view ->
            if (user.text.isEmpty() || pass.text.isEmpty()) {
                val snackbar = Snackbar.make(view, "Preencha todos os campos", Snackbar.LENGTH_LONG)
                snackbar.setBackgroundTint(Color.RED)
                snackbar.show()
            } else {
                // Corrigido: passando o texto dos EditText
                auth.createUserWithEmailAndPassword(user.text.toString(), pass.text.toString())
                    .addOnCompleteListener { cadastro ->
                        if (cadastro.isSuccessful) {
                            val snackbar =
                                Snackbar.make(view, "Sucesso ao cadastrar", Snackbar.LENGTH_LONG)
                            snackbar.setBackgroundTint(Color.BLUE)
                            snackbar.show()

                            // Redirecionando para a próxima tela (TelaPrincipal)
                            val intent = Intent(applicationContext, TelaPrincipal::class.java)
                            startActivity(intent)
                        } else {
                            // Se a criação do usuário falhar, exibe um erro
                            val snackbar = Snackbar.make(
                                view, "Erro ao cadastrar usuário: ${cadastro.exception?.message}",
                                Snackbar.LENGTH_LONG
                            )
                            snackbar.setBackgroundTint(Color.RED)
                            snackbar.show()
                        }
                    }
            }
        }


        voltarButton.setOnClickListener {
            intent = Intent(applicationContext, MainActivity::class.java)
            startActivity(intent)
        }













    }
}
