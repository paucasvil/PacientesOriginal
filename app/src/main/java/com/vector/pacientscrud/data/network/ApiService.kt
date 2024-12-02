package com.vector.pacientscrud.data.network

import com.vector.pacientscrud.data.model.Paciente
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ApiService {
    @GET("pacientes")
    suspend fun getPacients(): Response<List<Paciente>>
    @GET("pacientes/{id}")
    suspend fun getPacientById(@Path("id") id: Int): Response<Paciente>
    @POST("pacientes")
    suspend fun createPacient(@Body paciente: Paciente): Response<Paciente>
    @PUT("pacientes/{id}")
    suspend fun updatePacient(@Path("id") id: Int, @Body paciente: Paciente): Response<Paciente>
    @DELETE("pacientes/{id}")
    suspend fun deletePacient(@Path("id") id: Int): Response<Unit>
}