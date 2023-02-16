package com.ted.roomwordssample.viewmodels

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.*
import com.ted.roomwordssample.models.User
import com.ted.roomwordssample.repository.WordRepository
import kotlinx.coroutines.launch

class AuthenticationViewModel constructor(private val wordRepository: WordRepository): ViewModel() {

    private var _authenticatedUserLiveData:  MutableLiveData<ResponseState<User>> = MutableLiveData()
    val authenticatedUserLiveData: LiveData<ResponseState<User>>
    get() = _authenticatedUserLiveData

    fun signInWithFirebase(email: String, password: String){
        viewModelScope.launch {
            _authenticatedUserLiveData = wordRepository.signInWithFirebase(email, password)
            Log.e(TAG, "signInWithFirebase: ${_authenticatedUserLiveData.value}")
        }

    }

    fun signUpWithFirebase(email: String, password: String){
        viewModelScope.launch {
            _authenticatedUserLiveData = wordRepository.signUpWithFirebase(email, password)
        }

    }

    fun signOutUser(){
        wordRepository.signOutUser()
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