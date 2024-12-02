package com.vector.pacientscrud.data.model

import com.google.gson.annotations.SerializedName

data class Paciente(
    @SerializedName("id") val id: Int? = null,
    @SerializedName("nombre") val nombre: String,
    @SerializedName("apellido") val apellido: String,
    @SerializedName("edad") val edad: Int,
    @SerializedName("sexo") val sexo: Char
)