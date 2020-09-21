package com.manoloscorp.livinother.service.model

import java.io.Serializable

data class MedicalHistory(
    val alcoholConsumption: Boolean,
    val communicableDisease: Boolean,
    val degenerativeDisease: Boolean,
    val drugAddict: Boolean,
    val height: Long,
    val id: Long,
    val practicePhysicalActivity: Boolean,
    val weight: Long
) : Serializable