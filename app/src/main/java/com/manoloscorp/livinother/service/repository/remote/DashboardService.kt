package com.manoloscorp.livinother.service.repository.remote

import com.manoloscorp.livinother.service.model.Dashboard
import retrofit2.Call
import retrofit2.http.GET

interface DashboardService {

    @GET("/api/dashboard")
    fun getDashboard(): Call<Dashboard>

}