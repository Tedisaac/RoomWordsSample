package com.ted.roomwordssample.interfaces

import androidx.lifecycle.MutableLiveData
import com.ted.roomwordssample.models.User
import com.ted.roomwordssample.viewmodels.ResponseState

interface AuthInterface {

    suspend fun signUpWithFirebase(email: String, password: String) : MutableLiveData<ResponseState<User>>

    suspend fun signInWithFirebase(email: String, password: String) : MutableLiveData<ResponseState<User>>

    fun signOutUser()
}