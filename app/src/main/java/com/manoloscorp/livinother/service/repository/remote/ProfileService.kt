package com.manoloscorp.livinother.service.repository.remote

import com.manoloscorp.livinother.service.model.Profile
import retrofit2.Call
import retrofit2.http.*


interface ProfileService {

    @GET("/api/profile/{id}")
    fun getProfileById(
        @Path(value = "id", encoded = true) id: Long
    ): Call<Profile>

    @PUT("/api/profile/{id}")
    fun updateProfile(
        @Path(value = "id", encoded = true) id: Long, @Body param: Profile
    ): Call<Profile>

    @DELETE("/api/profile/{id}")
    fun deleteProfile(
        @Path(value = "id", encoded = true) id: Long
    ):  Call<Unit>
}