package com.manoloscorp.livinother.service.constants

class LivinOtherConstants private constructor(){


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
    }

    object RETROFIT {
        const val BASE_URL = "https://livinother.herokuapp.com/"
    }


}