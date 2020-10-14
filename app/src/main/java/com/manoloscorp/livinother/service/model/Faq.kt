package com.manoloscorp.livinother.service.model

import com.google.gson.annotations.SerializedName

class Faq {

    @SerializedName("id")
    var id: Long = 0

    @SerializedName("question")
    var question: String = ""

    @SerializedName("answer")
    var answer: String = ""

}
