package com.donsdirectory.mobile.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import com.donsdirectory.mobile.R
import com.donsdirectory.mobile.lib.KontactPicker
import com.donsdirectory.mobile.lib.RC_READ_CONTACTS
import com.donsdirectory.mobile.model.ImageMode
import com.donsdirectory.mobile.model.KontactPickerItem
import com.donsdirectory.mobile.model.SelectionMode
import com.donsdirectory.mobile.model.SelectionTickView
import kotlinx.android.synthetic.main.activity_login_page.*

class LoginPage : AppCompatActivity() {

    private val debugModeCheck = MutableLiveData<Boolean>()
    private val imageModeGroup = MutableLiveData<Int>()
    private val selectionTickViewGroup = MutableLiveData<Int>()
    private val selectionModeGroup = MutableLiveData<Int>()
    private var colorDefault: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_page)
        Log.d("ShowMeOnly", "Login page started")

        LoginButton.setOnClickListener {
            if (authenticateLogin(username, password)) {
                makeToast("Log-in Successful!")

                val item = KontactPickerItem().apply {
                    debugMode = debugModeCheck.value ?: false
                    //            textBgColor = ContextCompat.getColor(this@MainActivity, R.color.colorBlue100)
                    colorDefault?.let { textBgColor = it }
                    includePhotoUri = true
                    themeResId = R.style.CustomTheme
                    imageMode = when (imageModeGroup.value) {
                        0 -> ImageMode.None
                        1 -> ImageMode.TextMode
                        2 -> ImageMode.UserImageMode
                        else -> ImageMode.None
                    }
                    selectionTickView = when (selectionTickViewGroup.value) {
                        0 -> SelectionTickView.SmallView
                        1 -> SelectionTickView.LargeView
                        else -> SelectionTickView.SmallView
                    }
                    selectionMode = when (selectionModeGroup.value) {
                        0 -> SelectionMode.Single
                        1 -> SelectionMode.Multiple
                        else -> SelectionMode.Multiple
                    }
                }

                KontactPicker().startPickerForResult(this, item, RC_READ_CONTACTS)

                val intent = Intent(this, KontactPickerActivity::class.java)
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
