package com.example.mavtech

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity

class SignupActivity: AppCompatActivity() {

    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var signupButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.signup_screen)
        emailEditText = findViewById(R.id.etEmail)
        passwordEditText = findViewById(R.id.etPassword)
        signupButton = findViewById(R.id.signupButton)

        signupButton.setOnClickListener {
            // validate user sign up information
            // pass info to Firebase to create new account
            // When firebase creates new account, take user to the home screen
            val intent = Intent(this,MainActivity::class.java)
            startActivity(intent)
        }
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
