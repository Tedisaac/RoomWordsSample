package com.ted.roomwordssample

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
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
    private val authViewModel: AuthenticationViewModel by lazy {
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
        signInBinding.cbSignInShowPassword.setOnCheckedChangeListener{ _, isChecked ->
            showPassword(isChecked)
        }

    }

    private fun showPassword(checked: Boolean) {
        if (checked){
            signInBinding.edSignInPassword.transformationMethod = HideReturnsTransformationMethod.getInstance()
            signInBinding.edSignInPassword.setSelection(signInBinding.edSignInPassword.length())
        }else{
            signInBinding.edSignInPassword.transformationMethod = PasswordTransformationMethod.getInstance()
            signInBinding.edSignInPassword.setSelection(signInBinding.edSignInPassword.length())
        }
    }

    private fun validateData() {
        val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(signInBinding.root.windowToken, 0)
        val email = signInBinding.edSignInEmail.text.toString()
        val password = signInBinding.edSignInPassword.text.toString()
        if (signInBinding.edSignInEmail.text.isEmpty()) {
            Snackbar.make(this, signInBinding.root, "Please input email", Snackbar.LENGTH_SHORT).show()
        } else if (signInBinding.edSignInPassword.text.isEmpty()) {
            Snackbar.make(this, signInBinding.root, "Please input password", Snackbar.LENGTH_SHORT).show()
        } else {
            CustomAlertDialog.showLoadingDialog(this)
            authViewModel.signInWithFirebase(email, password)
            authViewModel.authenticatedUserLiveData.observe(this) { authenticatedUser ->
                Log.e(TAG, "validateData: $authenticatedUser")
                when (authenticatedUser) {
                    is ResponseState.Success -> {
                        Log.e(TAG, "validateData: ${authenticatedUser.data}")
                        CustomAlertDialog.dismissLoadingDialog()
                        switchToMainScreen()
                    }
                    is ResponseState.Error -> {
                        CustomAlertDialog.dismissLoadingDialog()
                        authenticatedUser.message.let {
                            Snackbar.make(
                                this,
                                signInBinding.root,
                                it.toString(),
                                Snackbar.LENGTH_SHORT
                            ).show()
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