package com.manoloscorp.livinother.service.model

import com.google.gson.annotations.SerializedName

data class Organ (

    @SerializedName("id")
    val id: Long?,

    @SerializedName("name")
    val name: String?,

    @SerializedName("timeIschemia")
    val timeIschemia: String?,

    @SerializedName("unit")
    val unit: String
)