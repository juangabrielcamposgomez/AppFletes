package com.oriundo.appfletes.viewmodels


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class ProfileUiState(
    val email: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val loginError: String? = null
    // Puedes agregar más estados aquí según las necesidades de tu pantalla de perfil
)

class ProfileViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState

    fun onEmailChanged(newEmail: String) {
        _uiState.update { currentState ->
            currentState.copy(email = newEmail)
        }
    }

    fun onPasswordChanged(newPassword: String) {
        _uiState.update { currentState ->
            currentState.copy(password = newPassword)
        }
    }

    fun login() {
        _uiState.update { currentState ->
            currentState.copy(isLoading = true, loginError = null)
        }
        // Aquí deberías llamar a tu repositorio o caso de uso para realizar la autenticación
        viewModelScope.launch {
            // Simulación de una llamada de inicio de sesión
            kotlinx.coroutines.delay(2000)
            if (_uiState.value.email == "test@example.com" && _uiState.value.password == "password") {
                // Inicio de sesión exitoso: puedes actualizar el estado para navegar, etc.
                _uiState.update { currentState ->
                    currentState.copy(isLoading = false)
                }
                // Aquí podrías emitir un evento para indicar que la navegación debe ocurrir
            } else {
                // Error de inicio de sesión
                _uiState.update { currentState ->
                    currentState.copy(
                        isLoading = false,
                        loginError = "Correo electrónico o contraseña incorrectos."
                    )
                }
            }
        }
    }

    // Puedes agregar más funciones para manejar otras acciones en la pantalla de perfil
}