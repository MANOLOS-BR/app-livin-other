package com.manoloscorp.livinother.service.model

import java.io.Serializable

data class User(
    val dataNascimento: String,
    val email: String,
    val genero: String,
    val id: Long,
    val medicalHistory: MedicalHistory,
    val name: String,
    val password: String,
    val userType: String
) : Serializable