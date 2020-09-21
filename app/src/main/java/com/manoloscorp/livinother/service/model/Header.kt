package com.manoloscorp.livinother.service.model

import com.google.gson.annotations.SerializedName

class Header {

    @SerializedName("authorization")
    var authorization: String = ""

    @SerializedName("token")
    var token: String = ""

}