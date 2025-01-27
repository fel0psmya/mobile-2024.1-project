package com.example.mygamingdatabase.ui.components

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.material3.AlertDialog
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Text
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import com.example.mygamingdatabase.AlarmReceiver
import com.example.mygamingdatabase.models.Game
import java.util.Calendar


@Composable
fun AlarmReminderDialog(
    game: Game,
    onDismissRequest: () -> Unit,
    onConfirm: (hour: Int, minute: Int, days: Set<String>) -> Unit
) {
    val context = LocalContext.current

    var hour by remember { mutableStateOf(19) }
    var minute by remember { mutableStateOf(0) }

    val daysOfWeek = listOf("DOM", "SEG", "TER", "QUA", "QUI", "SEX", "SAB")
    val selectedDays = remember { mutableStateOf(setOf<String>()) }

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S && !canScheduleExactAlarms(context)) {
        requestExactAlarmPermission(context)
        Toast.makeText(context, "Permissão necessária para configurar alarmes exatos.", Toast.LENGTH_SHORT).show()
        return
    }

    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = { Text("Definir lembrete") },
        text = {
            Column {
                // Time Picker Section
                Row(
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    // Hour Picker
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        IconButton(onClick = {
                            hour = (hour + 1) % 24
                        }) {
                            Icon(
                                imageVector = Icons.Default.KeyboardArrowUp,
                                contentDescription = "Aumentar Horas"
                            )
                        }
                        Text(
                            text = hour.toString().padStart(2, '0'),
                            style = MaterialTheme.typography.bodyLarge
                        )
                        IconButton(onClick = {
                            hour = (hour - 1).takeIf { it >= 0 } ?: 23
                        }) {
                            Icon(
                                imageVector = Icons.Default.KeyboardArrowDown,
                                contentDescription = "Diminuir horas"
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    Text(
                        text = ":",
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.align(Alignment.CenterVertically)
                    )

                    Spacer(modifier = Modifier.width(16.dp))

                    // Minute Picker
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        IconButton(onClick = {
                            minute = (minute + 1) % 60
                        }) {
                            Icon(
                                imageVector = Icons.Default.KeyboardArrowUp,
                                contentDescription = "Aumentar Minutos"
                            )
                        }
                        Text(
                            text = minute.toString().padStart(2, '0'),
                            style = MaterialTheme.typography.bodyLarge
                        )
                        IconButton(onClick = {
                            minute = (minute - 1).takeIf { it >= 0 } ?: 59
                        }) {
                            Icon(
                                imageVector = Icons.Default.KeyboardArrowDown,
                                contentDescription = "Diminuir Minutos"
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Seleção de dias
                // Text("Dias da semana:")

                Row(
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    daysOfWeek.forEach { day ->
                        val isSelected = selectedDays.value.contains(day)
                        val btnBackgroundColor by animateColorAsState(
                            targetValue =  if (isSelected) MaterialTheme.colorScheme.primary else Color.Transparent, // Alterando entre cor de destaque e transparente
                            animationSpec = tween(durationMillis = 400) // Duração da animação
                        )
                        val btnTextColor by animateColorAsState(
                            targetValue =  if (isSelected) Color.White else MaterialTheme.colorScheme.primary,
                            animationSpec = tween(durationMillis = 400)
                        )
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .clip(CircleShape)
                                .background(
                                    btnBackgroundColor
                                )
                                .border(
                                    width = 1.dp,
                                    color = MaterialTheme.colorScheme.primary,
                                    shape = CircleShape
                                )
                                .clickable {
                                    if (isSelected) selectedDays.value -= day else selectedDays.value += day
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = day,
                                color = btnTextColor,
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    onConfirm(hour, minute, selectedDays.value)

                    scheduleAlarm(context, hour, minute, selectedDays.value, game.id)

                    Log.d("AlarmReminderDialog", "Dias: ${game.reminderDays.value}, Horário: ${game.reminderTime.value}, Lembrado: ${game.isReminded.value}")
                },
                enabled = selectedDays.value.isNotEmpty() && canScheduleExactAlarms(context)
            ) {
                Text("Confirmar", color = Color.White)
            }
        },
        dismissButton = {
            Button(onClick = onDismissRequest) {
                Text("Cancelar", color = Color.White)
            }
        }
    )
}

@SuppressLint("DefaultLocale")
fun scheduleAlarm(context: Context, hour: Int, minute: Int, days: Set<String>, gameId: Int) {
    val calendar = Calendar.getInstance()
    calendar.set(Calendar.HOUR_OF_DAY, hour)
    calendar.set(Calendar.MINUTE, minute)
    calendar.set(Calendar.SECOND, 0)

    val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    // Verifica quais dias da semana o alarme será repetido
    val daysOfWeek = mapOf(
        "SEG" to Calendar.MONDAY,
        "TER" to Calendar.TUESDAY,
        "QUA" to Calendar.WEDNESDAY,
        "QUI" to Calendar.THURSDAY,
        "SEX" to Calendar.FRIDAY,
        "SAB" to Calendar.SATURDAY,
        "DOM" to Calendar.SUNDAY
    )

    // Formatar hora e minuto para sempre ter dois dígitos
    val formattedHour = String.format("%02d", hour)
    val formattedMinute = String.format("%02d", minute)
    val formattedDays = days.joinToString(", ")

    days.forEach { day ->
        val alarmIntent = Intent(context, AlarmReceiver::class.java)
        alarmIntent.putExtra("GAME_ID", gameId) // Enviar o ID do jogo

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            gameId,
            alarmIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        calendar.set(Calendar.DAY_OF_WEEK, daysOfWeek[day] ?: Calendar.SUNDAY)

        // Agendar o alarme para o horário e dia escolhido

        alarmManager.setExact( // Como estamos trabalhando com alarmes recorrentes, não usaremos alarmes exatos por enquanto
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            //AlarmManager.INTERVAL_DAY * 7, // Repetir semanalmente
            pendingIntent
        )

        Log.d("Alarme", "Alarme configurado para: ${calendar.timeInMillis}")
    }

    Toast.makeText(context, "Alarme configurado para $formattedDays  às ${formattedHour}:${formattedMinute}", Toast.LENGTH_SHORT).show()
}

private fun canScheduleExactAlarms(context: Context): Boolean {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.canScheduleExactAlarms()
    } else {
        true
    }
}

private fun requestExactAlarmPermission(context: Context) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        val intent = Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM)
        context.startActivity(intent)
    }
}