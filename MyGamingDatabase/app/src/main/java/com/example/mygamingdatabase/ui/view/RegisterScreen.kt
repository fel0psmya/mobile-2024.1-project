package com.example.mygamingdatabase.ui.view

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Password
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.mygamingdatabase.R
import com.example.mygamingdatabase.data.GameRepository
import com.example.mygamingdatabase.viewmodel.GameViewModel

@Composable
fun RegisterScreen(viewModel: GameViewModel, navController: NavController) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var repeatPassword by remember { mutableStateOf("") }

    var nameError by remember { mutableStateOf(false) }
    var emailError by remember { mutableStateOf(false) }
    var emailFormatError by remember { mutableStateOf(false) }
    var passwordError by remember { mutableStateOf(false) }
    var repeatPasswordError by remember { mutableStateOf(false) }
    var passwordMismatchError by remember { mutableStateOf(false) }

    val context = LocalContext.current

    fun isValidEmail(email: String): Boolean {
        val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"
        return email.matches(emailPattern.toRegex())
    }

    fun validateFields(): Boolean {
        nameError = name.isEmpty()
        emailError = email.isEmpty()
        emailFormatError = !isValidEmail(email)
        passwordError = password.isEmpty() || password.length < 6
        repeatPasswordError = repeatPassword.isEmpty()
        passwordMismatchError = password != repeatPassword

        return !nameError && !emailError && !emailFormatError && !passwordError && !repeatPasswordError && !passwordMismatchError
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.topbar_icon),
                contentDescription = "App Logo",
                modifier = Modifier.size(100.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Cadastre-se para mapear o que você joga!",
                style = MaterialTheme
                    .typography.headlineSmall.copy(fontWeight = FontWeight.Bold)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Name TextField
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Nome") },
                leadingIcon = {
                    Icon(imageVector = Icons.Filled.Person, contentDescription = "Email Icon")
                },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                isError = nameError,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
            )
            if (nameError) {
                Text(
                    text = "Campo obrigatório",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier
                        .align(Alignment.Start)
                        .padding(top = 4.dp)
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            // Possible username textfield

            // Email TextField
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
                leadingIcon = {
                    Icon(imageVector = Icons.Filled.Email, contentDescription = "Email Icon")
                },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                isError = emailError || emailFormatError,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
            )
            if (emailError) {
                Text(
                    text = "Campo obrigatório",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier
                        .align(Alignment.Start)
                        .padding(top = 4.dp)
                )
            } else if (emailFormatError) {
                Text(
                    text = "Email inválido",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier
                        .align(Alignment.Start)
                        .padding(top = 4.dp)
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            // Password TextField
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Senha") },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Password,
                        contentDescription = "Password Icon"
                    )
                },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                isError = passwordError,
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
            )
            if (passwordError) {
                Text(
                    text = "Campo obrigatório e deve ter no mínimo 6 caracteres",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier
                        .align(Alignment.Start)
                        .padding(top = 4.dp)
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            // Repeat Password TextField
            OutlinedTextField(
                value = repeatPassword,
                onValueChange = { repeatPassword = it },
                label = { Text("Repetir Senha") },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Password,
                        contentDescription = "Password Icon"
                    )
                },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                isError = repeatPasswordError || passwordMismatchError,
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
            )
            if (repeatPasswordError) {
                Text(
                    text = "Campo obrigatório",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier
                        .align(Alignment.Start)
                        .padding(top = 4.dp)
                )
            } else if (passwordMismatchError) {
                Text(
                    text = "As senhas não coincidem",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier
                        .align(Alignment.Start)
                        .padding(top = 4.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Register Button
            Button(
                onClick = {
                    Log.d("RegisterScreen", "Register button clicked")
                    if (validateFields()) {
                        Log.d("RegisterScreen", "Fields validated")
                        viewModel.register(email, password, name) { }
                        Log.d("RegisterScreen", "Registration successful, navigating to login")
                        navController.navigate("login") {
                            popUpTo("register") { inclusive = true }
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                shape = MaterialTheme.shapes.medium
            ) {
                Text(text = "Cadastrar", color = Color.White)
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Já tem uma conta? Faça login!",
                modifier = Modifier.clickable {
                    navController.navigate("login") {
                        popUpTo("register") { inclusive = true }
                    }
                },
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.bodyMedium
                    .copy(fontWeight = FontWeight.Bold)
            )
        }

        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Continuar sem login",
                modifier = Modifier.clickable {
                    navController.navigate("home") {
                        popUpTo("register") { inclusive = true }
                    }
                },
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(16.dp))

            // App Version
            Text(
                text = "Version 0.3.0",
                fontSize = 12.sp,
                color = Color.Gray,
                modifier = Modifier
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RegisterScreenPreview() {
    RegisterScreen(GameViewModel(LocalContext.current, GameRepository()), navController = NavController(LocalContext.current))
}