package com.example.pokeapirecycle

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.pokeapirecycle.R

class PokeAdapter(private val pokeList: List<SimplePokemon>) :
    RecyclerView.Adapter<PokeAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ivSprite: ImageView = itemView.findViewById(R.id.ivSprite)
        val tvName: TextView = itemView.findViewById(R.id.tvName)
        val tvId: TextView = itemView.findViewById(R.id.tvId)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.poke_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val pokemon = pokeList[position]
        holder.tvName.text = pokemon.name.replaceFirstChar { it.uppercase() }

        // Recommended fix for warning
        holder.tvId.text = holder.itemView.context.getString(R.string.pokemon_id, pokemon.id)

        Glide.with(holder.itemView.context)
            .load(pokemon.imageURL)
            .into(holder.ivSprite)
    }

    override fun getItemCount() = pokeList.size
}