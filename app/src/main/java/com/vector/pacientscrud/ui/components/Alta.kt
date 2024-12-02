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
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.vector.pacientscrud.data.implementations.ApiServiceRetrofitImp
import com.vector.pacientscrud.data.model.Paciente
import com.vector.pacientscrud.di.RetrofitInstance
import com.vector.pacientscrud.ui.theme.PacientsCrudTheme
import kotlinx.coroutines.launch

@Composable
fun AltaPaciente(modifier: Modifier = Modifier, navHostController: NavHostController) {
    val apiFacade = ApiServiceRetrofitImp(RetrofitInstance.apiService)
    val viewModelFactory = ViewModelFactory(apiFacade)
    val viewModel: ViewModelExample = viewModel(factory = viewModelFactory)

    // Contexto para mostrar Toast
    val context = LocalContext.current

    // Estados de entrada
    var nombre by remember { mutableStateOf("") }
    var apellido by remember { mutableStateOf("") }
    var edad by remember { mutableStateOf("") }
    var sexo by remember { mutableStateOf("") }

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
                    text = "ALTA DE PACIENTES",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )

                TextField(
                    value = nombre,
                    onValueChange = { nombre = it },
                    label = { Text(text = "Nombre") },
                    modifier = Modifier.fillMaxWidth()
                )

                TextField(
                    value = apellido,
                    onValueChange = { apellido = it },
                    label = { Text(text = "Apellido") },
                    modifier = Modifier.fillMaxWidth()
                )

                TextField(
                    value = edad,
                    onValueChange = { edad = it },
                    label = { Text(text = "Edad") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )

                TextField(
                    value = sexo,
                    onValueChange = { sexo = it },
                    label = { Text(text = "Sexo") },
                    modifier = Modifier.fillMaxWidth()
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Button(
                        onClick = {
                            // Validación básica
                            if (nombre.isBlank() || apellido.isBlank() || edad.isBlank() || sexo.isBlank()) {
                                Toast.makeText(
                                    context,
                                    "Por favor, llena todos los campos.",
                                    Toast.LENGTH_LONG
                                ).show()
                                return@Button
                            }

                            val paciente = Paciente(
                                nombre = nombre,
                                apellido = apellido,
                                edad = edad.toIntOrNull() ?: 0,
                                sexo = sexo.firstOrNull() ?: 'N'
                            )
                            viewModel.viewModelScope.launch {
                                val result = viewModel.altaPaciente(paciente)
                                if (result) {
                                    Toast.makeText(
                                        context,
                                        "Paciente creado!",
                                        Toast.LENGTH_LONG
                                    ).show()
                                    navHostController.navigateUp()
                                } else {
                                    Toast.makeText(
                                        context,
                                        "Error al crear paciente.",
                                        Toast.LENGTH_LONG
                                    ).show()
                                }
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                    ) {
                        Text(text = "Alta", color = MaterialTheme.colorScheme.onPrimary)
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
}


@Preview
@Composable
private fun AltaPacientePreview() {
    PacientsCrudTheme {
        Surface(onClick = { /*TODO*/ }) {
//            AltaPaciente(navHostController = NavHostController())
        }
    }

}