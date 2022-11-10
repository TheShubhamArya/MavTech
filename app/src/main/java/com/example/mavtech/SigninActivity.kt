package com.example.mavtech

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


/**
 *
 */
class SigninActivity : AppCompatActivity() {
    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var signinButton: Button
//    private lateinit var signupButton: Button
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.signin_screen)
        // initializing all the late init variables
        firebaseAuth = Firebase.auth
        emailEditText = findViewById(R.id.etEmail)
        passwordEditText = findViewById(R.id.etPassword)
        signinButton = findViewById(R.id.signinButton)
        // functionality to check if user is already signed in. If they are
        // then just go to the home screen
        val currentUser = firebaseAuth.currentUser
        if(currentUser != null){
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
        signinButton.setOnClickListener {
            signinUser()
        }
    }

    fun String.isEmailValid(): Boolean {
        return !TextUtils.isEmpty(this) && android.util.Patterns.EMAIL_ADDRESS.matcher(this)
            .matches()
    }

    fun displayAlertWith(s: String, s1: String) {
        AlertDialog.Builder(this)
            .setTitle(s)
            .setMessage(s1)
            .setPositiveButton("Dismiss") { _, _ ->
                emailEditText.setText("")
                passwordEditText.setText("")
            }
            .show()
    }

    fun hasPassedCredentialsWith(email: String, password: String): Boolean {
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

    fun signinUser() {
        val email = emailEditText.text.toString()
        val password = passwordEditText.text.toString()
        if (hasPassedCredentialsWith(email, password)) {
            // send the user info to firebase to sign in user
            // Once user is signed in, send user to home screen
            firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
                if (it.isSuccessful) {
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                } else {
                    Toast.makeText(this, it.exception.toString(), Toast.LENGTH_SHORT).show()
                }
            }
        } else {
            displayAlertWith(
                "Error",
                "The email and password did not pass valid credential criteria."
            )
        }
    }
}

