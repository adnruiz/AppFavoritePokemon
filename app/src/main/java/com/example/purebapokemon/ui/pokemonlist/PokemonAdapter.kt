package com.example.purebapokemon.ui.pokemonlist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.purebapokemon.R
import com.example.purebapokemon.domain.model.Pokemon

class PokemonAdapter(
    private val onPokemonClick: (Pokemon) -> Unit,
    private val onFavoriteClick: (Pokemon) -> Unit,
) : ListAdapter<Pokemon, PokemonAdapter.PokemonViewHolder>(PokemonDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PokemonViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_pokemon, parent, false)
        return PokemonViewHolder(view)
    }

    override fun onBindViewHolder(holder: PokemonViewHolder, position: Int) {
        val pokemon = getItem(position)
        holder.bind(pokemon)
    }

    inner class PokemonViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val ivPokemon: ImageView = itemView.findViewById(R.id.ivPokemon)
        private val tvPokemonName: TextView = itemView.findViewById(R.id.tvPokemonName)
        private val tvPokemonTypes: TextView = itemView.findViewById(R.id.tvPokemonTypes)
        private val ivFavorite: ImageView = itemView.findViewById(R.id.ivFavorite)

        fun bind(pokemon: Pokemon) {
            tvPokemonName.text = pokemon.name
            tvPokemonTypes.text = pokemon.types.joinToString(", ")

            Glide.with(itemView.context)
                .load(pokemon.imageUrl)
                .centerCrop()
                .placeholder(R.drawable.ic_launcher_foreground)
                .into(ivPokemon)

            ivFavorite.setImageResource(
                if (pokemon.isFavorite) R.drawable.ic_favorite_filled
                else R.drawable.ic_favorite_border
            )

            itemView.setOnClickListener { onPokemonClick(pokemon) }
            ivFavorite.setOnClickListener { onFavoriteClick(pokemon) }
        }
    }

    class PokemonDiffCallback : DiffUtil.ItemCallback<Pokemon>() {
        override fun areItemsTheSame(oldItem: Pokemon, newItem: Pokemon): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Pokemon, newItem: Pokemon): Boolean {
            return oldItem == newItem
        }
    }
}