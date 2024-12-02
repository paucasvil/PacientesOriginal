package com.vector.pacientscrud.ui.components

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
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

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .padding(16.dp),
            tonalElevation = 4.dp,
            shape = MaterialTheme.shapes.medium
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Editar Paciente",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.primary
                )
                TextField(
                    value = nombre.value,
                    onValueChange = { nombre.value = it },
                    label = { Text("Nombre") },
                    modifier = Modifier.fillMaxWidth()
                )
                TextField(
                    value = apellido.value,
                    onValueChange = { apellido.value = it },
                    label = { Text("Apellido") },
                    modifier = Modifier.fillMaxWidth()
                )
                TextField(
                    value = edad.value,
                    onValueChange = { edad.value = it },
                    label = { Text("Edad") },
                    modifier = Modifier.fillMaxWidth()
                )
                TextField(
                    value = sexo.value,
                    onValueChange = { sexo.value = it },
                    label = { Text("Sexo") },
                    modifier = Modifier.fillMaxWidth()
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Button(
                        onClick = {
                            viewModel.viewModelScope.launch {
                                viewModel.deletePaciente(paciente.id!!)
                                Toast.makeText(context, "Eliminado!", Toast.LENGTH_LONG).show()
                                navHostController.navigateUp()
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Eliminar",
                            tint = Color.White
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(text = "Eliminar", color = Color.White)
                    }
                    Button(
                        onClick = {
                            viewModel.viewModelScope.launch {
                                val updatedPaciente = paciente.copy(
                                    nombre = nombre.value,
                                    apellido = apellido.value,
                                    edad = edad.value.toIntOrNull() ?: paciente.edad,
                                    sexo = sexo.value.first()
                                )
                                viewModel.updatePaciente(paciente.id!!, updatedPaciente)
                                Toast.makeText(context, "Guardado!", Toast.LENGTH_LONG).show()
                                navHostController.navigateUp()
                            }
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Done,
                            contentDescription = "Guardar",
                            tint = Color.White
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(text = "Guardar", color = Color.White)
                    }
                }
                Button(
                    onClick = { navHostController.navigateUp() },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary)
                ) {
                    Text(text = "Atrás", color = MaterialTheme.colorScheme.onSecondary)
                }
            }
        }
    }
}