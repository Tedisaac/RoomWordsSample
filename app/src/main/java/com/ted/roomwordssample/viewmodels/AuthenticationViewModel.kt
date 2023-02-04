package com.ted.roomwordssample.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ted.roomwordssample.models.User
import com.ted.roomwordssample.repository.WordRepository

class AuthenticationViewModel constructor(private val wordRepository: WordRepository): ViewModel() {

    private var _authenticatedUserLiveData:  MutableLiveData<ResponseState<User>> = MutableLiveData()
    var authenticatedUserLiveData: LiveData<ResponseState<User>> = _authenticatedUserLiveData

    fun signInWithFirebase(email: String, password: String){
        _authenticatedUserLiveData = wordRepository.signInWithFirebase(email, password)
    }

    fun signUpWithFirebase(email: String, password: String){
        _authenticatedUserLiveData = wordRepository.signUpWithFirebase(email, password)
    }
}

class AuthenticationViewModelFactory(private val wordRepository: WordRepository) : ViewModelProvider.NewInstanceFactory(){

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AuthenticationViewModel::class.java)){
            return AuthenticationViewModel(wordRepository) as T
        }

        throw IllegalArgumentException("Unknown ViewModel Class")
    }
}

sealed class ResponseState<T>(
    val data: T? = null,
    val message: String? = null
) {


    class Success<T>(data: T) : ResponseState<T>(data)
    class Error<T>(message: String) : ResponseState<T>(message = message)
    class Loading<T> : ResponseState<T>()

}