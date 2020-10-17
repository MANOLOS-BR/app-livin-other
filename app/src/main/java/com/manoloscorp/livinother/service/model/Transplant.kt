package com.manoloscorp.livinother.service.model

import com.google.gson.annotations.SerializedName

data class Transplant (

    @SerializedName("id")
    val id: Long?,

    @SerializedName("organ")
    val organ: Organ?,

    @SerializedName("queueAmount")
    val queueAmount: Long?,

    @SerializedName("numberTransplants")
    val numberTransplants: Long?
)
