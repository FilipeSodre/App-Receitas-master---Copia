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
            Log.d("FireBaseManager", "Iniciando salvamento da receita: ${recipe.title}")
            val documentReference = withContext(Dispatchers.IO) {
                recipesCollection.add(recipe).await()
            }
            Log.d("FireBaseManager", "Receita salva com sucesso. ID: ${documentReference.id}")
            true
        } catch (e: Exception) {
            Log.e("FireBaseManager", "Erro ao salvar receita", e)
            Log.e("FireBaseManager", "Mensagem de erro: ${e.message}")
            Log.e("FireBaseManager", "Causa: ${e.cause}")
            e.printStackTrace()
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
}