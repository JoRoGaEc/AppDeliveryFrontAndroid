package com.optic.deliverykotlinudemy.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import com.google.gson.Gson
import com.optic.deliverykotlinudemy.R
import com.optic.deliverykotlinudemy.activities.client.home.ClientHomeActivity
import com.optic.deliverykotlinudemy.models.ResponseHttp
import com.optic.deliverykotlinudemy.models.User
import com.optic.deliverykotlinudemy.providers.UsersProvider
import com.optic.deliverykotlinudemy.utils.SharedPref
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    var imageViewGoToRegister: ImageView? = null //NULL SAFETY
    var editTextEmail:  EditText? = null
    var editTextPassword:  EditText? = null
    var buttonLogin:    Button? = null;

    var usersProvider = UsersProvider()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        /*Este método infla el layout (lo convierte de XML a objetos de vista en memoria) y lo asigna a la actividad actual.
        * Después de inflar el layout, las vistas definidas en el archivo XML (activity_main.xml) se convierten en objetos
        * de la interfaz de usuario.
        * En Android, los elementos de la UI son instancias de la clase View o de sus subclases. Algunos ejemplos comunes de vistas son:
            TextView: Una vista que muestra texto.
            ImageView: Una vista que muestra una imagen.
            Button: Un botón que el usuario puede pulsar.
            EditText: Un campo de texto donde el usuario puede escribir.
            LinearLayout, RelativeLayout, ConstraintLayout: Contenedores que organizan otras vistas dentro de ellos.
            Cuando inflas un layout XML con setContentView(R.layout.activity_main), Android convierte cada elemento XML en su
            correspondiente instancia de clase de vista en memoria. Por ejemplo:

            Un <TextView> en el XML se convierte en una instancia de la clase TextView.
            Un <ImageView> en el XML se convierte en una instancia de la clase ImageView.
            Un <Button> se convierte en una instancia de la clase Button, y así sucesivamente.
        * */

        /*
        * Inflar el layout significa convertir esas definiciones XML en objetos de vista reales que pueden ser manipulados programáticamente.
        * */
        /*R es una clase generada automáticamente que contiene referencias a todos los recursos de la aplicación,
        como layouts, cadenas de texto, imágenes, y otros elementos definidos en archivos XML.
        */

        /*instancia para hacer referencia al boton que tiene ese nombre imageview_go_to_register*/
        imageViewGoToRegister = findViewById(R.id.imageview_go_to_register)
        editTextEmail  =findViewById(R.id.edittext_email)
        editTextPassword  =findViewById(R.id.edittext_password)
        buttonLogin = findViewById(R.id.btn_login)
        /*Le establecemos el metodo onClick
        si imageViewGoToRegister no va ejecutar el metodo setOnClickListener*/
        imageViewGoToRegister?.setOnClickListener {
            goToRegister()  //para redirigir a esa pantalla
        }

        buttonLogin?.setOnClickListener{
            login()
        }
        getUserFromSession()
    }
    private fun goToClientHome(){
        val i =  Intent(this, ClientHomeActivity::class.java)
        startActivity(i)
    }

    private fun login(){
        val email = editTextEmail?.text.toString()
        val password = editTextPassword?.text.toString()


        if(isValidForm(email, password)){
            usersProvider.login(email, password)?.enqueue(object: Callback<ResponseHttp>{
                override fun onResponse(
                    call: Call<ResponseHttp>,
                    response: Response<ResponseHttp>
                ) {
                    Log.d("MainActivity","Response : ${response.body()}")
                    if(response.body()?.success == true){
                        Toast.makeText(this@MainActivity, "Response : ${response.body()?.message}", Toast.LENGTH_LONG).show()
                        saveUserInSession(response.body()?.data.toString())
                        goToClientHome()
                    }else{
                        Toast.makeText(this@MainActivity, "Not valid credentials", Toast.LENGTH_LONG).show()

                    }
                }

                override fun onFailure(call: Call<ResponseHttp>, t: Throwable) {
                    Toast.makeText(this@MainActivity, "There was an error", Toast.LENGTH_LONG).show()
                }

            })
        }else{
            Toast.makeText(this, "Formulario no  válido", Toast.LENGTH_LONG).show()

        }
    }
    private fun getUserFromSession(){
        val sharedPref =  SharedPref(this)
        val gson =  Gson()
        if(!sharedPref.getData("user").isNullOrBlank()){
            //The user exist in session
            val user = gson.fromJson(sharedPref.getData("user"), User::class.java)
            goToClientHome()
        }
    }

    private fun saveUserInSession(data: String){
        val sharePref =SharedPref(this)
        val gson = Gson()
        val user =  gson.fromJson(data, User::class.java)
        sharePref.save("user", user)
    }

    fun String.isEmailValid():Boolean{
        return !TextUtils.isEmpty(this) && android.util.Patterns.EMAIL_ADDRESS.matcher(this).matches();
    }
    private fun isValidForm(email:String, password: String):Boolean{
            if(email.isBlank()){
                return false
            }
            if(password.isBlank()){
                return false
            }
            if(!email.isEmailValid()){
                return false
            }
        return true;

    }
    private fun goToRegister() {
        val i = Intent(this, RegisterActivity::class.java)
        startActivity(i)
    }


}