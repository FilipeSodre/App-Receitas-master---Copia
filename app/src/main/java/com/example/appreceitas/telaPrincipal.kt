package com.example.appreceitas

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch

class TelaPrincipal : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: RecipesAdapter
    private val firebaseManager = FireBaseManager()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.tela_principal)

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = RecipesAdapter(emptyList())
        recyclerView.adapter = adapter

        // Adicionar uma receita de teste
        lifecycleScope.launch {
            try {
                val receitaTeste = RecipeModel(
                    title = "Bolo de Chocolate",
                    description = "Bolo de chocolate super fofinho",
                    ingredients = "2 xícaras de farinha, 1 xícara de chocolate em pó, 3 ovos, 1 xícara de leite",
                    instructions = "1. Misture os ingredientes secos\n2. Adicione os ovos e o leite\n3. Asse por 40 minutos"
                )

                Log.d("TelaPrincipal", "Tentando salvar receita: ${receitaTeste.title}")
                val sucesso = firebaseManager.saveRecipe(receitaTeste)
                if (sucesso) {
                    Log.d("TelaPrincipal", "Receita salva com sucesso!")
                    Toast.makeText(this@TelaPrincipal, "Receita salva com sucesso!", Toast.LENGTH_SHORT).show()
                } else {
                    Log.e("TelaPrincipal", "Falha ao salvar receita")
                    Toast.makeText(this@TelaPrincipal, "Erro ao salvar receita", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Log.e("TelaPrincipal", "Exceção ao salvar receita", e)
                Log.e("TelaPrincipal", "Mensagem de erro: ${e.message}")
                Log.e("TelaPrincipal", "Causa: ${e.cause}")
                e.printStackTrace()
                Toast.makeText(this@TelaPrincipal, "Erro inesperado: ${e.message}", Toast.LENGTH_LONG).show()
            }
        }

        // Observar as receitas
        firebaseManager.getRecipesRealtime { recipes ->
            Log.d("TelaPrincipal", "Receitas carregadas: ${recipes.size}")
            adapter = RecipesAdapter(recipes)
            recyclerView.adapter = adapter
        }
    }
}