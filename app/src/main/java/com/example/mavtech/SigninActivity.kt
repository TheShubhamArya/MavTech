package com.example.mavtech

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity


class SigninActivity : AppCompatActivity() {
    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var signinButton: Button
    private lateinit var signupButton: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.signin_screen)

        emailEditText = findViewById(R.id.etEmail)
        passwordEditText = findViewById(R.id.etPassword)
        signinButton = findViewById(R.id.signinButton)
        signupButton = findViewById(R.id.signupButton)

        signinButton.setOnClickListener {
            signinUser()
        }

        signupButton.setOnClickListener {
            // take to sign up page
//            displayAlertWith("Sign up", "Take user to sign up screen")
            val intent = Intent(this,SignupActivity::class.java)
            startActivity(intent)
        }
    }

    private fun signinUser() {
        val email = emailEditText.text.toString()
        val password = passwordEditText.text.toString()
//        if (hasPassedCredentialsWith(email, password)) {
//            displayAlertWith("Success","The email and password passed valid credential criteria")
            // send the user info to firebase to sign in user
            // Once user is signed in, send user to home screen
            val intent = Intent(this,MainActivity::class.java)
            startActivity(intent)
//        }

    }

    private fun hasPassedCredentialsWith(email: String, password: String): Boolean {
        if (email.isEmpty() || password.isEmpty()) {
            displayAlertWith("Incorrect Credentials", "Email and/or password fields are empty.")
            return false
        }
        if (!email.isEmailValid()) {
            displayAlertWith("Incorrect Email", "This is not a valid email.")
            return false
        }
        if (password.length < 6 || password.length > 18) {
            displayAlertWith("Incorrect Password", "Password must be between 6-18 characters.")
            return false
        }
        return true
    }

    private fun displayAlertWith(title: String, message: String) {
        AlertDialog.Builder(this)
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton("Dismiss") {_,_ ->
                emailEditText.setText("")
                passwordEditText.setText("")
            }
            .show()
    }

    fun String.isEmailValid(): Boolean {
        return !TextUtils.isEmpty(this) && android.util.Patterns.EMAIL_ADDRESS.matcher(this).matches()
    }
}