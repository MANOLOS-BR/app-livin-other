package com.manoloscorp.livinother.service.repository

import android.content.Context
import com.manoloscorp.livinother.R
import com.manoloscorp.livinother.service.constants.LivinOtherConstants
import com.manoloscorp.livinother.service.listener.ApiListener
import com.manoloscorp.livinother.service.model.Dashboard
import com.manoloscorp.livinother.service.repository.remote.DashboardService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DashboardRepository(val context: Context) : BaseRepository(context) {

    private val mRemote = RetrofitClient.createService(DashboardService::class.java)

    fun getDashboard(listener: ApiListener<Dashboard>) {

        val call: Call<Dashboard> = mRemote.getDashboard()

        call.enqueue(object : Callback<Dashboard> {
            override fun onFailure(call: Call<Dashboard>, t: Throwable) {
                listener.onFailure(context.getString(R.string.ERROR_UNEXPECTED))
            }

            override fun onResponse(call: Call<Dashboard>, response: Response<Dashboard>) {
                if (response.code() != LivinOtherConstants.HTTP.SUCCESS) {
                    listener.onFailure(context.getString(R.string.ERROR_UNEXPECTED))
                } else {
                    response.body()?.let { listener.onSuccess(it) }
                }
            }
        })
    }
}