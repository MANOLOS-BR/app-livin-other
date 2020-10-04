package com.manoloscorp.livinother.service.repository.remote

import com.manoloscorp.livinother.service.model.Authentication
import com.manoloscorp.livinother.service.model.Header
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST


interface AuthService {

    @POST("/api/authentication/login")
    fun auth(
        @Body body: Authentication
    ): Call<Header>

}