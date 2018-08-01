package io.github.alexperez78.kotlinfbmessenger

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        register_button_register.setOnClickListener {
            val email = email_editText_registration.text.toString()
            val password = password_editText_registration.text.toString()

            Log.d("MainActivity", "Email is " + email)
            Log.d("MainActivity", "Password is: $password")

            //Firebase Authetication to create a user with credentials
        }

        already_have_an_account_textView.setOnClickListener {
            Log.d("MainActivity", "Its not yet created")
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

    }
}
