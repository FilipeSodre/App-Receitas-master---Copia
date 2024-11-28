package com.example.appreceitas

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class FireBaseManager {
    private val db = FirebaseFirestore.getInstance()
    private val recipesCollection = db.collection("recipes")

    suspend fun updateRecipe(recipe: RecipeModel): Boolean {
        return try {
            withContext(Dispatchers.IO) {
                val updateData = mapOf(
                    "isFavorite" to recipe.isFavorite,
                    "title" to recipe.title,
                    "description" to recipe.description,
                    "ingredients" to recipe.ingredients,
                    "instructions" to recipe.instructions,
                    "imageUrl" to recipe.imageUrl
                )
                recipesCollection.document(recipe.id).update(updateData).await()
                Log.d("FireBaseManager", "Receita atualizada com sucesso. ID: ${recipe.id}, Favorito: ${recipe.isFavorite}")
                true
            }
        } catch (e: Exception) {
            Log.e("FireBaseManager", "Erro ao atualizar receita", e)
            false
        }
    }

    suspend fun saveRecipe(recipe: RecipeModel): Boolean {
        return try {
            withContext(Dispatchers.IO) {
                val documentReference = recipesCollection.add(recipe).await()
                val updatedRecipe = recipe.copy(id = documentReference.id)
                recipesCollection.document(documentReference.id).set(updatedRecipe).await()
                Log.d("FireBaseManager", "Receita salva com sucesso. ID: ${documentReference.id}")
                true
            }
        } catch (e: Exception) {
            Log.e("FireBaseManager", "Erro ao salvar receita", e)
            false
        }
    }

    fun getRecipesRealtime(onRecipesChanged: (List<RecipeModel>) -> Unit) {
        recipesCollection
            .addSnapshotListener { snapshot, e ->
                if (e != null) {
                    Log.e("FireBaseManager", "Error fetching recipes", e)
                    return@addSnapshotListener
                }

                if (snapshot != null) {
                    val recipes = snapshot.documents.mapNotNull { doc ->
                        try {
                            val recipe = doc.toObject(RecipeModel::class.java)?.copy(id = doc.id)
                            Log.d("FireBaseManager", "Fetched recipe: ${recipe?.title}")
                            recipe
                        } catch (e: Exception) {
                            Log.e("FireBaseManager", "Error converting document", e)
                            null
                        }
                    }
                    Log.d("FireBaseManager", "Total recipes fetched: ${recipes.size}")
                    onRecipesChanged(recipes)
                } else {
                    Log.d("FireBaseManager", "No recipes found")
                    onRecipesChanged(emptyList())
                }
            }
    }

    fun getFavoriteRecipesRealtime(onRecipesChanged: (List<RecipeModel>) -> Unit) {
        recipesCollection.whereEqualTo("isFavorite", true)
            .addSnapshotListener { snapshot, e ->
                if (e != null) {
                    Log.e("FireBaseManager", "Erro ao escutar mudanças nos favoritos", e)
                    return@addSnapshotListener
                }

                if (snapshot != null) {
                    val recipes = snapshot.documents.mapNotNull { doc ->
                        doc.toObject(RecipeModel::class.java)?.copy(id = doc.id)
                    }
                    Log.d("FireBaseManager", "Receitas favoritas carregadas: ${recipes.size}")
                    onRecipesChanged(recipes)
                }
            }
    }
}