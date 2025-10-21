package com.example.clinicaveterinariaapp

import android.util.Patterns
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * ViewModel que contiene la lógica de validación y estado para el login.
 * La UI debe consumir estos estados y no realizar validaciones directamente.
 */
class LoginViewModel : ViewModel() {
    var email by mutableStateOf("")
        private set

    var password by mutableStateOf("")
        private set

    var emailError by mutableStateOf<String?>(null)
        private set

    var passwordError by mutableStateOf<String?>(null)
        private set

    var isLoading by mutableStateOf(false)
        private set

    fun onEmailChange(new: String) {
        email = new
        if (emailError != null) validateEmail()
    }

    fun onPasswordChange(new: String) {
        password = new
        if (passwordError != null) validatePassword()
    }

    private fun validateEmail(): Boolean {
        return when {
            email.isBlank() -> {
                emailError = "El correo es obligatorio"
                false
            }
            !Patterns.EMAIL_ADDRESS.matcher(email).matches() -> {
                emailError = "Formato de correo inválido"
                false
            }
            else -> {
                emailError = null
                true
            }
        }
    }

    private fun validatePassword(): Boolean {
        return when {
            password.isBlank() -> {
                passwordError = "La contraseña es obligatoria"
                false
            }
            password.length < 6 -> {
                passwordError = "La contraseña debe tener al menos 6 caracteres"
                false
            }
            else -> {
                passwordError = null
                true
            }
        }
    }

    fun validateAll(): Boolean {
        val e = validateEmail()
        val p = validatePassword()
        return e && p
    }

    /**
     * Simula un inicio de sesión asíncrono.
     * Reemplazar la lógica interna por la autenticación real.
     */
    fun login(onResult: (success: Boolean, message: String?) -> Unit) {
        if (!validateAll()) {
            onResult(false, "Corrige los errores")
            return
        }

        isLoading = true
        viewModelScope.launch {
            delay(800)
            isLoading = false

            // Credenciales de ejemplo
            if (email == "test@vet.com" && password == "123456") {
                onResult(true, null)
            } else {
                onResult(false, "Credenciales inválidas")
            }
        }
    }
}

