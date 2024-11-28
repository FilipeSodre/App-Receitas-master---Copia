package com.example.appreceitas

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.android.material.appbar.MaterialToolbar

class RecipeDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recipe_detail)

        val toolbar: MaterialToolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val recipeTitle = intent.getStringExtra("RECIPE_TITLE") ?: "Receita"
        val recipeDescription = intent.getStringExtra("RECIPE_DESCRIPTION") ?: ""
        val recipeIngredients = intent.getStringExtra("RECIPE_INGREDIENTS") ?: ""
        val recipeInstructions = intent.getStringExtra("RECIPE_INSTRUCTIONS") ?: ""
        val recipeImageUrl = intent.getStringExtra("RECIPE_IMAGE_URL") ?: ""

        supportActionBar?.title = recipeTitle

        val ivRecipeImage: ImageView = findViewById(R.id.ivRecipeImage)
        val tvRecipeDescription: TextView = findViewById(R.id.tvRecipeDescription)
        val tvIngredients: TextView = findViewById(R.id.tvIngredients)
        val tvInstructions: TextView = findViewById(R.id.tvInstructions)

        Glide.with(this)
            .load(recipeImageUrl)
            .placeholder(R.drawable.pizza_placeholder)
            .into(ivRecipeImage)

        tvRecipeDescription.text = recipeDescription
        tvIngredients.text = recipeIngredients
        tvInstructions.text = recipeInstructions
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}