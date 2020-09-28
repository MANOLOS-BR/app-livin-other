package com.manoloscorp.livinother.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.manoloscorp.livinother.service.listener.ApiListener
import com.manoloscorp.livinother.service.listener.ValidationListener
import com.manoloscorp.livinother.service.model.Profile
import com.manoloscorp.livinother.service.repository.ProfileRepository

class ProfileViewModel(application: Application) : AndroidViewModel(application) {

    private val mProfileRepository = ProfileRepository(application)

    private val mProfile = MutableLiveData<Profile>()
    var profile: LiveData<Profile> = mProfile

    private val mValidationListener = MutableLiveData<ValidationListener>()
    var validation: LiveData<ValidationListener> = mValidationListener

    fun getProfile(id: Int){
        mProfileRepository.getProfile(id, object : ApiListener<Profile>{
            override fun onSuccess(param: Profile) {
                mProfile.value = param
            }

            override fun onFailure(msg: String) {
                mValidationListener.value = ValidationListener(msg)
            }

        })
    }

}