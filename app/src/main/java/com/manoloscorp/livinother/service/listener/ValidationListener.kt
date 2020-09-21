package com.manoloscorp.livinother.service.listener

class ValidationListener(message: String = "") {

    private var mStatus: Boolean = true
    private var mMessage: String = ""

    init {
        if (message != ""){
            mStatus = false
            mMessage = message
        }
    }

    fun status() = mStatus
    fun errorMessage() = mMessage

}