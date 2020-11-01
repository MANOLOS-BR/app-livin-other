package com.manoloscorp.livinother.service.repository

import android.content.Context
import com.manoloscorp.livinother.R
import com.manoloscorp.livinother.service.constants.LivinOtherConstants
import com.manoloscorp.livinother.service.listener.ApiListener
import com.manoloscorp.livinother.service.model.Faq
import com.manoloscorp.livinother.service.repository.remote.FaqService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FaqRepository(val context: Context) : BaseRepository(context) {

    private val mRemote = RetrofitClient.createService(FaqService::class.java)

    fun getAllFaqs(listener: ApiListener<List<Faq>>) {

        val call: Call<List<Faq>> = mRemote.getFaqs()

        call.enqueue(object : Callback<List<Faq>> {
            override fun onFailure(call: Call<List<Faq>>, t: Throwable) {
                listener.onFailure(context.getString(R.string.ERROR_UNEXPECTED))
            }

            override fun onResponse(call: Call<List<Faq>>, response: Response<List<Faq>>) {
                if (response.code() != LivinOtherConstants.HTTP.SUCCESS) {
                    listener.onFailure(context.getString(R.string.ERROR_UNEXPECTED))
                } else {
                    response.body()?.let { listener.onSuccess(it) }
                }
            }
        })
    }
}