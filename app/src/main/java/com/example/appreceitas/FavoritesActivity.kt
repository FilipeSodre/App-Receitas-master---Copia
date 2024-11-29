package com.example.appreceitas

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.MaterialToolbar
import kotlinx.coroutines.launch

class FavoritesActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: RecipesAdapter
    private val firebaseManager = FireBaseManager()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorites)

        val toolbar: MaterialToolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Favoritos"

        recyclerView = findViewById(R.id.recyclerViewFavorites)
        recyclerView.layoutManager = GridLayoutManager(this, 2)

        adapter = RecipesAdapter(
            emptyList(),
            onFavoriteClick = { recipe ->
                // atualiza o status de favorito da receita
                updateRecipeInDatabase(recipe.copy(isFavorite = !recipe.isFavorite))
            },
            onItemClick = { recipe ->
                // vai para a tela de detalhes da receita
                val intent = Intent(this, RecipeDetailActivity::class.java).apply {
                    putExtra("RECIPE_ID", recipe.id)
                    putExtra("RECIPE_TITLE", recipe.title)
                    putExtra("RECIPE_DESCRIPTION", recipe.description)
                    putExtra("RECIPE_INGREDIENTS", recipe.ingredients)
                    putExtra("RECIPE_INSTRUCTIONS", recipe.instructions)
                    putExtra("RECIPE_IMAGE_URL", recipe.imageUrl)
                }
                startActivity(intent)
            }
        )

        recyclerView.adapter = adapter

        loadFavoriteRecipes()
    }

    private fun loadFavoriteRecipes() {
        firebaseManager.getFavoriteRecipesRealtime { recipes ->
            Log.d("FavoritesActivity", "Receitas favoritas carregadas: ${recipes.size}")
            runOnUiThread {
                adapter.updateRecipes(recipes)
            }
        }
    }

    private fun updateRecipeInDatabase(recipe: RecipeModel) {
        lifecycleScope.launch {
            try {
                val success = firebaseManager.updateRecipe(recipe)
//                if (success) {
////                    Toast.makeText(
////                        this@FavoritesActivity,
////                        if (recipe.isFavorite) "Adicionado aos favoritos" else "Removido dos favoritos",
////                        Toast.LENGTH_SHORT
////                    ).show()
//                } else {
//                    Toast.makeText(
//                        this@FavoritesActivity,
//                        "Erro ao atualizar favorito",
//                        Toast.LENGTH_SHORT
//                    ).show()
//                }
            } catch (e: Exception) {
                Log.e("FavoritesActivity", "Erro ao atualizar receita", e)
                Toast.makeText(
                    this@FavoritesActivity,
                    "Erro ao atualizar favorito: ${e.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}