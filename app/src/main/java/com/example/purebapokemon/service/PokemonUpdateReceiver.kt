package com.example.purebapokemon.service

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast

class PokemonUpdateReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == PokemonUpdateService.ACTION_UPDATE_COMPLETED) {
            val success = intent.getBooleanExtra(PokemonUpdateService.EXTRA_SUCCESS, false)
            if (success) {
                Toast.makeText(context, "¡Actualización de Pokémon completada!", Toast.LENGTH_SHORT).show()
            } else {
                val error = intent.getStringExtra(PokemonUpdateService.EXTRA_ERROR)
                Toast.makeText(context, "Error en la actualización: $error", Toast.LENGTH_LONG).show()
            }
        }
    }
}