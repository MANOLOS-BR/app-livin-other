package com.manoloscorp.livinother.service.model

import com.google.gson.annotations.SerializedName


class MedicalHistory{

    @SerializedName("weight")
    var weight: Double = 0.0

    @SerializedName("height")
    var height: Double = 0.0

    @SerializedName("drugAddict")
    var drugAddict: Boolean = false

    @SerializedName("alcoholConsumption")
    var alcoholConsumption: Boolean = false

    @SerializedName("communicableDisease")
    var communicableDisease: Boolean = false

    @SerializedName("degenerativeDisease")
    var degenerativeDisease: Boolean = false

    @SerializedName("practicePhysicalActivity")
    var practicePhysicalActivity: Boolean = false

}