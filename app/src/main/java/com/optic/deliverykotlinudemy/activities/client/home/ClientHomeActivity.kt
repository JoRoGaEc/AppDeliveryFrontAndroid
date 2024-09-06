package com.optic.deliverykotlinudemy.activities.client.home

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import com.google.gson.Gson
import com.optic.deliverykotlinudemy.R
import com.optic.deliverykotlinudemy.activities.MainActivity
import com.optic.deliverykotlinudemy.models.User
import com.optic.deliverykotlinudemy.utils.SharedPref

class ClientHomeActivity : AppCompatActivity() {
    private val TAG  = "ClientHomeActivity"
    var buttonLogout: Button?= null
    var sharedPref: SharedPref?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_client_home)

        sharedPref =  SharedPref(this)
        buttonLogout =  findViewById(R.id.btn_logout)
        buttonLogout?.setOnClickListener{
            logout()
        }
        getUserFromSession()
    }
    private fun logout(){
        sharedPref?.remove("user")
        val i = Intent(this, MainActivity::class.java)
        startActivity(i)
    }

    private fun getUserFromSession(){
        val gson =  Gson()
        if(!sharedPref?.getData("user").isNullOrBlank()){
            //The user exist in session
            val user = gson.fromJson(sharedPref?.getData("user"), User::class.java)
            Log.d(TAG, "User: $user")
        }
    }
}