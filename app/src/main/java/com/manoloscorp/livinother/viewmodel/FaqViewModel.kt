package com.manoloscorp.livinother.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.manoloscorp.livinother.service.listener.ApiListener
import com.manoloscorp.livinother.service.listener.ValidationListener
import com.manoloscorp.livinother.service.model.Faq
import com.manoloscorp.livinother.service.model.Profile
import com.manoloscorp.livinother.service.repository.FaqRepository

class FaqViewModel(application: Application) : AndroidViewModel(application) {

    private val mFaqRepository = FaqRepository(application)

    private val mFaq = MutableLiveData<List<Faq>>()
    var faq: LiveData<List<Faq>> = mFaq

    private val mValidationListener = MutableLiveData<ValidationListener>()
    var validation: LiveData<ValidationListener> = mValidationListener

    fun getAllFaqs() {

        mFaqRepository.getAllFaqs(object : ApiListener<List<Faq>> {
            override fun onSuccess(param: List<Faq>) {
                mFaq.value = param
            }

            override fun onFailure(msg: String) {
                mValidationListener.value = ValidationListener(msg)
            }

        })

    }

}