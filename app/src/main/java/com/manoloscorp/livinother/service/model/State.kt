package com.manoloscorp.livinother.service.model

import com.google.gson.annotations.SerializedName

data class State (

    @SerializedName("id")
    val id: Long?,

    @SerializedName("region")
    val region: String?,

    @SerializedName("name")
    val name: String?,

    @SerializedName("stateAcronym")
    val stateAcronym: String?
)