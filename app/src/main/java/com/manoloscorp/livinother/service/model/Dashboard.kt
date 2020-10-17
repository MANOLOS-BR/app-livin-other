package com.manoloscorp.livinother.service.model

import com.google.gson.annotations.SerializedName

data class Dashboard (

    @SerializedName("transplants")
    val transplants: Long,

    @SerializedName("donations")
    val donations: Long,

    @SerializedName("organList")
    val organList: List<Organ>,

    @SerializedName("transplantList")
    val transplantList: List<Transplant>,

    @SerializedName("donationList")
    val donationList: List<Donation>
)
