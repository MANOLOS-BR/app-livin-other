package com.manoloscorp.livinother.service.repository

import android.content.Context
import com.manoloscorp.livinother.R
import com.manoloscorp.livinother.service.constants.LivinOtherConstants.HTTP.SUCCESS
import com.manoloscorp.livinother.service.listener.ApiListener
import com.manoloscorp.livinother.service.model.Authentication
import com.manoloscorp.livinother.service.model.Header
import com.manoloscorp.livinother.service.repository.remote.AuthService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AuthRepository(val context: Context) : BaseRepository(context) {

    private var mRemote = RetrofitClient.createService(AuthService::class.java)


    fun login(body: Authentication, listener: ApiListener<Header>) {

        if (!isConnectionAvailable(context)) {
            listener.onFailure(context.getString(R.string.ERROR_INTERNET_CONNECTION))
            return
        }

        val call: Call<Header> = mRemote.auth(body)

        call.enqueue(object : Callback<Header> {
            override fun onFailure(call: Call<Header>, t: Throwable) {
                listener.onFailure(context.getString(R.string.ERROR_UNEXPECTED))
            }

            override fun onResponse(call: Call<Header>, response: Response<Header>) {
                if (response.code() != SUCCESS) {
//                    val validation = Gson().fromJson(response.errorBody()?.let { toString() }, String::class.java)
//                    listener.onFailure(validation)

                    listener.onFailure(context.getString(R.string.ERROR_LOGIN))
                } else {
                    response.body()?.let { listener.onSuccess(it) }
                }
            }

        })

    }

}