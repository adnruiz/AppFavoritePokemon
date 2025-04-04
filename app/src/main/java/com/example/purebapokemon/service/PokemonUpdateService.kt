package com.example.purebapokemon.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.example.purebapokemon.R
import com.example.purebapokemon.domain.usecase.RefreshPokemonListUseCase
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class PokemonUpdateService : Service() {

    @Inject
    lateinit var refreshPokemonListUseCase: RefreshPokemonListUseCase

    private val serviceScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private val CHANNEL_ID = "PokemonUpdateChannel"
    private val NOTIFICATION_ID = 1

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Actualizando Pokémon")
            .setContentText("Descargando información de Pokémon...")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .build()

        startForeground(NOTIFICATION_ID, notification)

        serviceScope.launch {
            try {
                // Actualizamos los primeros 100 Pokémon
                refreshPokemonListUseCase(100, 0)

                // Enviamos un broadcast cuando la actualización se completa
                sendUpdateCompletedBroadcast(true, null)
            } catch (e: Exception) {
                sendUpdateCompletedBroadcast(false, e.message)
            } finally {
                stopForeground(true)
                stopSelf()
            }
        }

        return START_NOT_STICKY
    }

    private fun sendUpdateCompletedBroadcast(success: Boolean, error: String?) {
        val intent = Intent(ACTION_UPDATE_COMPLETED)
        intent.putExtra(EXTRA_SUCCESS, success)
        error?.let { intent.putExtra(EXTRA_ERROR, it) }
        sendBroadcast(intent)
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Pokemon Updates"
            val descriptionText = "Notificaciones para actualizaciones de Pokémon"
            val importance = NotificationManager.IMPORTANCE_LOW
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onDestroy() {
        super.onDestroy()
        serviceScope.cancel()
    }

    companion object {
        const val ACTION_UPDATE_COMPLETED = "com.example.purebapokemon.ACTION_UPDATE_COMPLETED"
        const val EXTRA_SUCCESS = "extra_success"
        const val EXTRA_ERROR = "extra_error"
    }
}