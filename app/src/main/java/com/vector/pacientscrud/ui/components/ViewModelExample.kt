package com.vector.pacientscrud.ui.components

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vector.pacientscrud.data.model.Paciente
import com.vector.pacientscrud.data.network.ApiResult
import com.vector.pacientscrud.data.network.PacientApiFacade
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ViewModelExample(
    private val apiImp: PacientApiFacade
) : ViewModel() {
    private val _pacientes = MutableStateFlow<List<Paciente>>(emptyList())
    val pacientes = _pacientes

    init {
        // Cargar pacientes al inicio
        loadPacientes()
    }

    // Función para cargar pacientes desde la API
    private fun loadPacientes() {
        viewModelScope.launch {
            when (val res = apiImp.getPacients()) {
                is ApiResult.Failure -> {
                    _pacientes.value = emptyList()
                }
                is ApiResult.NetworkError -> {
                    _pacientes.value = emptyList()
                }
                is ApiResult.Success -> _pacientes.value = res.data
            }
        }
    }

    suspend fun altaPaciente(paciente: Paciente): Boolean {
        var made = false
        return withContext(Dispatchers.IO) {
            when (val res = apiImp.createPacient(paciente)) {
                is ApiResult.Failure -> {
                    Log.i("Victor", res.message)
                }
                is ApiResult.NetworkError -> {
                    Log.i("Victor", res.errorMessage)
                }
                is ApiResult.Success -> {
                    made = true
                    // Recargar la lista de pacientes después de crear uno nuevo
                    loadPacientes()
                }
            }
            made
        }
    }

    suspend fun updatePaciente(id: Int, paciente: Paciente): Boolean {
        return withContext(Dispatchers.IO) {
            var res = false
            when (val query = apiImp.updatePacient(id, paciente)) {
                is ApiResult.Failure -> {
                    Log.i("Victor", "El error es ${query.message} con codigo ${query.code}")
                }
                is ApiResult.NetworkError -> Log.i("Victor", query.errorMessage)
                is ApiResult.Success -> {
                    res = true
                    // Recargar la lista de pacientes después de la actualización
                    loadPacientes()
                }
            }
            res
        }
    }

    suspend fun deletePaciente(id: Int) {
        return withContext(Dispatchers.IO) {
            when (val res = apiImp.deletePacient(id)) {
                is ApiResult.Failure -> {
                    Log.i("Victor", "El error es ${res.message} con codigo ${res.code}")
                }
                is ApiResult.NetworkError -> {
                    Log.i("Victor", "El error es ${res.errorMessage}")
                }
                is ApiResult.Success -> {
                    // Recargar la lista de pacientes después de eliminar uno
                    loadPacientes()
                }
            }
        }
    }

    suspend fun getPacientById(id: Int): Paciente? {
        return withContext(Dispatchers.IO) {
            var paciente: Paciente? = null
            when (val res = apiImp.getPacientById(id)) {
                is ApiResult.Failure -> {
                    Log.i("Victor", "El error es ${res.message} con codigo ${res.code}")
                }
                is ApiResult.NetworkError -> {
                    Log.i("Victor", "El error es ${res.errorMessage}")
                }
                is ApiResult.Success -> {
                    paciente = res.data
                }
            }
            paciente
        }
    }
}
