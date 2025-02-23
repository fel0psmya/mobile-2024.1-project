package com.example.mygamingdatabase

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log
import android.widget.Toast

fun isInternetAvailable(context: Context): Boolean {
    val connectivityManager = context
        .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    val network = connectivityManager.activeNetwork ?: run {
        Log.d("isInternetAvailable", "No active network")
        return false
    }
    val capabilities = connectivityManager.getNetworkCapabilities(network) ?: run {
        Log.d("isInternetAvailable", "No network capabilities")
        return false
    }

    val hasInternet = capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    val isValidated = capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)

    Log.d("isInternetAvailable", "hasInternet: $hasInternet, isValidated: $isValidated")

    return hasInternet && isValidated
}