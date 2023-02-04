package com.ted.roomwordssample

import android.content.ContentValues
import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import com.ted.roomwordssample.databinding.ActivitySignUpBinding
import com.ted.roomwordssample.models.WordRoomDatabase
import com.ted.roomwordssample.repository.WordRepository
import com.ted.roomwordssample.utils.CustomAlertDialog
import com.ted.roomwordssample.viewmodels.AuthenticationViewModel
import com.ted.roomwordssample.viewmodels.AuthenticationViewModelFactory
import com.ted.roomwordssample.viewmodels.ResponseState

class SignUpActivity : AppCompatActivity() {

    lateinit var signUpBinding: ActivitySignUpBinding
    val authViewModel: AuthenticationViewModel by lazy {
        ViewModelProvider(
            this,
            AuthenticationViewModelFactory(
                WordRepository(
                    WordRoomDatabase.getDatabase(this)
                )
            )
        )[AuthenticationViewModel::class.java]
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        signUpBinding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(signUpBinding.root)

        signUpBinding.btnSignUp.setOnClickListener { validateData() }
        signUpBinding.txtSignIn.setOnClickListener { switchToSignInScreen() }



    }

    private fun validateData() {
        val email = signUpBinding.edSignUpEmail.text.toString()
        val password = signUpBinding.edSignUpPassword.text.toString()
        val confirmPassword = signUpBinding.edSignUpConfirmPassword.text.toString()

        if (email.isEmpty()){
            Snackbar.make(this, signUpBinding.root, "Please input email", Snackbar.LENGTH_SHORT)
        }else if (password.isEmpty()){
            Snackbar.make(this, signUpBinding.root, "Please input password", Snackbar.LENGTH_SHORT)
        }else if (password != confirmPassword){
            Snackbar.make(this, signUpBinding.root, "Passwords don't match!", Snackbar.LENGTH_SHORT)
        }else{
            authViewModel.signUpWithFirebase(email, password)
            authViewModel.authenticatedUserLiveData.observe(this) { authenticatedUser ->
                when (authenticatedUser) {
                    is ResponseState.Success -> {
                        CustomAlertDialog.dismissLoadingDialog()
                        switchToMainScreen()
                    }
                    is ResponseState.Error -> {
                        authenticatedUser.message.let {
                            Snackbar.make(this, signUpBinding.root, it.toString(), Snackbar.LENGTH_SHORT).show()
                        }
                    }
                    is ResponseState.Loading -> {
                        CustomAlertDialog.showLoadingDialog(this)
                    }
                }
            }
        }
    }

    private fun switchToMainScreen() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    private fun switchToSignInScreen() {
        startActivity(Intent(this, SignInActivity::class.java))
        finish()
    }

    override fun onBackPressed() {
        switchToSignInScreen()
    }

}

