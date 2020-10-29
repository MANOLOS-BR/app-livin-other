package com.manoloscorp.livinother.service.model

import com.google.gson.annotations.SerializedName

class Lead {

    @SerializedName("name")
    var name: String = ""

    @SerializedName("email")
    var email: String = ""

    @SerializedName("password")
    var password: String = ""

    @SerializedName("birthDate")
    var birthDate: String = ""

    @SerializedName("genre")
    var genre: String = ""

    @SerializedName("userType")
    var userType: String = ""

    @SerializedName("eatingHabit")
    var eatingHabit: String = ""

    @SerializedName("medicalHistory")
    lateinit var medicalHistory: MedicalHistory

}