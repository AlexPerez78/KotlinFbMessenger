package io.github.alexperez78.kotlinfbmessenger

import android.app.Activity
import android.content.Intent
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_register.*
import java.util.*

class RegisterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        register_button_register.setOnClickListener {
            performRegister()
        }

        already_have_an_account_textView.setOnClickListener {
            Log.d("RegisterActivity", "Its not yet created")
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        selectphoto_button_register.setOnClickListener {
            Log.d("RegisterActivity", "try to show photo selector")

            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, 0)
        }
    }

    var selectPhotoURI:Uri? = null

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 0 && resultCode == Activity.RESULT_OK && data != null){
            Log.d("RegisterActivity", "Photo was Selected")
        }

        selectPhotoURI = data?.data //Image URI (Tells us where it is located)
        val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, selectPhotoURI)

        select_photo_Imageview_register.setImageBitmap(bitmap)
        selectphoto_button_register.alpha = 0f
        /*val bitmapDrawable = BitmapDrawable(bitmap)
        selectphoto_button_register.setBackgroundDrawable(bitmapDrawable)*/
    }



    private fun performRegister(){
        val email = email_editText_registration.text.toString()
        val password = password_editText_registration.text.toString()

        if(email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Username or Password is empty, Please enter the fields.", Toast.LENGTH_SHORT).show()
            return
        }

        Log.d("RegisterActivity", "Email is " + email)
        Log.d("RegisterActivity", "Password is: $password")

        //Firebase Authetication to create a user with credentials
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener{
                    if(!it.isSuccessful) return@addOnCompleteListener

                    Log.d("Main","Successful created user with uid: ${it.result.user.uid}")

                    uploadImageToFirebaseStorage()
                }
                .addOnFailureListener{
                    Log.d("Main", "Failed to create user: ${it.message}")
                    Toast.makeText(this, "Failed to create user: ${it.message}", Toast.LENGTH_SHORT).show()
                }
    }

    private fun uploadImageToFirebaseStorage(){
        if(selectPhotoURI == null) return

        val filename = UUID.randomUUID().toString()
        val ref = FirebaseStorage.getInstance().getReference("/images/$filename")

        ref.putFile(selectPhotoURI!!)
                .addOnSuccessListener {
                    Log.d("RegisterActivity", "Successfully uploaded image: ${it.metadata?.path}")

                    ref.downloadUrl.addOnSuccessListener {
                        Log.d("RegisterActivity", "File Location: $it")

                        saveUserToFirebaseDatabase(it.toString())
                    }
                }
                .addOnCanceledListener {
                    //Add some code to save from crash
                }
    }

    private fun saveUserToFirebaseDatabase(profileImageUrl: String){
        val uid = FirebaseAuth.getInstance().uid ?: ""
        val ref = FirebaseDatabase.getInstance().getReference("/users/$uid")
        val user = User(uid, username_editText_register.text.toString(), profileImageUrl)
        ref.setValue(user)
                .addOnSuccessListener {
                    Log.d("RegisterActivity", "Finally we saved the user to Firebase Database")
                }
                .addOnCanceledListener {
                    //Add some code to save from crashing
                }
    }
}

class User(val uid: String, val username: String, val profileImageUrl: String)
