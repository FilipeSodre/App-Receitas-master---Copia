package com.example.appreceitas

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class FireBaseManager {
    private val db = FirebaseFirestore.getInstance()
    private val recipesCollection = db.collection("recipes")

    suspend fun saveRecipe(recipe: RecipeModel): Boolean {
        return try {
            val documentReference = withContext(Dispatchers.IO) {
                recipesCollection.add(recipe).await()
            }
            Log.d("FireBaseManager", "Receita salva com sucesso. ID: ${documentReference.id}")
            true
        } catch (e: Exception) {
            Log.e("FireBaseManager", "Erro ao salvar receita", e)
            false
        }
    }

    suspend fun updateRecipe(recipe: RecipeModel): Boolean {
        return try {
            withContext(Dispatchers.IO) {
                recipesCollection.document(recipe.id).set(recipe).await()
            }
            Log.d("FireBaseManager", "Receita atualizada com sucesso. ID: ${recipe.id}")
            true
        } catch (e: Exception) {
            Log.e("FireBaseManager", "Erro ao atualizar receita", e)
            false
        }
    }

    fun getRecipesRealtime(onRecipesChanged: (List<RecipeModel>) -> Unit) {
        recipesCollection.addSnapshotListener { snapshot, e ->
            if (e != null) {
                Log.e("FireBaseManager", "Erro ao escutar mudanças", e)
                return@addSnapshotListener
            }

            if (snapshot != null) {
                val recipes = snapshot.documents.mapNotNull { doc ->
                    doc.toObject(RecipeModel::class.java)?.copy(id = doc.id)
                }
                Log.d("FireBaseManager", "Receitas carregadas: ${recipes.size}")
                onRecipesChanged(recipes)
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