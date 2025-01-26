package com.example.mygamingdatabase.models

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mygamingdatabase.DataStoreUtils

class GameViewModel (context: Context) : ViewModel() {
    // Lê o tema do DataStore
    val themeFlow: Flow<Boolean> = DataStoreUtils.readTheme(context)
    private val _isDarkTheme = MutableStateFlow(false)
    val isDarkTheme: StateFlow<Boolean> = _isDarkTheme

    init {
        viewModelScope.launch {
            // Carrega as tarefas salvas do DataStore
            try {
                themeFlow.collect { savedTheme ->
                    _isDarkTheme.value = savedTheme
                }
            } catch (e: Exception) {
                // Logar erro ou tratar de alguma maneira
                e.printStackTrace()
            }
        }
    }

    fun toggleTheme(context: Context) {
        viewModelScope.launch {
            val newTheme = !_isDarkTheme.value
            _isDarkTheme.value = newTheme
            DataStoreUtils.saveTheme(context, newTheme)
        }
    }
}