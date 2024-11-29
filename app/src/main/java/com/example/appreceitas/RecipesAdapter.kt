package com.example.appreceitas

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class RecipesAdapter(
    var recipes: List<RecipeModel>,
    private val onFavoriteClick: (RecipeModel) -> Unit,
    private val onItemClick: (RecipeModel) -> Unit
) : RecyclerView.Adapter<RecipesAdapter.RecipeViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.conteudo_reciclerview, parent, false)
        return RecipeViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecipeViewHolder, position: Int) {
        val recipe = recipes[position]
        holder.bind(recipe)
    }

    override fun getItemCount() = recipes.size


    fun updateRecipes(newRecipes: List<RecipeModel>) {
        recipes = newRecipes
        notifyDataSetChanged()
    }

    inner class RecipeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val ivRecipe: ImageView = itemView.findViewById(R.id.ivRecipe)
        private val tvTitle: TextView = itemView.findViewById(R.id.tvTitle)
        private val tvDescription: TextView = itemView.findViewById(R.id.tvDescription)
        private val btnFavorite: ImageButton = itemView.findViewById(R.id.btnFavorite)

        fun bind(recipe: RecipeModel) {
            tvTitle.text = recipe.title
            tvDescription.text = recipe.description

            // carrega a imagem da receita usando
            Glide.with(itemView.context)
                .load(recipe.imageUrl)
                .placeholder(R.drawable.pizza_placeholder)
                .into(ivRecipe)

            // muda o ícone de favorito baseado no status da receita
            val favoriteIcon = if (recipe.isFavorite) R.drawable.ic_favorite_red else R.drawable.ic_favorite
            btnFavorite.setImageResource(favoriteIcon)

            btnFavorite.setOnClickListener { onFavoriteClick(recipe) }
            itemView.setOnClickListener { onItemClick(recipe) }
        }
    }
}