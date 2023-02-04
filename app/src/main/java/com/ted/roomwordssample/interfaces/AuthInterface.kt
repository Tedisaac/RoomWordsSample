package com.ted.roomwordssample.interfaces

import androidx.lifecycle.MutableLiveData
import com.ted.roomwordssample.models.User
import com.ted.roomwordssample.viewmodels.ResponseState

interface AuthInterface {

    fun signUpWithFirebase(email: String, password: String) : MutableLiveData<ResponseState<User>>

    fun signInWithFirebase(email: String, password: String) : MutableLiveData<ResponseState<User>>
}