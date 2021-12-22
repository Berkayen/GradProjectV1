package com.example.movieappproject.activities

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Button
import android.widget.LinearLayout
import com.example.movieappproject.R
import com.example.movieappproject.utils.MSPButton
import com.example.movieappproject.utils.MSPEditText
import com.example.movieappproject.utils.MSPTextView
import com.example.movieappproject.utils.MSPTextViewBold
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : BaseActivity(), View.OnClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        val tvForgotPassword = findViewById<MSPTextView>(R.id.tv_forgot_password)
        tvForgotPassword.setOnClickListener(this)
        val btnLogin = findViewById<MSPButton>(R.id.btn_login)
        btnLogin.setOnClickListener(this)
        val tvRegister = findViewById<MSPTextViewBold>(R.id.tv_register)
        tvRegister.setOnClickListener(this)
    }

    override fun onClick(view: View?) {
        if (view != null) {
            when (view.id) {

                R.id.tv_forgot_password -> {
                    val intent = Intent(this@LoginActivity, ForgotPasswordActivity::class.java)
                    startActivity(intent)
                }

                R.id.btn_login -> {
                    logInRegisteredUser()
                }

                R.id.tv_register -> {
                    // Launch the register screen when the user clicks on the text.
                    val intent = Intent(this@LoginActivity, RegisterActivity::class.java)
                    startActivity(intent)
                }
            }
        }
    }
    private fun validateLoginDetails(): Boolean {
        val etEmail = findViewById<MSPEditText>(R.id.et_email)
        val etPassword = findViewById<MSPEditText>(R.id.et_password)
        return when {
            TextUtils.isEmpty(etEmail.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_email), true)
                false
            }
            TextUtils.isEmpty(etPassword.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_password), true)
                false
            }
            else -> {
                true
            }
        }
    }
    private fun logInRegisteredUser(){
        val etEmail = findViewById<MSPEditText>(R.id.et_email)
        val etPassword = findViewById<MSPEditText>(R.id.et_password)

        if (validateLoginDetails()){
            // Show the progress dialog
            showProgress(resources.getString(R.string.please_wait))

            //Get the text from editText and trim the space
            val email: String = etEmail.text.toString().trim{it <= ' '}
            val password: String = etPassword.text.toString().trim(){it <= ' '}

            //Log-in using FirebaseAuth
            FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                .addOnCompleteListener{task ->
                    hideProgressDialog()
                    if (task.isSuccessful){
                        // TODO - Send user to main activity
                        showErrorSnackBar("You logged in successfully.", false)
                    }else{
                        showErrorSnackBar(task.exception!!.message.toString(), true)
                    }

            }
        }
    }
}