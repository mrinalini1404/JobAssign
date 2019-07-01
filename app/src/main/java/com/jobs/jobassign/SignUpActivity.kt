package com.jobs.jobassign

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.firebase.auth.FirebaseAuth
import com.jobs.jobassign.LoginActivity
import com.jobs.jobassign.R
import kotlinx.android.synthetic.main.activity_sign_up.*

class SignUpActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        auth = FirebaseAuth.getInstance()

        Signed_up.setOnClickListener {
            signUpUser()
        }
    }

    private fun signUpUser() {
        if (sign_username.text.toString().isEmpty()) {
            sign_username.error = "Please enter email"
            sign_username.requestFocus()
            return
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(sign_username.text.toString()).matches()) {
            sign_username.error = "Please enter valid email"
            sign_username.requestFocus()
            return
        }

        if (sign_password.text.toString().isEmpty()) {
            sign_password.error = "Please enter password"
            sign_password.requestFocus()
            return
        }

        auth.createUserWithEmailAndPassword(sign_username.text.toString(), sign_password.text.toString())
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    user?.sendEmailVerification()
                        ?.addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                startActivity(Intent(this,LoginActivity::class.java))
                                finish()
                            }
                        }
                } else {
                    Toast.makeText(baseContext, "Sign Up failed. Try again after some time.",
                        Toast.LENGTH_SHORT).show()
                }
            }


    }

    override fun onBackPressed() {
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }
}