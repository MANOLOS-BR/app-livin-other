package com.manoloscorp.livinother.service.repository

import android.content.Context
import com.manoloscorp.livinother.R
import com.manoloscorp.livinother.service.constants.LivinOtherConstants
import com.manoloscorp.livinother.service.listener.ApiListener
import com.manoloscorp.livinother.service.model.Profile
import com.manoloscorp.livinother.service.repository.remote.ProfileService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProfileRepository(val context: Context) : BaseRepository(context) {

    private val mRemote = RetrofitClient.createService(ProfileService::class.java)

    fun getProfile(id: Long, listener: ApiListener<Profile>) {
        val call: Call<Profile> = mRemote.getId(id)

        call.enqueue(object : Callback<Profile> {
            override fun onFailure(call: Call<Profile>, t: Throwable) {
                listener.onFailure(context.getString(R.string.ERROR_UNEXPECTED))
            }

            override fun onResponse(call: Call<Profile>, response: Response<Profile>) {
                if (response.code() != LivinOtherConstants.HTTP.SUCCESS) {
//                    val validation = Gson().fromJson(response.errorBody()?.string(), String::class.java)
//                    listener.onFailure(validation)
                    listener.onFailure(context.getString(R.string.ERROR_UNEXPECTED))
                } else {
                    response.body()?.let { listener.onSuccess(it) }
                }
            }

        })
    }

}