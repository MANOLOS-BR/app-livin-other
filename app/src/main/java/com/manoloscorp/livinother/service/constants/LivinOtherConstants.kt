package com.manoloscorp.livinother.service.constants

import com.google.gson.annotations.SerializedName
import com.manoloscorp.livinother.service.model.MedicalHistory

class LivinOtherConstants private constructor() {


    // SharedPreferences
    object SHARED {
        const val SHARED_NAME = "livinOtherShared"
        const val TOKEN_AUTH = "authorization"
        const val TOKEN_KEY = "token"
    }

    // Requisições API
    object HEADER {
        const val NAME_AUTH = "Authorization"
        const val TOKEN_AUTH = "authorization"
        const val TOKEN_KEY = "token"
    }

    object HTTP {
        const val SUCCESS = 200
        const val CREATED = 201
    }

    object RETROFIT {
        const val BASE_URL = "https://livinother.herokuapp.com/"
    }

    object ONBOARDING {
        const val PRIVATE_MODE = 0    // Shared preference mode
        const val PREF_NAME = "app-prefs"
        const val IS_FIRST_TIME_LAUNCH = "IsFirstTimeLaunch"
    }

}