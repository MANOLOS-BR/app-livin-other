package com.manoloscorp.livinother.service.model

import com.google.gson.annotations.SerializedName

data class Donation (

    @SerializedName("id")
    val id: Long,

    @SerializedName("state")
    val state: State,

    @SerializedName("potentialDonor")
    val potentialDonor: Long,

    @SerializedName("effectiveDonor")
    val effectiveDonor: Long,

    @SerializedName("familyInterview")
    val familyInterview: Long,

    @SerializedName("familyNegative")
    val familyNegative: Long
)