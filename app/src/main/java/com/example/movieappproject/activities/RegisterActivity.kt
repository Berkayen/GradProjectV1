package com.example.movieappproject.activities

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.WindowInsets
import android.view.WindowManager
import androidx.appcompat.widget.AppCompatCheckBox
import androidx.appcompat.widget.Toolbar
import com.example.movieappproject.R
import com.example.movieappproject.utils.MSPButton
import com.example.movieappproject.utils.MSPEditText
import com.example.movieappproject.utils.MSPTextViewBold
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser


class RegisterActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        setupActionBar()

        val tvLogin = findViewById<MSPTextViewBold>(R.id.tv_login)
        tvLogin.setOnClickListener{
            onBackPressed()
        }
        val btnRegister = findViewById<MSPButton>(R.id.btn_register)
        btnRegister.setOnClickListener{
            registerUser()
        }
    }


    private fun setupActionBar(){
        val toolbarRegisterAct: Toolbar = findViewById(R.id.toolbar_register_activity)
        setSupportActionBar(toolbarRegisterAct)

        val actionBar = supportActionBar
        if (actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_black_color_back_24dp)
        }
        toolbarRegisterAct.setNavigationOnClickListener{onBackPressed()}
    }


    private fun validateRegisterDetails(): Boolean {
        val etFirstName = findViewById<MSPEditText>(R.id.et_first_name)
        val etLastName = findViewById<MSPEditText>(R.id.et_last_name)
        val etEmail = findViewById<MSPEditText>(R.id.et_email)
        val etPassword = findViewById<MSPEditText>(R.id.et_password)
        val etConfirmPassword = findViewById<MSPEditText>(R.id.et_confirm_password)
        val cbTermsandConditions  = findViewById<AppCompatCheckBox>(R.id.cb_terms_and_condition)

        return when {
            TextUtils.isEmpty(etFirstName.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_first_name), true)
                false
            }

            TextUtils.isEmpty(etLastName.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_last_name), true)
                false
            }

            TextUtils.isEmpty(etEmail.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_email), true)
                false
            }

            TextUtils.isEmpty(etPassword.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_password), true)
                false
            }

            TextUtils.isEmpty(etConfirmPassword.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_confirm_password), true)
                false
            }

            etPassword.text.toString().trim { it <= ' ' } != etConfirmPassword.text.toString()
                .trim { it <= ' ' } -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_password_and_confirm_password_mismatch), true)
                false
            }
            !cbTermsandConditions.isChecked -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_agree_terms_and_condition), true)
                false
            }
            else -> {
                //showErrorSnackBar("Thank you for registration", false)
                true
            }
        }
    }
    private fun registerUser(){
        val etEmail = findViewById<MSPEditText>(R.id.et_email)
        val etPassword = findViewById<MSPEditText>(R.id.et_password)

        //Check with validate function if the entries are valid or not.
        if(validateRegisterDetails()){

            showProgress(resources.getString(R.string.please_wait))
            val email: String = etEmail.text.toString().trim{it <= ' '}
            val password: String = etPassword.text.toString().trim(){it <= ' '}

            //Create an instance and create a register a user with email and password.
            FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(
                    OnCompleteListener<AuthResult> { task ->
                        hideProgressDialog()

                        //If the registration is successfully done.
                        if (task.isSuccessful){

                            //Firebase registered user
                            val firebaseUser: FirebaseUser = task.result!!.user!!
                            showErrorSnackBar(
                                "You registered successfully. Your user id is ${firebaseUser.uid}",
                                false
                            )
                            FirebaseAuth.getInstance().signOut()
                            finish()

                        }else{
                            showErrorSnackBar(task.exception!!.message.toString(), true)
                        }
                    }
                )
        }
    }
}