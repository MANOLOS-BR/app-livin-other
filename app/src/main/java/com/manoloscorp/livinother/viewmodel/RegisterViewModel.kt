package com.manoloscorp.livinother.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.manoloscorp.livinother.service.constants.LivinOtherConstants
import com.manoloscorp.livinother.service.model.Lead
import com.manoloscorp.livinother.service.model.MedicalHistory
import com.manoloscorp.livinother.service.repository.local.SecurityPreferences

class RegisterViewModel(application: Application) : AndroidViewModel(application) {

    private val mRegisterUser = MutableLiveData<Lead>()
    var user: LiveData<Lead> = mRegisterUser

    private val mFragmentPosition = MutableLiveData<Int>()
    var fragmentPosition: LiveData<Int> = mFragmentPosition

    fun setCurrentFragmentPosition(position: Int) {
        mFragmentPosition.value = position
    }

    fun setBasicRegister(username: String, email: String, password: String) {
        val user = Lead()

        user.name = username
        user.email = email
        user.password = password

        mRegisterUser.value = user
    }

    fun setPersonalRegister(birthday: String, height: Double, weight: Double, genre: String) {
        val medicalHistory = MedicalHistory()

        medicalHistory.height = height
        medicalHistory.weight = weight

        mRegisterUser.value?.dataNascimento = birthday
        mRegisterUser.value?.medicalHistory = medicalHistory
        mRegisterUser.value?.genre = genre
    }

    fun setHealthRegister(){

    }


}