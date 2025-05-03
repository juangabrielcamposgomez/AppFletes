package com.oriundo.appfletes

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.oriundo.appfletes.ui.theme.AppFletesTheme
import com.oriundo.appfletes.wiews.ProfileScreen
import com.oriundo.appfletes.wiews.RegisterScreen
import java.util.Locale



private val type: MenuAnchorType

    get() {

        TODO("Not yet implemented")

    }



sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Trips : Screen("trips")
    object StartTrip : Screen("start_trip")
    object Profile : Screen("profile")
    object Register : Screen("register")
    object Payments : Screen("payments")
}



class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AppFletesTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AppNavigation()
                }

            }

        }

    }

}



@OptIn(ExperimentalMaterial3Api::class)
@Composable

fun AppNavigation() {
    val navController = rememberNavController()
    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = currentBackStackEntry?.destination?.route

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("App Fletes") }
            )
        },

        bottomBar = {
            if (currentRoute != Screen.Register.route) { // No mostrar la barra de navegación en la pantalla de registro
                NavigationBar {
                    val items = listOf(
                        Screen.Trips,
                        Screen.StartTrip,
                        Screen.Profile,
                        Screen.Payments
                    )
                    val icons = listOf(
                        Icons.Filled.Map,
                        Icons.Filled.PlayArrow,
                        Icons.Filled.Person,
                        Icons.Filled.CreditCard
                    )
                    items.forEachIndexed { index, screen ->
                        NavigationBarItem(
                            icon = { Icon(icons[index], contentDescription = screen.route) },
                            label = { Text(screen.route.replaceFirstChar {
                                if (it.isLowerCase()) it.titlecase(
                                    Locale.getDefault()
                                ) else it.toString()
                            }) },
                            selected = currentRoute == screen.route,
                            onClick = {
                                navController.navigate(screen.route) {

// Evitar múltiples copias de la misma pantalla en la pila de navegación

                                    launchSingleTop = true

// Restaurar el estado al volver

                                    restoreState = true

                                }

                            }

                        )

                    }

                }

            }

        }

    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Home.route,
            Modifier.padding(innerPadding)
        ) {
            composable(Screen.Home.route) { HomeScreen() }
            composable(Screen.Trips.route) { Text("Pantalla de Viajes") } // Reemplazar con tu Composable
            composable(Screen.StartTrip.route) { Text("Pantalla de Iniciar Viaje") } // Reemplazar con tu Composable
            composable(Screen.Profile.route) {
                ProfileScreen(onNavigateToRegister = { navController.navigate(Screen.Register.route) })
            }
            composable(Screen.Register.route) {
                RegisterScreen(onRegister = { accountType ->
                    println("Usuario registrado como: $accountType")
                    navController.popBackStack() // Volver a la pantalla anterior (Perfil)
                })
            }
            composable(Screen.Payments.route) { Text("Pantalla de Pagos") } // Reemplazar con tu Composable
        }

    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen() {
    var destination by remember { mutableStateOf("") }
    var pickup by remember { mutableStateOf("") }
    var selectedServiceType: String by remember { mutableStateOf("Casa") }
    var expandedServiceType by remember { mutableStateOf(false) }
    val serviceTypes = listOf("Casa", "Departamento", "Ferretería")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ExposedDropdownMenuBox(
            expandedServiceType, { (!expandedServiceType).also { expandedServiceType = it } }
        ) {
            val enabled = true // Puedes ajustar este valor según tus necesidades enabled
            OutlinedTextField(
                value = selectedServiceType,
                onValueChange = { /* No se permite la edición manual */ },
                readOnly = true,
                label = { Text("Tipo de servicio") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedServiceType) },
                modifier = Modifier.fillMaxWidth()
                    .menuAnchor(type, enabled)
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
        OutlinedTextField(
            value = destination,
            onValueChange = { destination = it },
            label = { Text("¿A dónde vamos?") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(
            value = pickup,
            onValueChange = { pickup = it },
            label = { Text("¿Dónde te recogemos?") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(32.dp))
        Text("Tipo de servicio seleccionado: $selectedServiceType")
        Spacer(modifier = Modifier.height(16.dp))
        Text("Contenido principal de la aplicación")
    }
}