package com.feylabs.halalkan.data.local

import android.content.Context
import android.content.SharedPreferences
import com.feylabs.halalkan.data.remote.reqres.auth.UserModel

class MyPreference(context: Context) {

    private val PREFS_NAME = "kotlinpref"
    val sharedPref: SharedPreferences =
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    private val editor: SharedPreferences.Editor = sharedPref.edit()

    fun save(KEY_NAME: String, value: Int) {
        editor.putInt(KEY_NAME, value)
        editor.commit()
    }

    fun save(KEY_NAME: String, value: String) {
        editor.putString(KEY_NAME, value)
        editor.commit()
    }

    fun saveTokenWithTemplate(TOKEN: String) {
        editor.putString("TOKEN", "Bearer $TOKEN")
        editor.commit()
    }

    fun getUserData(): UserModel {
        return UserModel(
            name = getUserName(),
            phoneNumber = getUserPhone().orEmpty(),
            email = getUserEmail().orEmpty(),
            createdAt = "",
            id = getUserID()?.toIntOrNull() ?: 0,
            photo_path = "",
            rolesId = getUserRole()?.toIntOrNull() ?: 0,
            updatedAt = ""
        )
    }

    fun saveLoginData(
        userId: String,
        name: String,
        email: String,
        photo: String,
        role: String,
        token: String? = null,
        phone: String? = null
    ) {
        saveUserRole(role)
        saveUserPhoto(photo)
        saveUserEmail(email)
        saveUserName(name)
        saveUserID(userId)
        saveUserPhone(phone.orEmpty())

        if (token != null) {
            saveTokenWithTemplate(token)
        }
    }


    fun getToken(): String {
        return sharedPref.getString("TOKEN", "").orEmpty()
    }

    fun getUserRole(): String? {
        return sharedPref.getString("ROLE", "")
    }

    fun getUserPhoto(): String {
        return sharedPref.getString("USER_PHOTO", "").orEmpty()
    }

    fun getTokenRaw(): String? {
        return sharedPref.getString("TOKEN_RAW", "")
    }

    fun getUserID(): String? {
        return sharedPref.getString("USER_ID", "")
    }

    fun saveUserID(id: String) {
        editor.putString("USER_ID", id)
        editor.commit()
    }

    fun saveUserRole(role: String) {
        editor.putString("ROLE", role)
        editor.commit()
    }

    fun saveUserPhoto(photo: String) {
        editor.putString("USER_PHOTO", photo)
        editor.commit()
    }

    fun removeKey(key: String) {
        editor.remove(key)
        editor.commit()
    }

    fun saveUserEmail(id: String) {
        editor.putString("USER_EMAIL", id)
        editor.commit()
    }

    fun getUserEmail(): String? {
        return sharedPref.getString("USER_EMAIL", "")
    }

    fun getUserPhone(): String? {
        return sharedPref.getString("USER_PHONE", "")
    }

    fun saveUserPhone(phone: String) {
        editor.putString("USER_PHONE", phone)
        editor.commit()
    }

    fun saveUserPassword(id: String) {
        editor.putString("USER_PASSWORD", id)
        editor.commit()
    }


    fun getUserPassword(): String? {
        return sharedPref.getString("USER_PASSWORD", "")
    }

    fun save(KEY_NAME: String, value: Boolean) {
        editor.putBoolean(KEY_NAME, value)
        editor.commit()
    }

    fun saveFloat(KEY_NAME: String, value: Float) {
        editor.putFloat(KEY_NAME, value)
        editor.commit()
    }

    fun getPrefBool(KEY_NAME: String): Boolean {
        return sharedPref.getBoolean(KEY_NAME, false)
    }

    fun getPrefString(KEY_NAME: String): String? {
        return sharedPref.getString(KEY_NAME, null)
    }

    fun getPrefFloat(KEY_NAME: String): Float {
        return sharedPref.getFloat(KEY_NAME, 0f)
    }

    fun getPrefInt(KEY_NAME: String): Int {
        return sharedPref.getInt(KEY_NAME, 0)
    }

    fun clearPreferences() {
        editor.clear()
        editor.commit()
    }

    fun getUserName(): String {
        return sharedPref.getString("NAME", "").orEmpty()
    }

    fun saveUserName(name: String) {
        editor.putString("NAME", name)
        editor.commit()
    }


}