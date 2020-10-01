package com.manoloscorp.livinother.service.constants

import com.google.gson.annotations.SerializedName
import com.manoloscorp.livinother.service.model.MedicalHistory

class LivinOtherConstants private constructor(){


    // SharedPreferences
    object SHARED {
        const val SHARED_NAME = "livinOtherShared"
        const val TOKEN_AUTH = "authorization"
        const val TOKEN_KEY = "token"
    }

    object REGISTER {
        const val USERNAME = "name"
        const val EMAIL = "email"
        const val PASSWORD = "password"
        const val DT_BIRTHDAY = "bday"
        const val GENRE = "genre"
        const val USER_TYPE = "userType"

        const val WEIGHT = "weight"
        const val HEIGHT = "height"
        const val DRUG_ADDICT = "drugAddict"
        const val ALCOHOL_CONSUMPTION = "alcoholConsumption"
        const val COMMUNICABLE_DISEASE = "communicableDisease"
        const val DEGENERATIVE_DISEASE = "degenerativeDisease"
        const val PRATICE_PHYSICAL_ACTIVITY = "practicePhysicalActivity"
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