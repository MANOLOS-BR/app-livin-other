package com.manoloscorp.livinother.service.constants

class LivinOtherConstants private constructor() {


    // SharedPreferences
    object SHARED {
        const val SHARED_NAME = "livinOtherShared"
        const val TOKEN_USER = "user"
        const val TOKEN_KEY = "token"
    }

    // Requisições API
    object HEADER {
        const val NAME_AUTH = "Authorization"
    }

    object HTTP {
        const val SUCCESS = 200
        const val CREATED = 201
    }

    object RETROFIT {
        const val BASE_URL_DEV = "https://livinother.herokuapp.com/"
        const val BASE_URL_PROD = "https://api-livin-other.azurewebsites.net/"
    }

    object ONBOARDING {
        const val PRIVATE_MODE = 0    // Shared preference mode
        const val PREF_NAME = "app-prefs"
        const val IS_FIRST_TIME_LAUNCH = "IsFirstTimeLaunch"
    }

    object ORGAN {
        const val CORACAO = "Coração"
        const val PULMAO = "Pulmão"
        const val RIM = "Rim"
        const val FIGADO = "Fígado"
        const val PANCREAS = "Pâncreas"
        const val CORNEA = "Córnea"
    }

}