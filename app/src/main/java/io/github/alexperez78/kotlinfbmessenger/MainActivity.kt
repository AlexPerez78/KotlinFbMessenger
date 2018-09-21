package io.github.alexperez78.kotlinfbmessenger

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        register_button_register.setOnClickListener {
            performRegister()
        }

        already_have_an_account_textView.setOnClickListener {
            Log.d("MainActivity", "Its not yet created")
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

    }

    private fun performRegister(){
        val email = email_editText_registration.text.toString()
        val password = password_editText_registration.text.toString()

        if(email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Username or Password is empty, Please enter the fields.", Toast.LENGTH_SHORT).show()
            return
        }

        Log.d("MainActivity", "Email is " + email)
        Log.d("MainActivity", "Password is: $password")

        //Firebase Authetication to create a user with credentials
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener{
                    if(!it.isSuccessful) return@addOnCompleteListener

                    Log.d("Main","Successful created user with uid: ${it.result.user.uid}")
                }
                .addOnFailureListener{
                    Log.d("Main", "Failed to create user: ${it.message}")
                    Toast.makeText(this, "Failed to create user: ${it.message}", Toast.LENGTH_SHORT).show()
                }
    }
}
