package com.vector.pacientscrud.ui.components

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.vector.pacientscrud.data.implementations.ApiServiceRetrofitImp
import com.vector.pacientscrud.data.model.Paciente
import com.vector.pacientscrud.di.RetrofitInstance
import kotlinx.coroutines.launch

@Composable
fun Mostrar(modifier: Modifier = Modifier, paciente: Paciente, navHostController: NavHostController) {
    val apiFacade = ApiServiceRetrofitImp(RetrofitInstance.apiService)
    val viewModelFactory = ViewModelFactory(apiFacade)
    val viewModel: ViewModelExample = viewModel(factory = viewModelFactory)
    val context = LocalContext.current

    val nombre = remember { mutableStateOf(paciente.nombre) }
    val apellido = remember { mutableStateOf(paciente.apellido) }
    val edad = remember { mutableStateOf(paciente.edad.toString()) }
    val sexo = remember { mutableStateOf(paciente.sexo.toString()) }

    Column(
        modifier = modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TextField(
            value = nombre.value,
            onValueChange = { nombre.value = it },
            label = { Text("Nombre") }
        )
        TextField(
            value = apellido.value,
            onValueChange = { apellido.value = it },
            label = { Text("Apellido") }
        )
        TextField(
            value = edad.value,
            onValueChange = { edad.value = it },
            label = { Text("Edad") }
        )
        TextField(
            value = sexo.value,
            onValueChange = { sexo.value = it },
            label = { Text("Sexo") }
        )
        Row(
            modifier = modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(onClick = {
                viewModel.viewModelScope.launch {
                    viewModel.deletePaciente(paciente.id!!)
                    Toast.makeText(
                        context,
                        "Eliminado!",
                        Toast.LENGTH_LONG
                    ).show()
                    navHostController.navigateUp()
                }
            }) {
                Text(text = "Eliminar")
            }
            Button(onClick = {
                viewModel.viewModelScope.launch {
                    val updatedPaciente = paciente.copy(
                        nombre = nombre.value,
                        apellido = apellido.value,
                        edad = edad.value.toIntOrNull() ?: paciente.edad,
                        sexo = sexo.value.first()
                    )
                    viewModel.updatePaciente(paciente.id!!, updatedPaciente)
                    Toast.makeText(
                        context,
                        "Guardado!",
                        Toast.LENGTH_LONG
                    ).show()
                    navHostController.navigateUp()
                }
            }) {
                Text(text = "Guardar")
            }
        }
    }
}