package com.example.appreceitas

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class RecipesAdapter(private val recipes: List<RecipeModel>) : RecyclerView.Adapter<RecipesAdapter.RecipeViewHolder>() {

    class RecipeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageButton: ImageView = itemView.findViewById(R.id.ivRecipe)
        val title: TextView = itemView.findViewById(R.id.tvTitle)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.conteudo_reciclerview, parent, false)
        return RecipeViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecipeViewHolder, position: Int) {
        val recipe = recipes[position]
        holder.title.text = recipe.title
        holder.imageButton.setImageResource(recipe.imageResId)

        holder.itemView.setOnClickListener {
            val context = holder.itemView.context
            val intent = Intent(context, RecipeDetailActivity::class.java).apply {
                putExtra("RECIPE_TITLE", recipe.title)
                putExtra("RECIPE_DESCRIPTION", recipe.description)
                putExtra("RECIPE_INGREDIENTS", recipe.ingredients)
                putExtra("RECIPE_INSTRUCTIONS", recipe.instructions)
            }
            context.startActivity(intent)
        }
    }

    override fun getItemCount() = recipes.size
}

