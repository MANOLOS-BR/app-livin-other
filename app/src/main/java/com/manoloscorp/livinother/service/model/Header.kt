package com.manoloscorp.livinother.service.model

import com.google.gson.annotations.SerializedName

class Header {

    @SerializedName("user")
    var user: Long = 0

    @SerializedName("token")
    var token: String = ""

}