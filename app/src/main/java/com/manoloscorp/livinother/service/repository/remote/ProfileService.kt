package com.manoloscorp.livinother.service.repository.remote

import com.manoloscorp.livinother.service.model.Profile
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Path


interface ProfileService {

    @GET("/api/profile/{id}")
    fun getProfileById(
        @Path(value = "id", encoded = true) id: Long
    ): Call<Profile>

    @PUT("/api/profile/{id}")
    fun updateProfile(
        @Path(value = "id", encoded = true) id: Long, @Body param: Profile
    ): Call<Profile>

}