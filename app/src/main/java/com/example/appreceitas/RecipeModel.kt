package com.example.appreceitas

data class RecipeModel(
    val id: String = "",
    val title: String = "",
    val description: String = "",
    val ingredients: String = "",
    val instructions: String = "",
    val imageUrl: String = "",
    var isFavorite: Boolean = false
)