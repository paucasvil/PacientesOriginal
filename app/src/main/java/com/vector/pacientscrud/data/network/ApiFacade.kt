package com.vector.pacientscrud.data.network



import com.vector.pacientscrud.data.model.Paciente

sealed class ApiResult<out T> {
    data class Success<out T>(val data: T) : ApiResult<T>()
    data class Failure(val code: Int, val message: String) : ApiResult<Nothing>()
    data class NetworkError(val errorMessage: String) : ApiResult<Nothing>()
}

interface PacientApiFacade {
    suspend fun getPacients(): ApiResult<List<Paciente>>
    suspend fun getPacientById(id: Int): ApiResult<Paciente>
    suspend fun createPacient(paciente: Paciente): ApiResult<Boolean>
    suspend fun updatePacient(id: Int, paciente: Paciente): ApiResult<Boolean>
    suspend fun deletePacient(id: Int): ApiResult<Boolean>
}
