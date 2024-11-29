package com.example.appreceitas

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.launch

class TelaPrincipal : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: RecipesAdapter
    private val firebaseManager = FireBaseManager()
    private var allRecipes: List<RecipeModel> = emptyList()

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
                query?.let { searchRecipes(it) }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                newText?.let { searchRecipes(it) }
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
            runOnUiThread {
                allRecipes = recipes
                adapter.updateRecipes(recipes)
                if (recipes.isEmpty()) {
                    Toast.makeText(this, "Nenhuma receita encontrada", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun searchRecipes(query: String) {
        val filteredRecipes = if (query.isEmpty()) {
            allRecipes
        } else {
            allRecipes.filter { recipe ->
                recipe.title.contains(query, ignoreCase = true) ||
                        recipe.description.contains(query, ignoreCase = true)
            }
        }
        adapter.updateRecipes(filteredRecipes)
    }

    private fun updateRecipeInDatabase(recipe: RecipeModel) {
        lifecycleScope.launch {
            try {
                val success = firebaseManager.updateRecipe(recipe)
                if (success) {
                    Toast.makeText(
                        this@TelaPrincipal,
                        if (recipe.isFavorite) "Adicionado aos favoritos" else "Removido dos favoritos",
                        Toast.LENGTH_SHORT
                    ).show()
                    // Atualizar a lista local de receitas
                    val updatedRecipes = allRecipes.map {
                        if (it.id == recipe.id) recipe else it
                    }
                    allRecipes = updatedRecipes
                    adapter.updateRecipes(updatedRecipes)
                } else {
                    Toast.makeText(
                        this@TelaPrincipal,
                        "Erro ao atualizar favorito",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } catch (e: Exception) {
                Log.e("TelaPrincipal", "Erro ao atualizar receita", e)
                Toast.makeText(
                    this@TelaPrincipal,
                    "Erro ao atualizar favorito: ${e.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}

