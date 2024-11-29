package com.example.appreceitas

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.google.android.material.appbar.MaterialToolbar
import kotlinx.coroutines.launch

class AddRecipeActivity : AppCompatActivity() {

    private lateinit var etTitle: EditText
    private lateinit var etDescription: EditText
    private lateinit var etIngredients: EditText
    private lateinit var etInstructions: EditText
    private lateinit var etImageUrl: EditText
    private lateinit var btnSave: Button
    private lateinit var toolbar: MaterialToolbar

    private val firebaseManager = FireBaseManager()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_recipe)

        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

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

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
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
                Log.d("AddRecipeActivity", "Recipe saved successfully")
                Toast.makeText(this@AddRecipeActivity, "Receita salva com sucesso!", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Log.e("AddRecipeActivity", "Failed to save recipe")
                Toast.makeText(this@AddRecipeActivity, "Erro ao salvar a receita", Toast.LENGTH_SHORT).show()
            }
        }
    }
}

