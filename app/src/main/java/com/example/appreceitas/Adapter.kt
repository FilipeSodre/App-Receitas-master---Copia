package com.example.appreceitas

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class Adapter(private val itemList: List<String>) : RecyclerView.Adapter<Adapter.ItemViewHolder>() {

    // guarda referencias para as views
    class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.tvTitle)
    }
    // icria o viewholder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.conteudo_reciclerview   , parent, false)
        return ItemViewHolder(view)
    }
    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.title.text = itemList[position]
    }

    // informa quantos itens tem na lista
    override fun getItemCount(): Int {
        return itemList.size
    }
}
