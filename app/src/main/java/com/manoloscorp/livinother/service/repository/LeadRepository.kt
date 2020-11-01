package com.manoloscorp.livinother.service.repository

import android.content.Context
import com.manoloscorp.livinother.R
import com.manoloscorp.livinother.service.constants.LivinOtherConstants
import com.manoloscorp.livinother.service.listener.ApiListener
import com.manoloscorp.livinother.service.model.Lead
import com.manoloscorp.livinother.service.model.Profile
import com.manoloscorp.livinother.service.repository.remote.LeadService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LeadRepository(val context: Context) : BaseRepository(context) {

    private val mRemote = RetrofitClient.createService(LeadService::class.java)

    fun register(body: Lead, listener: ApiListener<Profile>) {

        val call: Call<Profile> = mRemote.createLead(body)

        call.enqueue(object : Callback<Profile> {
            override fun onFailure(call: Call<Profile>, t: Throwable) {
                listener.onFailure(context.getString(R.string.ERROR_UNEXPECTED))
            }

            override fun onResponse(call: Call<Profile>, response: Response<Profile>) {
                if (response.code() != LivinOtherConstants.HTTP.CREATED) {
                    listener.onFailure(context.getString(R.string.ERROR_EXISTING_EMAIL))
                } else {
                    response.body()?.let { listener.onSuccess(it) }
                }
            }

        })
    }

}