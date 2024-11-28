package com.example.appreceitas

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TelaPrincipal : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: RecipesAdapter
    private val firebaseManager = FireBaseManager()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.tela_principal)

        // Set up toolbar
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        // Set up RecyclerView
        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Initialize adapter with empty list and favorite click handler
        adapter = RecipesAdapter(
            emptyList(),
            onFavoriteClick = { recipe ->
                updateRecipeInDatabase(recipe.copy(isFavorite = !recipe.isFavorite))
            },
            onItemClick = { recipe ->
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

        // Set up SearchView
        val searchView: SearchView = findViewById(R.id.searchView)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                // Implement search functionality here
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                // Implement search suggestions or filtering here
                return true
            }
        })

        // Set up Menu button
        val btnMenu: ImageButton = findViewById(R.id.btnMenu)
        btnMenu.setOnClickListener {
            Toast.makeText(this, "Menu button clicked", Toast.LENGTH_SHORT).show()
        }

        // Set up Bottom Navigation
        val bottomNavigation: BottomNavigationView = findViewById(R.id.bottomNavigation)
        bottomNavigation.setOnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.navigation_home -> {
                    // Already on home, do nothing
                    true
                }
                R.id.navigation_add -> {
                    val intent = Intent(this, AddRecipeActivity::class.java)
                    startActivity(intent)
                    true
                }
                R.id.navigation_favorites -> {
                    val intent = Intent(this, FavoritesActivity::class.java)
                    startActivity(intent)
                    true
                }
                R.id.navigation_profile -> {
                    // Implement profile screen navigation
                    true
                }
                else -> false
            }
        }

        // Observe recipes
        firebaseManager.getRecipesRealtime { recipes ->
            Log.d("TelaPrincipal", "Receitas carregadas: ${recipes.size}")
            adapter.updateRecipes(recipes)
        }
    }

    private fun updateRecipeInDatabase(recipe: RecipeModel) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val success = firebaseManager.updateRecipe(recipe)
                if (success) {
                    runOnUiThread {
                        Toast.makeText(
                            this@TelaPrincipal,
                            if (recipe.isFavorite) "Adicionado aos favoritos" else "Removido dos favoritos",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } else {
                    runOnUiThread {
                        Toast.makeText(
                            this@TelaPrincipal,
                            "Erro ao atualizar favorito",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            } catch (e: Exception) {
                Log.e("TelaPrincipal", "Erro ao atualizar receita", e)
                runOnUiThread {
                    Toast.makeText(
                        this@TelaPrincipal,
                        "Erro ao atualizar receita: ${e.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }
}