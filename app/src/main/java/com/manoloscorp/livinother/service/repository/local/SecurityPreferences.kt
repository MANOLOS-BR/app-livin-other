package com.manoloscorp.livinother.service.repository.local

import android.content.Context
import android.content.SharedPreferences
import com.manoloscorp.livinother.service.constants.LivinOtherConstants.ONBOARDING.IS_FIRST_TIME_LAUNCH
import com.manoloscorp.livinother.service.constants.LivinOtherConstants.SHARED.SHARED_NAME

class SecurityPreferences(context: Context) {

    private val mPreferences: SharedPreferences =
        context.getSharedPreferences(SHARED_NAME, Context.MODE_PRIVATE)

    fun storeString(key: String, value: String) {
        mPreferences.edit().putString(key, value).apply()
    }

    fun isFirstLaunch(): Boolean = mPreferences.getBoolean(IS_FIRST_TIME_LAUNCH, true)

    fun setFirstTimeLaunch(isFirstTime: Boolean) {
        mPreferences.edit().putBoolean(IS_FIRST_TIME_LAUNCH, isFirstTime).commit()
    }

    fun storeDouble(key: String, value: Double) {
        mPreferences.edit().putLong(key, java.lang.Double.doubleToRawLongBits(value));
    }

    fun remove(key: String) {
        mPreferences.edit().remove(key).apply()
    }

    fun getString(key: String): String {
        return mPreferences.getString(key, "") ?: ""
    }

    fun getDouble(key: String?, defaultValue: Double): Double {
        return java.lang.Double.longBitsToDouble(
            mPreferences.getLong(
                key, java.lang.Double.doubleToLongBits(defaultValue)
            )
        )
    }

}