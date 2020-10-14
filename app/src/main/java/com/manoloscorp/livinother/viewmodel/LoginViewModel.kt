package com.manoloscorp.livinother.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.manoloscorp.livinother.service.constants.LivinOtherConstants.SHARED.TOKEN_AUTH
import com.manoloscorp.livinother.service.constants.LivinOtherConstants.SHARED.TOKEN_KEY
import com.manoloscorp.livinother.service.listener.ApiListener
import com.manoloscorp.livinother.service.listener.ValidationListener
import com.manoloscorp.livinother.service.model.Authentication
import com.manoloscorp.livinother.service.model.Header
import com.manoloscorp.livinother.service.repository.AuthRepository
import com.manoloscorp.livinother.service.repository.RetrofitClient
import com.manoloscorp.livinother.service.repository.local.SecurityPreferences

class LoginViewModel(application: Application) : AndroidViewModel(application) {

    private val mAuthRepository = AuthRepository(application)
    private val mSharedPreferences = SecurityPreferences(application)

    private val mLogin = MutableLiveData<ValidationListener>()
    var login: LiveData<ValidationListener> = mLogin

    private val mLoggedUser = MutableLiveData<Boolean>()
    var loggedUser: LiveData<Boolean> = mLoggedUser

    fun doLogin(username: String, password: String){
        val bodyAuth = Authentication(username, password)
        mAuthRepository.login(bodyAuth, object : ApiListener<Header>{
            override fun onSuccess(param: Header) {
//                mSharedPreferences.storeString(TOKEN_AUTH, param.authorization)
                mSharedPreferences.storeString(TOKEN_KEY, param.token)

                RetrofitClient.addHeader(param.token)

                mLogin.value = ValidationListener()
            }

            override fun onFailure(msg: String) {
                mLogin.value = ValidationListener(msg)
            }

        })
    }

    /**
     * Verifica se usuário está logado
     */
    fun verifyLoggedUser() {

        val token = mSharedPreferences.getString(TOKEN_KEY)

        RetrofitClient.addHeader(token)
        val logged = (token != "")
        mLoggedUser.value = logged
    }

}