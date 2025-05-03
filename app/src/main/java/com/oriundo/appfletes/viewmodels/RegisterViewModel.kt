package com.oriundo.appfletes.viewmodels


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class RegisterUiState(
    val email: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val accountType: String = "Usuario",
    val isLoading: Boolean = false,
    val registrationError: String? = null,
    val passwordMismatchError: Boolean = false
    // Puedes agregar más estados aquí según las necesidades de tu pantalla de registro
)

class RegisterViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(RegisterUiState())
    val uiState: StateFlow<RegisterUiState> = _uiState

    fun onEmailChanged(newEmail: String) {
        _uiState.update { currentState ->
            currentState.copy(email = newEmail)
        }
    }

    fun onPasswordChanged(newPassword: String) {
        _uiState.update { currentState ->
            currentState.copy(password = newPassword, passwordMismatchError = false)
        }
    }

    fun onConfirmPasswordChanged(newConfirmPassword: String) {
        _uiState.update { currentState ->
            currentState.copy(confirmPassword = newConfirmPassword, passwordMismatchError = false)
        }
    }

    fun onAccountTypeChanged(newAccountType: String) {
        _uiState.update { currentState ->
            currentState.copy(accountType = newAccountType)
        }
    }

    fun register(onRegistrationSuccess: (String) -> Unit) {
        if (_uiState.value.password != _uiState.value.confirmPassword) {
            _uiState.update { currentState ->
                currentState.copy(passwordMismatchError = true)
            }
            return
        }

        _uiState.update { currentState ->
            currentState.copy(isLoading = true, registrationError = null, passwordMismatchError = false)
        }

        // Aquí deberías llamar a tu repositorio o caso de uso para realizar el registro
        viewModelScope.launch {
            // Simulación de una llamada de registro
            kotlinx.coroutines.delay(2000)
            if (_uiState.value.email.isNotBlank() && _uiState.value.password.isNotBlank()) {
                // Registro exitoso
                _uiState.update { currentState ->
                    currentState.copy(isLoading = false)
                }
                onRegistrationSuccess(_uiState.value.accountType) // Llama a la función de éxito con el tipo de cuenta
                // Aquí podrías emitir un evento para indicar que la navegación debe ocurrir
            } else {
                // Error de registro
                _uiState.update { currentState ->
                    currentState.copy(
                        isLoading = false,
                        registrationError = "Error al registrar la cuenta. Inténtalo de nuevo."
                    )
                }
            }
        }
    }

    // Puedes agregar más funciones para manejar otras acciones en la pantalla de registro
}