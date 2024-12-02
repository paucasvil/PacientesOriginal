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
): ViewModel() {
    private val _pacientes = MutableStateFlow<List<Paciente>>(emptyList())

    val pacientes = _pacientes
    init {
        viewModelScope.launch {
            when(val res = apiImp.getPacients()){
                is ApiResult.Failure -> {
                    _pacientes.value = emptyList()
                    Log.i("Victor", res.message)
                }
                is ApiResult.NetworkError -> {
                    _pacientes.value = emptyList()
                    Log.i("Victor", res.errorMessage)
                }
                is ApiResult.Success -> _pacientes.value = res.data
            }
        }
    }

    suspend fun altaPaciente(paciente: Paciente): Boolean{
        var made = false
        return withContext(Dispatchers.IO) {
            when(val res = apiImp.createPacient(paciente)){
                is ApiResult.Failure -> {
                    Log.i("Victor",res.message)
                }
                is ApiResult.NetworkError -> {
                    Log.i("Victor",res.errorMessage)
                }
                is ApiResult.Success -> {
                    made = true
                }
            }
            made
        }

    }

    suspend fun updatePaciente(id: Int, paciente: Paciente): Boolean{
        return withContext(Dispatchers.IO) {
            var res = false
            when(val query = apiImp.updatePacient(id, paciente)) {
                is ApiResult.Failure -> {
                    Log.i("Victor", "El error es ${query.message} con codigo ${query.code}")
                }
                is ApiResult.NetworkError -> Log.i("Victor",query.errorMessage)
                is ApiResult.Success -> {
                    res = true
                }
            }
            res
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

    suspend fun deletePaciente(id: Int){
        return withContext(Dispatchers.IO) {
            when(val res = apiImp.deletePacient(id)){
                is ApiResult.Failure -> {
                    Log.i("Victor", "El error es ${res.message} con codigo ${res.code}")
                }
                is ApiResult.NetworkError -> {
                    Log.i("Victor", "El error es ${res.errorMessage}")
                }
                is ApiResult.Success -> {

                }
            }
        }
    }
}