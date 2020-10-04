package com.manoloscorp.livinother.service.repository.remote

import com.manoloscorp.livinother.service.model.Lead
import com.manoloscorp.livinother.service.model.Profile
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface LeadService {

    @POST("/api/leads")
    fun createLead(
        @Body body: Lead
    ): Call<Profile>

}