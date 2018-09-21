package io.github.alexperez78.kotlinfbmessenger

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_login)

        login_button_login.setOnClickListener {
            val email = email_textView_login.text.toString()
            val password = password_textView_login.text.toString()

            FirebaseAuth.getInstance().signInWithEmailAndPassword(email,password)
        }

        back_to_register_textView.setOnClickListener {
            finish()
        }
    }
}
