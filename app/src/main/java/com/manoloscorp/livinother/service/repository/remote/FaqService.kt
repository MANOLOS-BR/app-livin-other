package com.manoloscorp.livinother.service.repository.remote

import com.manoloscorp.livinother.service.model.Faq
import retrofit2.Call
import retrofit2.http.GET

interface FaqService {

    @GET("/api/faq")
    fun getFaqs(): Call<List<Faq>>

}