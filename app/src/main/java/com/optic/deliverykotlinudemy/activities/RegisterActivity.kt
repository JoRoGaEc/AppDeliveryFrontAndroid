package com.optic.deliverykotlinudemy.activities

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.optic.deliverykotlinudemy.R
import com.optic.deliverykotlinudemy.models.ResponseHttp
import com.optic.deliverykotlinudemy.models.User
import com.optic.deliverykotlinudemy.providers.UsersProvider
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterActivity : AppCompatActivity() {
    val TAG = "RegisterActivity"
    var imageViewGoToLogin:ImageView? = null

    var editTextName: EditText? = null
    var editTextLastname: EditText? = null
    var editTextEmail :  EditText? = null
    var editTextPhone : EditText?= null
    var editTextPassword: EditText?= null
    var editTextConfirmPassword: EditText?= null

    var buttonRegister: Button?= null


    var usersProvider =  UsersProvider()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        imageViewGoToLogin  =  findViewById(R.id.imageview_go_to_login)

        editTextName  =findViewById(R.id.edittext_name)
        editTextLastname  =findViewById(R.id.edittext_lastname)
        editTextEmail  =findViewById(R.id.edittext_email)
        editTextPhone  =findViewById(R.id.edittext_phone)
        editTextPassword  =findViewById(R.id.edittext_password)
        editTextConfirmPassword  =findViewById(R.id.edittext_confirm_password)

        buttonRegister = findViewById(R.id.btn_register)
        imageViewGoToLogin?.setOnClickListener{
            goToLogin()
        }


        buttonRegister?.setOnClickListener{
            register()
        }
    }
    private fun register(){
        val name = editTextName?.text.toString()
        val lastname = editTextLastname?.text.toString()
        val email = editTextEmail?.text.toString()
        val phone = editTextPhone?.text.toString()
        val password = editTextPassword?.text.toString()
        val confirmPassword = editTextConfirmPassword?.text.toString()

        if(isValidForm(
                name = name,
                lastname = lastname,
                email =  email,
                phone =  phone,
                password =  password,
                confirmPassword =  confirmPassword
            )){
            val  user =  User(
                name = name,
                lastname = lastname,
                email = email,
                phone = phone,
                password = password
            )
            usersProvider.register(user)?.enqueue(object : Callback<ResponseHttp>{
                override fun onResponse(
                    call: Call<ResponseHttp>,
                    response: Response<ResponseHttp>
                ) {
                    Toast.makeText(this@RegisterActivity, response.body()?.message, Toast.LENGTH_LONG).show()
                    Log.d(TAG, "Response: ${response}")
                    Log.d(TAG, "Body: ${response.body()}")
                }

                override fun onFailure(call: Call<ResponseHttp>, t: Throwable) {
                    Log.d(TAG, "Se produjo un error ${t.message}")
                    Toast.makeText(this@RegisterActivity, "Se produjo un error ${t.message}", Toast.LENGTH_LONG).show()
                }

            })
        }else{
            Toast.makeText(this, "Formulario no  válido", Toast.LENGTH_LONG).show()

        }

        /*Los Toast hacen que aparezca en la app*/
        /*Toast.makeText(this, "El name es: $name", Toast.LENGTH_LONG).show()
        Toast.makeText(this, "El lastname es: $lastname", Toast.LENGTH_LONG).show()
        Toast.makeText(this, "El email es: $email", Toast.LENGTH_LONG).show()
        Toast.makeText(this, "El phone es: $phone", Toast.LENGTH_LONG).show()
        Toast.makeText(this, "El password es: $password", Toast.LENGTH_LONG).show()
        Toast.makeText(this, "El confirmPassword es: $confirmPassword", Toast.LENGTH_LONG).show()*/

        /*Esto para que imprima en consola*/
        Log.d("MainActivity","El name es $name")
        Log.d("MainActivity","El lastname es $lastname")
        Log.d("MainActivity","El email es $email")
        Log.d("MainActivity","El phone es $phone")
        Log.d("MainActivity","El password es $password")
        Log.d("MainActivity","El confirmPassword es $confirmPassword")

    }
    fun String.isEmailValid():Boolean{
        return !TextUtils.isEmpty(this) && android.util.Patterns.EMAIL_ADDRESS.matcher(this).matches();
    }
    private fun isValidForm(name:String, lastname:String,
                            email:String, phone:String,
                            password: String, confirmPassword:String): Boolean{
        if(name.isBlank()){
            return false
        }
        if(lastname.isBlank()){
            return false
        }
        if(email.isBlank()){
            return false
        }
        if(!email.isEmailValid()){
            return false
        }

        if(phone.isBlank()){
            return false
        }
        if(password.isBlank()){
            return false
        }

        if(confirmPassword.isBlank()){
            return false
        }
        if(password != confirmPassword){
            Toast.makeText(this, "Las contraseñas no coinciden", Toast.LENGTH_LONG).show()
            return false
        }
        return true

    }
    /*Para redireccionar al Login*/
    private fun goToLogin(){
        val i =  Intent(this, MainActivity::class.java)
        startActivity(i)
    }
}