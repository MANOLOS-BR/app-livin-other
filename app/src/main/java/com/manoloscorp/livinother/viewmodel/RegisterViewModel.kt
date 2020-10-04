package com.manoloscorp.livinother.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.manoloscorp.livinother.service.listener.ApiListener
import com.manoloscorp.livinother.service.listener.ValidationListener
import com.manoloscorp.livinother.service.model.Lead
import com.manoloscorp.livinother.service.model.MedicalHistory
import com.manoloscorp.livinother.service.model.Profile
import com.manoloscorp.livinother.service.repository.LeadRepository
import com.manoloscorp.livinother.utils.FormatValues

class RegisterViewModel(application: Application) : AndroidViewModel(application) {

    private val mLeadRepository = LeadRepository(application)

    private val mCreateUser = MutableLiveData<Lead>()
    var user: LiveData<Lead> = mCreateUser

    private val mFragmentPosition = MutableLiveData<Int>()
    var fragmentPosition: LiveData<Int> = mFragmentPosition

    private val mRegister = MutableLiveData<ValidationListener>()
    var register: LiveData<ValidationListener> = mRegister

    private val mDialog = MutableLiveData<Boolean>()
    var dialog: LiveData<Boolean> = mDialog

    fun setCurrentFragmentPosition(position: Int) {
        mFragmentPosition.value = position
    }

    fun setBasicRegister(username: String, email: String, password: String) {
        val user = Lead()

        user.name = username
        user.email = email
        user.password = password

        mCreateUser.value = user
    }

    fun setPersonalRegister(birthday: String, height: Double, weight: Double, genre: String) {
        val medicalHistory = MedicalHistory()

        medicalHistory.height = height
        medicalHistory.weight = weight

        mCreateUser.value?.dataNascimento = FormatValues.formatBirthdayDate(birthday).toString()

        mCreateUser.value?.medicalHistory = medicalHistory
        mCreateUser.value?.genre = genre
    }

    fun setHealthRegister(
        userType: String,
        eatingHabit: String,
        chemicalAddict: Boolean,
        alcoholic: Boolean,
        communicableDiseases: Boolean,
        degenerativeDiseases: Boolean,
        practicePhysicalActivities: Boolean
    ) {
        mCreateUser.value?.userType = userType

        mCreateUser.value?.medicalHistory?.drugAddict = chemicalAddict
        mCreateUser.value?.medicalHistory?.alcoholConsumption = alcoholic
        mCreateUser.value?.medicalHistory?.communicableDisease = communicableDiseases
        mCreateUser.value?.medicalHistory?.degenerativeDisease = degenerativeDiseases
        mCreateUser.value?.medicalHistory?.practicePhysicalActivity = practicePhysicalActivities

        doRegister(mCreateUser.value!!)
    }

    private fun doRegister(body: Lead) {
        mDialog.value = true

        mLeadRepository.register(body, object : ApiListener<Profile> {
            override fun onSuccess(param: Profile) {
                mRegister.value = ValidationListener()
            }

            override fun onFailure(msg: String) {
                mRegister.value = ValidationListener(msg)
            }

        })
    }

}