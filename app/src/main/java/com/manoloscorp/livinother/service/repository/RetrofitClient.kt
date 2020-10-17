package com.manoloscorp.livinother.service.repository

import com.manoloscorp.livinother.service.constants.LivinOtherConstants.HEADER.NAME_AUTH
import com.manoloscorp.livinother.service.constants.LivinOtherConstants.RETROFIT.BASE_URL
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitClient private constructor() {

    companion object {

        private var mAuthorization = ""

        private lateinit var mRetrofit: Retrofit

        private fun getRetrofitInstance(): Retrofit {

            val httpClient = OkHttpClient.Builder()

            httpClient.addInterceptor { chain ->
                val request = chain.request()
                    .newBuilder()
                    .addHeader(NAME_AUTH, mAuthorization)
                    .build()
                chain.proceed(request)
            }

            if (!Companion::mRetrofit.isInitialized) {
                mRetrofit = Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(httpClient.build())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
            }

            return mRetrofit

        }

        fun <S> createService(serviceClass: Class<S>): S {
            return getRetrofitInstance().create(serviceClass)
        }

        fun addHeader(token: String) {
            this.mAuthorization = token
        }

    }

}