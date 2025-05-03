package com.oriundo.appfletes.wiews


import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.oriundo.appfletes.ui.theme.AppFletesTheme

data class TransportType(val name: String, val capacity: String, val imageUrl: String) // Modelo para el tipo de transporte
data class PaymentMethod(val name: String) // Modelo para el método de pago

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StartTripScreen(onRequestTrip: () -> Unit) {
    var selectedServiceType by remember { mutableStateOf("") }
    var expandedServiceType by remember { mutableStateOf(false) }
    val serviceTypes = listOf("Mudanza Casa", "Mudanza Departamento", "Traslado Ferretería")

    var selectedTransportType by remember { mutableStateOf<TransportType?>(null) }
    val transportTypes = remember {
        listOf(
            TransportType("Camioneta Pequeña", "500 kg", "url_camioneta_pequena"), // Reemplazar con URLs reales
            TransportType("Camioneta Mediana", "1000 kg", "url_camioneta_mediana"),
            TransportType("Camión 3/4", "2000 kg", "url_camion_3_4")
            // Agrega más tipos de transporte
        )
    }

    var selectedPaymentMethod by remember { mutableStateOf<PaymentMethod?>(null) }
    val paymentMethods = remember { listOf(PaymentMethod("Efectivo"), PaymentMethod("Débito"), PaymentMethod("Crédito"), PaymentMethod("Prepago")) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text("Detalles del Viaje", style = MaterialTheme.typography.headlineSmall)
        Spacer(modifier = Modifier.height(16.dp))

        // Información Estimada (la lógica de cálculo vendrá después)
        Text("Distancia Estimada: [Calcular]", style = MaterialTheme.typography.bodyMedium)
        Text("Costo Estimado: [Calcular]", style = MaterialTheme.typography.bodyMedium)
        Spacer(modifier = Modifier.height(16.dp))

        // Selección del Tipo de Servicio
        ExposedDropdownMenuBox(
            expanded = expandedServiceType,
            onExpandedChange = { expandedServiceType = !expandedServiceType }
        ) {
            val type = null
            val enabled = false
            OutlinedTextField(
                value = selectedServiceType,
                onValueChange = { /* No se permite la edición manual */ },
                readOnly = true,
                label = { Text("Tipo de Servicio") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedServiceType) },
                modifier = Modifier.fillMaxWidth()
                    .menuAnchor(type = type, enabled = enabled)
            )
            ExposedDropdownMenu(
                expanded = expandedServiceType,
                onDismissRequest = { expandedServiceType = false }
            ) {
                serviceTypes.forEach { serviceType ->
                    DropdownMenuItem(
                        text = { Text(text = serviceType) },
                        onClick = {
                            selectedServiceType = serviceType
                            expandedServiceType = false
                        }
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(16.dp))

        // Selección del Tipo de Transporte
        Text("Seleccionar Transporte", style = MaterialTheme.typography.titleMedium)
        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            items(transportTypes) { type ->
                Card(
                    modifier = Modifier
                        .width(150.dp)
                        .clickable { selectedTransportType = type },
                    colors = CardDefaults.cardColors(
                        containerColor = if (selectedTransportType == type) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(8.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        // Aquí deberías cargar la imagen desde la URL (necesitarás una biblioteca para esto como Coil o Glide)
                        Text("[Imagen]", modifier = Modifier.height(80.dp)) // Placeholder para la imagen
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(type.name, style = MaterialTheme.typography.bodyMedium)
                        Text("Capacidad: ${type.capacity}", style = MaterialTheme.typography.bodySmall)
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(16.dp))

        // Selección del Método de Pago
        Text("Seleccionar Pago", style = MaterialTheme.typography.titleMedium)
        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            items(paymentMethods) { method ->
                Button(
                    onClick = { selectedPaymentMethod = method },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (selectedPaymentMethod == method) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondaryContainer
                    )
                ) {
                    Text(method.name)
                }
            }
        }
        Spacer(modifier = Modifier.weight(1f)) // Empuja el botón de solicitar al fondo

        // Botón para Solicitar Viaje
        Button(
            onClick = onRequestTrip,
            modifier = Modifier.fillMaxWidth(),
            enabled = selectedServiceType.isNotEmpty() && selectedTransportType != null && selectedPaymentMethod != null
        ) {
            Text("Solicitar Viaje")
        }
    }
}

@SuppressLint("ModifierFactoryUnreferencedReceiver")
private fun Modifier.menuAnchor(type: Nothing?, enabled: Boolean): Modifier {
    TODO("Not yet implemented")
}

@Preview(showBackground = true)
@Composable
fun StartTripScreenPreview() {
    AppFletesTheme {
        StartTripScreen(onRequestTrip = {})
    }
}