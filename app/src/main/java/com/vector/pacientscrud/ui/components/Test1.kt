@file:OptIn(ExperimentalMaterial3Api::class)

package com.vector.pacientscrud.ui.components

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
    Column(
        modifier = modifier
            .fillMaxSize()
    ) {
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.weight(0.9f)
        ) {
            items(listOfPacientes.value) { paciente ->
                PacientDisplay(paciente = paciente, onClick = { id ->
                    viewModel.viewModelScope.launch {
                        val paciente = viewModel.getPacientById(id)
                        Log.i("Victor", "${paciente?.nombre}")
                        val pacienteJson = Gson().toJson(paciente)
                        navHostController.navigate("Buscar/$pacienteJson")
                    }
                })
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.1f),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(onClick = { navHostController.navigate("alta") }) {
                Text(text = "Altas")
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