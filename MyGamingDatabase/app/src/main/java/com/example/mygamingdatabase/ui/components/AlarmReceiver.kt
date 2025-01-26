package com.example.mygamingdatabase.ui.components

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.core.app.NotificationCompat
import com.example.mygamingdatabase.MainActivity
import com.example.mygamingdatabase.models.Game
import com.example.mygamingdatabase.models.gameList

class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        Toast.makeText(context, "Alarme Disparado!", Toast.LENGTH_LONG).show()

        // Recuperar o nome do jogo do Intent
        val gameId = intent.getIntExtra("GAME_ID", -1)
        Log.d("AlarmReceiver", "Alarme acionado para o GAME_ID: $gameId")

        if (gameId != -1) {
            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            // Create notification channel
            createNotificationChannel(context)

            // Intent to open the app when clicking the notification
            val notificationIntent = Intent(context, MainActivity::class.java)
            val pendingIntent = PendingIntent.getActivity(
                context,
                gameId,
                notificationIntent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )

            val gameName = getGameById(gameId)?.name ?: "um jogo incrível"

            // Build notification
            val notification = NotificationCompat.Builder(context, "alarm_channel")
                .setSmallIcon(android.R.drawable.ic_lock_idle_alarm)
                .setContentTitle("Hora de jogar!")
                .setContentText("É hora de jogar ${gameName}!") // Chamar o nome do jogo
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .build()

            // Exibir a notificação
            notificationManager.notify(1, notification)
        }
    }

    private fun createNotificationChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Alarm Notification"
            val descriptionText = "Canal para notificações de lembretes de jogos"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel("alarm_channel", name, importance).apply {
                description = descriptionText
            }
            val notificationManager: NotificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun getGameById(gameId: Int): Game? { // Provisório
        return gameList.find { it.id == gameId }
    }
}