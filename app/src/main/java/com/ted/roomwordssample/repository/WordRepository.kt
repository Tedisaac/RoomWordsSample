package com.ted.roomwordssample.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.ted.roomwordssample.interfaces.AuthInterface
import com.ted.roomwordssample.models.User
import com.ted.roomwordssample.models.WordRoomDatabase
import com.ted.roomwordssample.models.Word
import com.ted.roomwordssample.viewmodels.ResponseState

class WordRepository(private val wordRoomDatabase: WordRoomDatabase) : AuthInterface {

    suspend fun insertWord(word: Word) = wordRoomDatabase.wordDao().insert(word)

    suspend fun deleteAllWords() = wordRoomDatabase.wordDao().deleteAll()

    suspend fun deleteWord(word: String) = wordRoomDatabase.wordDao().deleteWord(word)

    fun getAllWords() : LiveData<List<Word>> = wordRoomDatabase.wordDao().getAllWords()



    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()

    override suspend fun signUpWithFirebase(email: String, password: String) : MutableLiveData<ResponseState<User>> {
        val authenticatedUser: MutableLiveData<ResponseState<User>> = MutableLiveData()

        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener{ task->
            if (task.isSuccessful){
                val isNewUser = task.result.additionalUserInfo?.isNewUser
                val firebaseUser = firebaseAuth.currentUser
                if (firebaseUser != null){
                    val uid = firebaseUser.uid
                    val name = firebaseUser.displayName
                    val email = firebaseUser.email
                    val user = User(uid = uid, name = name, email = email)
                    user.isNew = isNewUser
                    authenticatedUser.value = ResponseState.Success(user)
                }
            }else{
                authenticatedUser.value = task.exception?.message?.let {
                    ResponseState.Error(it)
                }
            }
        }
        return authenticatedUser
    }

    override suspend fun signInWithFirebase(email: String, password: String) : MutableLiveData<ResponseState<User>> {
        val authenticatedUser: MutableLiveData<ResponseState<User>> = MutableLiveData()

        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener{ task->
            if (task.isSuccessful){
                val isNewUser = task.result.additionalUserInfo?.isNewUser
                val firebaseUser = firebaseAuth.currentUser
                if (firebaseUser != null){
                    val uid = firebaseUser.uid
                    val name = firebaseUser.displayName
                    val email = firebaseUser.email
                    val user = User(uid = uid, name = name, email = email)
                    user.isNew = isNewUser
                    authenticatedUser.value = ResponseState.Success(user)
                }
            }else{
                authenticatedUser.value = task.exception?.message?.let {
                    ResponseState.Error(it)
                }
            }
        }
        return authenticatedUser
    }

    override fun signOutUser() {
        firebaseAuth.signOut()
    }

}