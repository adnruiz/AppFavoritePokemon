package com.example.purebapokemon.ui.pokemondetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.purebapokemon.R
import com.example.purebapokemon.databinding.FragmentPokemonDetailBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PokemonDetailFragment : Fragment() {

    private var _binding: FragmentPokemonDetailBinding? = null
    private val binding get() = _binding!!

    private val viewModel: PokemonDetailViewModel by viewModels()
    private val args: PokemonDetailFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPokemonDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeViewModel()
        setupListeners()

        // Cargamos los datos del Pokémon usando el ID recibido de los argumentos
        viewModel.loadPokemon(args.pokemonId)
    }

    private fun setupListeners() {
        binding.cbFavorite.setOnCheckedChangeListener { _, _ ->
            viewModel.toggleFavorite()
        }
    }

    private fun observeViewModel() {
        viewModel.pokemon.observe(viewLifecycleOwner) { pokemon ->
            binding.tvPokemonName.text = pokemon.name
            binding.tvTypes.text = pokemon.types.joinToString(", ")
            binding.cbFavorite.isChecked = pokemon.isFavorite

            Glide.with(requireContext())
                .load(pokemon.imageUrl)
                .placeholder(R.drawable.ic_launcher_foreground)
                .into(binding.ivPokemonImage)
        }

        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }

        viewModel.error.observe(viewLifecycleOwner) { errorMessage ->
            Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_LONG).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}