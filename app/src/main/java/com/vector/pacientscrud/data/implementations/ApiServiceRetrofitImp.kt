package com.vector.pacientscrud.data.implementations

import com.vector.pacientscrud.data.model.Paciente
import com.vector.pacientscrud.data.network.ApiResult
import com.vector.pacientscrud.data.network.ApiService
import com.vector.pacientscrud.data.network.PacientApiFacade

class ApiServiceRetrofitImp(
    private val apiService: ApiService
) : PacientApiFacade {

    override suspend fun getPacients(): ApiResult<List<Paciente>> {
        return try {
            val response = apiService.getPacients()
            if (response.isSuccessful) {
                ApiResult.Success(response.body() ?: emptyList())
            } else {
                ApiResult.Failure(
                    response.code(),
                    response.errorBody()?.string() ?: "Unknown error"
                )
            }
        } catch (e: Exception) {
            ApiResult.NetworkError(e.message ?: "Network error occurred")
        }
    }

    override suspend fun getPacientById(id: Int): ApiResult<Paciente> {
        return try {
            val response = apiService.getPacientById(id)
            if (response.isSuccessful) {
                response.body()?.let {
                    ApiResult.Success(it)
                } ?: ApiResult.Failure(response.code(), "No data available")
            } else {
                ApiResult.Failure(
                    response.code(),
                    response.errorBody()?.string() ?: "Unknown error"
                )
            }
        } catch (e: Exception) {
            ApiResult.NetworkError(e.message ?: "Network error occurred")
        }
    }

    override suspend fun createPacient(paciente: Paciente): ApiResult<Boolean> {
        return try {
            val response = apiService.createPacient(paciente)
            if (response.isSuccessful) {
                ApiResult.Success(true)
            } else {
                ApiResult.Failure(response.code(), response.message())
            }
        } catch (e: Exception) {
            ApiResult.NetworkError(e.message ?: "Network error occurred")
        }
    }

    override suspend fun updatePacient(id: Int, paciente: Paciente): ApiResult<Boolean> {
        return try {
            val response = apiService.updatePacient(id, paciente)
            if (response.isSuccessful) {
                ApiResult.Success(true)
            } else {
                ApiResult.Failure(
                    response.code(),
                    response.errorBody()?.string() ?: "Unknown error"
                )
            }
        } catch (e: Exception) {
            ApiResult.NetworkError(e.message ?: "Network error occurred")
        }
    }

    override suspend fun deletePacient(id: Int): ApiResult<Boolean> {
        return try {
            val response = apiService.deletePacient(id)
            if (response.isSuccessful) {
                ApiResult.Success(true)
            } else {
                ApiResult.Failure(
                    response.code(),
                    response.errorBody()?.string() ?: "Unknown error"
                )
            }
        } catch (e: Exception) {
            // En caso de error de red, convertimos la excepción en un error de red
            ApiResult.NetworkError(e.localizedMessage ?: "Unknown network error")
        }
    }
}
