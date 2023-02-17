package com.ted.roomwordssample

import android.content.ContentValues
import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.view.inputmethod.InputMethodManager
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
        signUpBinding.cbSignUpShowPassword.setOnCheckedChangeListener { _, isChecked ->
            showPassword(isChecked)
        }

    }

    private fun showPassword(checked: Boolean) {
        if (checked){
            signUpBinding.edSignUpPassword.transformationMethod = HideReturnsTransformationMethod.getInstance()
            signUpBinding.edSignUpConfirmPassword.transformationMethod = HideReturnsTransformationMethod.getInstance()
            if (signUpBinding.edSignUpPassword.hasFocus()){
                signUpBinding.edSignUpPassword.setSelection(signUpBinding.edSignUpPassword.length())
            } else if (signUpBinding.edSignUpConfirmPassword.hasFocus()){
                signUpBinding.edSignUpConfirmPassword.setSelection(signUpBinding.edSignUpConfirmPassword.length())
            }
        }else{
            signUpBinding.edSignUpPassword.transformationMethod = PasswordTransformationMethod.getInstance()
            signUpBinding.edSignUpConfirmPassword.transformationMethod = PasswordTransformationMethod.getInstance()
            if (signUpBinding.edSignUpPassword.hasFocus()){
                signUpBinding.edSignUpPassword.setSelection(signUpBinding.edSignUpPassword.length())
            } else if (signUpBinding.edSignUpConfirmPassword.hasFocus()){
                signUpBinding.edSignUpConfirmPassword.setSelection(signUpBinding.edSignUpConfirmPassword.length())
            }

        }
    }

    private fun validateData() {
        val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(signUpBinding.root.windowToken, 0)
        val email = signUpBinding.edSignUpEmail.text.toString()
        val password = signUpBinding.edSignUpPassword.text.toString()
        val confirmPassword = signUpBinding.edSignUpConfirmPassword.text.toString()

        if (email.isEmpty()){
            Snackbar.make(this, signUpBinding.root, "Please input email", Snackbar.LENGTH_SHORT).show()
        }else if (password.isEmpty()){
            Snackbar.make(this, signUpBinding.root, "Please input password", Snackbar.LENGTH_SHORT).show()
        }else if (password != confirmPassword){
            Snackbar.make(this, signUpBinding.root, "Passwords don't match!", Snackbar.LENGTH_SHORT).show()
        }else{
            CustomAlertDialog.showLoadingDialog(this)
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

