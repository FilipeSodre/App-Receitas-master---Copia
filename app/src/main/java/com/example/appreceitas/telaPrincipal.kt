package com.example.appreceitas

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class TelaPrincipal : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.tela_principal)

        val itemList = listOf(
            RecipeModel(
                title = "Pizza Margherita",
                description = "Deliciosa pizza italiana com molho de tomate, mozzarella e manjericão",
                imageResId = R.drawable.pizza_placeholder,
                ingredients = "Massa de pizza, Molho de tomate, Queijo mozzarella, Manjericão fresco, Azeite de oliva",
                instructions = "1. Pré-aqueça o forno a 220°C.\n2. Abra a massa de pizza.\n3. Espalhe o molho de tomate.\n4. Adicione o queijo mozzarella.\n5. Asse por 10-12 minutos.\n6. Adicione folhas de manjericão fresco e um fio de azeite antes de servir."
            ),
            RecipeModel(
                title = "Hambúrguer Clássico",
                description = "Hambúrguer suculento com queijo, alface e tomate",
                imageResId = R.drawable.burguer_placeholder,
                ingredients = "Carne moída, Pão de hambúrguer, Queijo cheddar, Alface, Tomate, Cebola, Ketchup, Mostarda",
                instructions = "1. Tempere a carne moída e forme os hambúrgueres.\n2. Grelhe os hambúrgueres por 3-4 minutos de cada lado.\n3. Toste levemente o pão.\n4. Monte o hambúrguer com queijo, alface, tomate e cebola.\n5. Adicione ketchup e mostarda a gosto."
            ),
            RecipeModel(
                title = "Strogonoff de Frango",
                description = "Cremoso strogonoff de frango com champignons",
                imageResId = R.drawable.strogonoff_placeholder,
                ingredients = "Peito de frango, Creme de leite, Champignons, Cebola, Alho, Ketchup, Mostarda, Sal e pimenta",
                instructions = "1. Corte o frango em cubos e tempere com sal e pimenta.\n2. Refogue a cebola e o alho.\n3. Adicione o frango e cozinhe até dourar.\n4. Acrescente os champignons, ketchup e mostarda.\n5. Por fim, adicione o creme de leite e misture bem.\n6. Sirva com arroz branco e batata palha."
            )
        )

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = RecipesAdapter(itemList)
    }
}

