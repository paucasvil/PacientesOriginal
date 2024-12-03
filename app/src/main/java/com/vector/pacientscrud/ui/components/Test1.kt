@file:OptIn(ExperimentalMaterial3Api::class)

package com.vector.pacientscrud.ui.components

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.google.gson.Gson
import com.vector.pacientscrud.data.implementations.ApiServiceRetrofitImp
import com.vector.pacientscrud.data.model.Paciente
import com.vector.pacientscrud.di.RetrofitInstance
import kotlinx.coroutines.launch

@Composable
fun Screen(modifier: Modifier = Modifier, navHostController: NavHostController) {
    val apiFacade = ApiServiceRetrofitImp(RetrofitInstance.apiService)
    val viewModelFactory = ViewModelFactory(apiFacade)
    val viewModel: ViewModelExample = viewModel(factory = viewModelFactory)
    val listOfPacientes = viewModel.pacientes.collectAsState()
    val pageSize = 5
    var currentPage by remember { mutableStateOf(0) }

    val pacientesPaginated = listOfPacientes.value.chunked(pageSize)
    val isFirstPage = currentPage == 0
    val isLastPage = currentPage == pacientesPaginated.lastIndex

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background) // Fondo de la pantalla
            .padding(16.dp)
    ) {
        // Botón "Menú principal" al inicio
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp), // Separación desde el top
            horizontalArrangement = Arrangement.Center
        ) {
            Button(
                onClick = { navHostController.navigate("menu") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary, // Color de fondo del botón
                    contentColor = Color.White // Color del texto
                )
            ) {
                Text(text = "Menú principal", style = MaterialTheme.typography.titleMedium)
            }
        }

        if (pacientesPaginated.isNotEmpty()) {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.weight(0.9f)
            ) {
                items(pacientesPaginated[currentPage]) { paciente ->
                    PacientDisplay(paciente = paciente, onClick = { id ->
                        viewModel.viewModelScope.launch {
                            val paciente = viewModel.getPacientById(id)
                            val pacienteJson = Gson().toJson(paciente)
                            navHostController.navigate("Buscar/$pacienteJson")
                        }
                    })
                }
            }
        } else {
            Text(
                text = "No hay pacientes para mostrar.",
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        }

        // Botones de paginación con flechas
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.1f),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(
                onClick = { if (!isFirstPage) currentPage-- },
                enabled = !isFirstPage,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.secondary
                )
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Anterior",
                    tint = Color.White
                )
            }

            Button(
                onClick = { if (!isLastPage) currentPage++ },
                enabled = !isLastPage,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.secondary
                )
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = "Siguiente",
                    tint = Color.White
                )
            }
        }

        // Botón "Nuevo paciente"
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            Button(
                onClick = { navHostController.navigate("alta") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary, // Color de fondo del botón
                    contentColor = Color.White // Color del texto
                )
            ) {
                Text(text = "Nuevo paciente", style = MaterialTheme.typography.titleMedium)
            }
        }
    }
}


@Composable
private fun PacientDisplay(
    modifier: Modifier = Modifier,
    paciente: Paciente,
    onClick: (Int) -> Unit
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .padding(12.dp)
            .clickable { onClick(paciente.id ?: -1) } // Acción de clic en la tarjeta
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),  // Degradado desde un color suave
                        MaterialTheme.colorScheme.primary // Hasta un color más intenso
                    )
                ),
                shape = MaterialTheme.shapes.medium // Bordes redondeados
            ),
        shadowElevation = 8.dp, // Sombra más pronunciada para darle profundidad
        shape = MaterialTheme.shapes.medium // Bordes redondeados
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp), // Espaciado interno de la tarjeta
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "Paciente ID: ${paciente.id}",
                style = MaterialTheme.typography.bodyMedium.copy(color = MaterialTheme.colorScheme.onSurface)
            )
            Text(
                text = "Nombre: ${paciente.nombre}",
                style = MaterialTheme.typography.bodyMedium.copy(color = MaterialTheme.colorScheme.onSurface)
            )
            Text(
                text = "Apellido: ${paciente.apellido}",
                style = MaterialTheme.typography.bodyMedium.copy(color = MaterialTheme.colorScheme.onSurface)
            )
            Text(
                text = "Edad: ${paciente.edad}",
                style = MaterialTheme.typography.bodyMedium.copy(color = MaterialTheme.colorScheme.onSurface)
            )
            Text(
                text = "Sexo: ${paciente.sexo}",
                style = MaterialTheme.typography.bodyMedium.copy(color = MaterialTheme.colorScheme.onSurface)
            )

            Button(
                onClick = { onClick(paciente.id ?: -1) },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.secondary, // Fondo del botón
                    contentColor = Color.White // Color del texto
                ),
                shape = MaterialTheme.shapes.small // Bordes redondeados en el botón
            ) {
                Text(text = "Ver más")
            }
        }
    }
}
