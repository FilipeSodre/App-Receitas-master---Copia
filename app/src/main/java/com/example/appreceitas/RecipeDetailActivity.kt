package com.example.appreceitas

import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.floatingactionbutton.FloatingActionButton


class RecipeDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recipe_detail)

        // Recuperar os dados da receita passados pela intent
        val recipeTitle = intent.getStringExtra("RECIPE_TITLE") ?: "Receita"
        val recipeDescription = intent.getStringExtra("RECIPE_DESCRIPTION") ?: ""
        val recipeIngredients = intent.getStringExtra("RECIPE_INGREDIENTS") ?: ""
        val recipeInstructions = intent.getStringExtra("RECIPE_INSTRUCTIONS") ?: ""

        // Encontrar as views pelo ID
        val tvRecipeTitle = findViewById<TextView>(R.id.tvRecipeTitle)
        val tvRecipeDescription = findViewById<TextView>(R.id.tvRecipeDescription)
        val tvIngredients = findViewById<TextView>(R.id.tvIngredients)
        val tvInstructions = findViewById<TextView>(R.id.tvInstructions)
        val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)
        val fabFavorite = findViewById<FloatingActionButton>(R.id.fabFavorite)

        // Configurar a Toolbar
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // Preencher os dados na tela
        tvRecipeTitle.text = recipeTitle
        tvRecipeDescription.text = recipeDescription
        tvIngredients.text = recipeIngredients
        tvInstructions.text = recipeInstructions

        // Configurar o FAB para adicionar aos favoritos
        fabFavorite.setOnClickListener {
            Snackbar.make(it, "Adicionado aos favoritos", Snackbar.LENGTH_SHORT).show()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}