package com.vector.pacientscrud.ui.navigation

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.gson.Gson
import com.vector.pacientscrud.data.model.Paciente
import com.vector.pacientscrud.ui.components.AltaPaciente
import com.vector.pacientscrud.ui.components.Mostrar
import com.vector.pacientscrud.ui.components.Screen

@Composable
fun MyNavHost(
    navController: NavHostController = rememberNavController(),
    modifier: Modifier = Modifier
) {
    NavHost(navController = navController  , startDestination = "pacientes", modifier = modifier) {
        composable("pacientes"){
            Screen(navHostController = navController)
        }

        composable("Buscar/{pacienteJson}") { backStackEntry ->
            val pacienteJson = backStackEntry.arguments?.getString("pacienteJson")
            val paciente = pacienteJson?.let { json ->
                // Convertir el JSON a un objeto Paciente
                Gson().fromJson(json, Paciente::class.java)
            }
            paciente?.let {
                Log.i("Victor","${paciente.id}")
                Mostrar(paciente = it, navHostController = navController)
            }
        }

        composable("alta"){
            AltaPaciente(navHostController = navController)
        }

        composable("modificar"){

        }

        composable("eliminar"){

        }
    }
}