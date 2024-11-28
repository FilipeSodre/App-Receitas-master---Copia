package com.example.appreceitas

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch

class AddRecipeActivity : AppCompatActivity() {

    private lateinit var etTitle: EditText
    private lateinit var etDescription: EditText
    private lateinit var etIngredients: EditText
    private lateinit var etInstructions: EditText
    private lateinit var etImageUrl: EditText
    private lateinit var btnSave: Button

    private val firebaseManager = FireBaseManager()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_recipe)

        etTitle = findViewById(R.id.etTitle)
        etDescription = findViewById(R.id.etDescription)
        etIngredients = findViewById(R.id.etIngredients)
        etInstructions = findViewById(R.id.etInstructions)
        etImageUrl = findViewById(R.id.etImageUrl)
        btnSave = findViewById(R.id.btnSave)

        btnSave.setOnClickListener {
            saveRecipe()
        }
    }

    private fun saveRecipe() {
        val title = etTitle.text.toString().trim()
        val description = etDescription.text.toString().trim()
        val ingredients = etIngredients.text.toString().trim()
        val instructions = etInstructions.text.toString().trim()
        val imageUrl = etImageUrl.text.toString().trim()

        if (title.isEmpty() || description.isEmpty() || ingredients.isEmpty() || instructions.isEmpty() || imageUrl.isEmpty()) {
            Toast.makeText(this, "Por favor, preencha todos os campos", Toast.LENGTH_SHORT).show()
            return
        }

        val newRecipe = RecipeModel(
            title = title,
            description = description,
            ingredients = ingredients,
            instructions = instructions,
            imageUrl = imageUrl
        )

        lifecycleScope.launch {
            val success = firebaseManager.saveRecipe(newRecipe)
            if (success) {
                Toast.makeText(this@AddRecipeActivity, "Receita salva com sucesso!", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(this@AddRecipeActivity, "Erro ao salvar a receita", Toast.LENGTH_SHORT).show()
            }
        }
    }
}