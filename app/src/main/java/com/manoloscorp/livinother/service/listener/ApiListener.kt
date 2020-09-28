package com.manoloscorp.livinother.service.listener

interface ApiListener<T>  {

    fun onSuccess(param: T)
    fun onFailure(msg: String)

}