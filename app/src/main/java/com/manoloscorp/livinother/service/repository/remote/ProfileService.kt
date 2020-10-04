package com.manoloscorp.livinother.service.repository.remote

import com.manoloscorp.livinother.service.model.Profile
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Path

interface ProfileService {

    @GET("/api/profile/{id}")
    fun getId(
        @Path(value = "id", encoded = true) id: Int
    ): Call<Profile>

    @PUT
    fun updateProfile()

}