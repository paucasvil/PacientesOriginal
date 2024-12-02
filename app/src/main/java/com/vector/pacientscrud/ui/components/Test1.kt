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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
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
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.google.gson.Gson
import com.vector.pacientscrud.data.implementations.ApiServiceRetrofitImp
import com.vector.pacientscrud.data.model.Paciente
import com.vector.pacientscrud.di.RetrofitInstance
import com.vector.pacientscrud.ui.theme.DismissBackground
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
            .padding(16.dp) // Agregado padding general
    ) {
        if (pacientesPaginated.isNotEmpty()) {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.weight(0.9f)
            ) {
                items(pacientesPaginated[currentPage]) { paciente ->
                    PacientCard(paciente = paciente, onClick = { id ->
                        viewModel.viewModelScope.launch {
                            val paciente = viewModel.getPacientById(id)
                            val pacienteJson = Gson().toJson(paciente)
                            navHostController.navigate("Buscar/$pacienteJson")
                        }
                    })
                }
            }
        }else{
            Text(
                text = "No hay pacientes para mostrar.",
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            Button(
                onClick = { if (!isFirstPage) currentPage-- },
                enabled = !isFirstPage
            ) {
                Text(text = "Anterior")
            }

            Button(
                onClick = { if (!isLastPage) currentPage++ },
                enabled = !isLastPage
            ) {
                Text(text = "Siguiente")
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
            horizontalArrangement = Arrangement.Center
        ){
            Button(
                onClick = { navHostController.navigate("alta") }
            ) {
                Text(text = "Registrar Paciente")
            }
        }

    }
}

@Composable
private fun PacientCard(
    modifier: Modifier = Modifier,
    paciente: Paciente,
    onClick: (Int) -> Unit
) {
    val dismissState = rememberSwipeToDismissBoxState()

    SwipeToDismissBox(
        state = dismissState,
        backgroundContent = { DismissBackground() },

    ) {
        Surface(
            modifier = modifier
                .fillMaxWidth()
                .clickable { onClick(paciente.id ?: -1) }
                .padding(8.dp), // Espaciado interno
            tonalElevation = 2.dp,
            shape = androidx.compose.foundation.shape.RoundedCornerShape(8.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp), // Más espaciado interno
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = "Paciente ID: ${paciente.id}",
                    style = androidx.compose.material3.MaterialTheme.typography.titleMedium
                )
                Text(
                    text = "Nombre: ${paciente.nombre}",
                    style = androidx.compose.material3.MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = "Apellido: ${paciente.apellido}",
                    style = androidx.compose.material3.MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = "Edad: ${paciente.edad}",
                    style = androidx.compose.material3.MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = "Sexo: ${paciente.sexo}",
                    style = androidx.compose.material3.MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}

@Composable
fun DismissBackground() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .size(60.dp)
            .background(color = androidx.compose.ui.graphics.Color.Blue),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Eliminar",
            color = androidx.compose.ui.graphics.Color.White,
            style = androidx.compose.material3.MaterialTheme.typography.bodyLarge
        )
    }
}


@Composable
private fun PacientDisplay(
    modifier: Modifier = Modifier,
    paciente: Paciente,
    onClick: (Int) -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick(paciente.id ?: -1) },
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Paciente ${paciente.id}")
        Text(text = "Nombre: ${paciente.nombre}")
        Text(text = "Apellido: ${paciente.apellido}")
        Text(text = "Edad: ${paciente.edad}")
        Text(text = "Sexo: ${paciente.sexo}")
    }
}