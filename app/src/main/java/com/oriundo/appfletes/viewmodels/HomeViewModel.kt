package com.oriundo.appfletes.viewmodels



import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class HomeUiState(
    val destination: String = "",
    val pickup: String = "",
    val selectedBottomNavigationItem: Int = 0,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
    // Puedes agregar más estados aquí según las necesidades de tu pantalla
)

class HomeViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState

    fun onDestinationChanged(newDestination: String) {
        _uiState.update { currentState ->
            currentState.copy(destination = newDestination)
        }
    }

    fun onPickupChanged(newPickup: String) {
        _uiState.update { currentState ->
            currentState.copy(pickup = newPickup)
        }
    }

    fun onBottomNavigationItemSelected(index: Int) {
        _uiState.update { currentState ->
            currentState.copy(selectedBottomNavigationItem = index)
        }
    }

    // Ejemplo de una función para iniciar una búsqueda o acción basada en los datos
    fun buscarFletes() {
        _uiState.update { currentState ->
            currentState.copy(isLoading = true, errorMessage = null)
        }
        // Aquí podrías llamar a un repositorio o caso de uso para realizar la lógica de búsqueda
        viewModelScope.launch {
            // Simulación de una operación asíncrona
            kotlinx.coroutines.delay(2000)
            if (_uiState.value.destination.isNotBlank() && _uiState.value.pickup.isNotBlank()) {
                // Éxito: actualiza el estado si es necesario
                _uiState.update { currentState ->
                    currentState.copy(isLoading = false)
                }
            } else {
                // Error: actualiza el estado con un mensaje de error
                _uiState.update { currentState ->
                    currentState.copy(
                        isLoading = false,
                        errorMessage = "Por favor, ingresa el destino y el punto de recogida."
                    )
                }
            }
        }
    }

    // Puedes agregar más funciones para manejar la lógica de la pantalla Home
}