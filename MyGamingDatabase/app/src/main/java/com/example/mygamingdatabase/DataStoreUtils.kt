package com.example.mygamingdatabase

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

val Context.dataStore by preferencesDataStore(name = "user_prefs")

object DataStoreUtils {
    private val GAMES_IN_LIST_KEY = stringPreferencesKey("games_in_list")
    private val FAVORITE_GAMES_KEY = stringPreferencesKey("favorite_games")

    private val THEME_KEY = booleanPreferencesKey("is_dark_theme")

    /*

    // Salva favoritos no DataStore
    suspend fun saveFavorites(context: Context, favorites: String) {
        try {
            context.dataStore.edit { prefs ->
                prefs[FAVORITE_GAMES_KEY] = favorites
            }
        } catch (e: Exception) {
            // Log de erro ou tratamento personalizado
            println("Erro ao salvar os favoritos: ${e.message}")
        }
    }

    // Lê favoritos do DataStore
    fun readFavorites(context: Context): Flow<String> = context.dataStore.data.map { prefs ->
        prefs[FAVORITE_GAMES_KEY] ?: ""
    }

    // Serializa uma lista de favoritos para string JSON
    fun serializeFavorites(tasks: List<Favorites>): String {
        return try {
            Json.encodeToString(favorites)
        } catch (e: Exception) {
            // Log de erro ou tratamento personalizado
            println("Erro na serialização dos favoritos: ${e.message}")
            ""
        }
    }

    // Desserializa uma string JSON para uma lista de favoritos
    fun deserializeFavorites(serializedFavorites: String): List<Favorites> {
        return try {
            if (serializedFavorites.isNotEmpty()) {
                Json.decodeFromString(serializedFavorites)
            } else {
                emptyList()
            }
        } catch (e: Exception) {
            // Log de erro ou tratamento personalizado
            println("Erro na desserialização dos favoritos: ${e.message}")
            emptyList()
        }
    } */

    // Salva o tema no DataStore
    suspend fun saveTheme(context: Context, isDark: Boolean) {
        context.dataStore.edit { prefs ->
            prefs[THEME_KEY] = isDark
        }
    }

    // Lê o tema no DataStore
    fun readTheme(context: Context): Flow<Boolean> = context.dataStore.data.map { prefs ->
        prefs[THEME_KEY] ?: false
    }
}