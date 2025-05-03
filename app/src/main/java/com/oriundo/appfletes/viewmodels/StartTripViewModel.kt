package com.oriundo.appfletes.viewmodels



import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.oriundo.appfletes.wiews.PaymentMethod
import com.oriundo.appfletes.wiews.TransportType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class StartTripUiState(
    val selectedServiceType: String = "",
    val expandedServiceType: Boolean = false,
    val serviceTypes: List<String> = emptyList(),
    val selectedTransportType: TransportType? = null,
    val transportTypes: List<TransportType> = emptyList(),
    val selectedPaymentMethod: PaymentMethod? = null,
    val paymentMethods: List<PaymentMethod> = emptyList(),
    val estimatedDistance: String = "[Calcular]",
    val estimatedCost: String = "[Calcular]",
    val isLoading: Boolean = false,
    val tripRequestError: String? = null
    // Puedes agregar más estados aquí según las necesidades de tu pantalla de inicio de viaje
)

class StartTripViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(StartTripUiState(
        serviceTypes = listOf("Mudanza Casa", "Mudanza Departamento", "Traslado Ferretería"),
        transportTypes = listOf(
            TransportType("Camioneta Pequeña", "500 kg", "url_camioneta_pequena"),
            TransportType("Camioneta Mediana", "1000 kg", "url_camioneta_mediana"),
            TransportType("Camión 3/4", "2000 kg", "url_camion_3_4")
        ),
        paymentMethods = listOf(PaymentMethod("Efectivo"), PaymentMethod("Débito"), PaymentMethod("Crédito"), PaymentMethod("Prepago"))
    ))
    val uiState: StateFlow<StartTripUiState> = _uiState

    fun onServiceTypeChanged(newServiceType: String) {
        _uiState.update { currentState ->
            currentState.copy(selectedServiceType = newServiceType)
        }
    }

    fun onExpandedServiceTypeChanged(isExpanded: Boolean) {
        _uiState.update { currentState ->
            currentState.copy(expandedServiceType = isExpanded)
        }
    }

    fun onTransportTypeSelected(transportType: TransportType) {
        _uiState.update { currentState ->
            currentState.copy(selectedTransportType = transportType)
        }
    }

    fun onPaymentMethodSelected(paymentMethod: PaymentMethod) {
        _uiState.update { currentState ->
            currentState.copy(selectedPaymentMethod = paymentMethod)
        }
    }

    // Función para simular el cálculo de la distancia y el costo estimado
    fun calculateEstimatedInfo(destination: String, pickup: String) {
        // Aquí iría la lógica real para calcular la distancia y el costo
        // Esto podría involucrar llamadas a servicios de mapas, etc.
        viewModelScope.launch {
            // Simulación de la obtención de la información estimada
            kotlinx.coroutines.delay(1000)
            _uiState.update { currentState ->
                currentState.copy(
                    estimatedDistance = "15 km (Estimado)",
                    estimatedCost = "$25.000 (Estimado)"
                )
            }
        }
    }

    fun requestTrip(onTripRequestedSuccess: () -> Unit) {
        if (_uiState.value.selectedServiceType.isNotEmpty() &&
            _uiState.value.selectedTransportType != null &&
            _uiState.value.selectedPaymentMethod != null
        ) {
            _uiState.update { currentState ->
                currentState.copy(isLoading = true, tripRequestError = null)
            }
            // Aquí deberías llamar a tu repositorio o caso de uso para realizar la solicitud del viaje
            viewModelScope.launch {
                // Simulación de la solicitud del viaje
                kotlinx.coroutines.delay(3000)
                // Simulación de éxito
                _uiState.update { currentState ->
                    currentState.copy(isLoading = false)
                }
                onTripRequestedSuccess()
                // Si la solicitud falla, actualiza el estado con un error
                // _uiState.update { currentState ->
                //     currentState.copy(isLoading = false, tripRequestError = "Error al solicitar el viaje.")
                // }
            }
        } else {
            _uiState.update { currentState ->
                currentState.copy(tripRequestError = "Por favor, selecciona el tipo de servicio, transporte y método de pago.")
            }
        }
    }

    // Puedes agregar más funciones para manejar otras acciones en la pantalla de inicio de viaje
}