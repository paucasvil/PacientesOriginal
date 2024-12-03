package com.vector.pacientscrud.ui.components

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val context = LocalContext.current
        var nombre by remember { mutableStateOf("") }
        var apellido by remember { mutableStateOf("") }
        var edad by remember { mutableStateOf("") }
        var sexo by remember { mutableStateOf("") }
        var direccion by remember { mutableStateOf("") }
        var telefono by remember { mutableStateOf("") }
        var tipo_sangre by remember { mutableStateOf("") }
        var peso by remember { mutableStateOf("") }
        var altura by remember { mutableStateOf("") }

        Text(text = "ALTA DE PACIENTES", fontWeight = FontWeight.Bold, fontSize = 30.sp)
        Spacer(modifier = Modifier.height(20.dp))

        // Campos de entrada
        TextField(value = nombre, onValueChange = { nombre = it }, label = { Text(text = "Nombre") })
        Spacer(modifier = Modifier.height(20.dp))
        TextField(value = apellido, onValueChange = { apellido = it }, label = { Text(text = "Apellido") })
        Spacer(modifier = Modifier.height(20.dp))
        TextField(
            value = edad,
            onValueChange = { edad = it },
            label = { Text(text = "Edad") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )
        Spacer(modifier = Modifier.height(20.dp))
        TextField(value = sexo, onValueChange = { sexo = it }, label = { Text(text = "Sexo") })
        Spacer(modifier = Modifier.height(20.dp))

        // Nuevos campos
        TextField(value = direccion, onValueChange = { direccion = it }, label = { Text(text = "Dirección") })
        Spacer(modifier = Modifier.height(20.dp))
        TextField(value = telefono, onValueChange = { telefono = it }, label = { Text(text = "Teléfono") })
        Spacer(modifier = Modifier.height(20.dp))
        TextField(value = tipo_sangre, onValueChange = { tipo_sangre = it }, label = { Text(text = "Tipo de sangre") })
        Spacer(modifier = Modifier.height(20.dp))
        TextField(
            value = peso,
            onValueChange = { peso = it },
            label = { Text(text = "Peso") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )
        Spacer(modifier = Modifier.height(20.dp))
        TextField(
            value = altura,
            onValueChange = { altura = it },
            label = { Text(text = "Altura") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )
        Spacer(modifier = Modifier.height(20.dp))

        // Botones
        Button(onClick = {
            val paciente = Paciente(
                nombre = nombre,
                apellido = apellido,
                edad = edad.toInt(),
                sexo = sexo.first(),
                direccion = direccion,
                telefono = telefono,
                tipo_sangre = tipo_sangre,
                peso = peso.toFloatOrNull(),
                altura = altura.toFloatOrNull()
            )
            viewModel.viewModelScope.launch {
                val result = viewModel.altaPaciente(paciente)
                if (result) {
                    Toast.makeText(context, "Paciente creado!", Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(context, "Paciente no creado!", Toast.LENGTH_LONG).show()
                }
            }
        }) {
            Text(text = "Guardar")
        }
        Spacer(modifier = Modifier.height(20.dp))

        Button(onClick = { navHostController.navigate("pacientes") }) {
            Text(text = "Regresar")
        }
    }
}

