package com.example.appreceitas

data class RecipeModel(
    val title: String,
    val description: String,
    val imageResId: Int,
    val ingredients: String,
    val instructions: String
)
