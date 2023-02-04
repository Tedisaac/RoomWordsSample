package com.ted.roomwordssample

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import com.ted.roomwordssample.databinding.ActivitySignInBinding
import com.ted.roomwordssample.models.WordRoomDatabase
import com.ted.roomwordssample.repository.WordRepository
import com.ted.roomwordssample.utils.CustomAlertDialog
import com.ted.roomwordssample.viewmodels.AuthenticationViewModel
import com.ted.roomwordssample.viewmodels.AuthenticationViewModelFactory
import com.ted.roomwordssample.viewmodels.ResponseState

class SignInActivity : AppCompatActivity() {

    private lateinit var signInBinding: ActivitySignInBinding
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

        signInBinding = ActivitySignInBinding.inflate(layoutInflater)
        setContentView(signInBinding.root)

        signInBinding.btnSignIn.setOnClickListener { validateData() }
        signInBinding.txtCreateAccount.setOnClickListener { switchToSignUpScreen() }

    }

    private fun validateData() {
        val email = signInBinding.edSignInEmail.text.toString()
        val password = signInBinding.edSignInPassword.text.toString()

        if (email.isEmpty()){
            Snackbar.make(this, signInBinding.root, "Please input email", Snackbar.LENGTH_SHORT)
        }else if (password.isEmpty()){
            Snackbar.make(this, signInBinding.root, "Please input password", Snackbar.LENGTH_SHORT)
        }else{

           authViewModel.signInWithFirebase(email, password)
            authViewModel.authenticatedUserLiveData.observe(this) { authenticatedUser ->
                when (authenticatedUser) {
                    is ResponseState.Success -> {
                        CustomAlertDialog.dismissLoadingDialog()
                        switchToMainScreen()
                    }
                    is ResponseState.Error -> {
                        authenticatedUser.message.let {
                            Snackbar.make(this, signInBinding.root, it.toString(), Snackbar.LENGTH_SHORT).show()
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

    private fun switchToSignUpScreen() {
        startActivity(Intent(this, SignUpActivity::class.java))
        finish()
    }

}