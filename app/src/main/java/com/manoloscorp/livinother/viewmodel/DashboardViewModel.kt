package com.manoloscorp.livinother.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.manoloscorp.livinother.service.listener.ApiListener
import com.manoloscorp.livinother.service.listener.ValidationListener
import com.manoloscorp.livinother.service.model.Dashboard
import com.manoloscorp.livinother.service.repository.DashboardRepository

class DashboardViewModel(application: Application) : AndroidViewModel(application) {

    private val mDashboardRepository = DashboardRepository(application)

    private val mDashboard = MutableLiveData<Dashboard>()
    var dashboard: LiveData<Dashboard> = mDashboard

    private val mValidationListener = MutableLiveData<ValidationListener>()
    var validation: LiveData<ValidationListener> = mValidationListener

    fun getDashboard() {
        mDashboardRepository.getDashboard(object : ApiListener<Dashboard> {
            override fun onSuccess(param: Dashboard) {
                mDashboard.value = param
            }

            override fun onFailure(msg: String) {
                mValidationListener.value = ValidationListener(msg)
            }
        })
    }
}