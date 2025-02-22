package com.example.mygamingdatabase.data

import android.content.Context
import android.util.Log
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class GameRepository {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val realtimeDatabase: FirebaseDatabase = FirebaseDatabase.getInstance()


    // Registro de usuário
    suspend fun registerUser(email: String, password: String, name: String): Boolean {
        return try {
            Log.d("GameRepository", "Attempting to create user")

            val result = auth.createUserWithEmailAndPassword(email, password).await()
            val uid = result.user?.uid

            if (uid != null) {
                Log.d("GameRepository", "User created: $uid")

                val user = hashMapOf(
                    "uid" to uid,
                    "name" to name,
                    "email" to email,
                    "created_at" to System.currentTimeMillis()
                )
                firestore.collection("users").document(uid).set(user).await()
                initializeUserData(uid)
            }
            true
        } catch (e: Exception) {
            Log.e("GameRepository", "Erro no cadastro: ${e.message}")
            false
        }
    }

    // Login com email e senha
    suspend fun loginUser(email: String, password: String): Boolean {
        return try {
            auth.signInWithEmailAndPassword(email, password).await()
            true
        } catch (e: Exception) {
            Log.e("GameRepository", "Erro no login: ${e.message}")
            false
        }
    }

    suspend fun resetPassword(email: String): Boolean {
        return try {
            auth.sendPasswordResetEmail(email).await()
            true
        } catch (e: Exception) {
            Log.e("GameRepository", "Erro ao enviar email de recuperação: ${e.message}")
            false
        }
    }

    suspend fun getUserName(): String? {
        return try {
            val uid = auth.currentUser?.uid
            if (uid != null) {
                val snapshot = firestore.collection("users").document(uid).get().await()
                snapshot.getString("name") // Retorna o nome salvo no Firestore
            } else {
                null
            }
        } catch (e: Exception) {
            Log.e("GameRepository", "Erro ao buscar nome do usuário: ${e.message}")
            null
        }
    }

    // Login com Google
    fun getGoogleSignInClient(context: Context): GoogleSignInClient {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(context.getString(
                com.example.mygamingdatabase.R.string.default_web_client_id))
            .requestEmail()
            .build()
        return GoogleSignIn.getClient(context, gso)
    }

    suspend fun loginWithGoogle(idToken: String): Boolean {
        return try {
            val credential = GoogleAuthProvider.getCredential(idToken, null)
            val result = auth.signInWithCredential(credential).await()
            val user = result.user

            user?.let {
                val uid = it.uid
                val name = it.displayName ?: "Usuário"
                val email = it.email ?: ""

                // Verifica se o usuário já existe no Firestore antes de salvar
                val userRef = firestore.collection("users").document(uid)
                val snapshot = userRef.get().await()

                if (!snapshot.exists()) {
                    val userData = hashMapOf(
                        "uid" to uid,
                        "name" to name,
                        "email" to email,
                        "created_at" to System.currentTimeMillis()
                    )
                    userRef.set(userData).await()
                    initializeUserData(uid)
                }
            }
            true
        } catch (e: Exception) {
            Log.e("AuthRepository", "Erro no login Google: ${e.message}")
            false
        }
    }

    // Logout
    fun logout() {
        auth.signOut()
    }

    // Verifica se o usuário está logado
    fun isUserLogged(): Boolean {
        return auth.currentUser != null
    }
    // Inicializa os dados do usuário no Realtime Database
    private fun initializeUserData(userId: String) {
        val userRef = realtimeDatabase.reference.child("users").child(userId)

        val userData = mapOf(
            "favorites" to emptyList<Int>(),
            "games" to emptyMap<Int, Map<String, Any>>(),
            "reminders" to emptyList<Int>()
        )

        userRef.setValue(userData)
    }
}