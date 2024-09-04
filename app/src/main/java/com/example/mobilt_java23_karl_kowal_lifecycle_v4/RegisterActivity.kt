package com.example.mobilt_java23_karl_kowal_lifecycle_v4

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class RegisterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val auth = FirebaseAuth.getInstance()

        val emailET = findViewById<EditText>(R.id.email)
        val pwET = findViewById<EditText>(R.id.password)
        val registerButton = findViewById<Button>(R.id.registerBtn)

        registerButton.setOnClickListener {
            val email = emailET.text.toString()
            val pw = pwET.text.toString()
            if(email.isEmpty() || pw.isEmpty()){
                Toast.makeText(this,"Please fill out all fields!",Toast.LENGTH_SHORT).show()
            }
            auth.createUserWithEmailAndPassword(email,pw)
                .addOnCompleteListener { task ->
                    if(task.isSuccessful){
                        Toast.makeText(this,"User added successfully!",Toast.LENGTH_SHORT).show()
                    }else{
                        val errorMsg = task.exception?.message?:"Registration failed!"
                        Toast.makeText(this,errorMsg,Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }
}