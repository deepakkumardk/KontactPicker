package com.donsdirectory.mobile

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_login_page.*

class LoginPage : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_page)
        Log.d("ShowMeOnly", "Login page started")

        LoginButton.setOnClickListener {
            if (authenticateLogin(username, password)) {
                makeToast("Log-in Successful!")

                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            } else {
                makeToast("Invalid username or password")
            }
        }
    }

    //Toasts are little cheery text boxes or speech bubbles that appear on the screen for a few
    //seconds and then disappear.
    private fun makeToast(toastText: String) {
        val toastDuration = Toast.LENGTH_SHORT
        val toast = Toast.makeText(applicationContext, toastText, toastDuration)
        toast.show()
    }

    //This function receives the username and password entered and performs a server call to check
    //if the login was successful before bringing the user to the main screen
    private fun authenticateLogin(username: EditText, password: EditText): Boolean {
        makeToast("Authenticating...")
        return username.text.isNotBlank() && password.text.isNotBlank()
    }
}
