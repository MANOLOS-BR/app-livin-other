package com.manoloscorp.livinother.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.manoloscorp.livinother.service.constants.LivinOtherConstants.SHARED.TOKEN_KEY
import com.manoloscorp.livinother.service.constants.LivinOtherConstants.SHARED.TOKEN_USER
import com.manoloscorp.livinother.service.listener.ApiListener
import com.manoloscorp.livinother.service.listener.ValidationListener
import com.manoloscorp.livinother.service.model.Profile
import com.manoloscorp.livinother.service.repository.ProfileRepository
import com.manoloscorp.livinother.service.repository.local.SecurityPreferences

class ProfileViewModel(application: Application) : AndroidViewModel(application) {

    private val mProfileRepository = ProfileRepository(application)
    private val mSharedPreferences = SecurityPreferences(application)

    private val mIdUser = MutableLiveData<Long>()
    var idUser: LiveData<Long> = mIdUser

    private val mUpdateProfile = MutableLiveData<Profile>()
    var updateProfile: LiveData<Profile> = mUpdateProfile

    private val mProfile = MutableLiveData<Profile>()
    var profile: LiveData<Profile> = mProfile

    private val mValidationListener = MutableLiveData<ValidationListener>()
    var validation: LiveData<ValidationListener> = mValidationListener

    fun getUserId() {
        val id = mSharedPreferences.getString(TOKEN_USER).toLong()
        mIdUser.value = id
    }

    fun getProfile(id: Long) {
        mProfileRepository.getProfile(id, object : ApiListener<Profile> {
            override fun onSuccess(param: Profile) {
                mProfile.value = param
            }

            override fun onFailure(msg: String) {
                mValidationListener.value = ValidationListener(msg)
            }

        })
    }

    fun updateProfile(id: Long, param: Profile) {
        mProfileRepository.updateProfile(id, param, object : ApiListener<Profile> {
            override fun onSuccess(param: Profile) {
                mProfile.value = param
            }

            override fun onFailure(msg: String) {
                mValidationListener.value = ValidationListener(msg)
            }

        })
    }

    fun getUserData(): Profile {
        return mProfile.value!!
    }

    fun logout() {
        mSharedPreferences.remove(TOKEN_USER)
        mSharedPreferences.remove(TOKEN_KEY)
    }

}