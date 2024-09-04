package com.example.mobilt_java23_karl_kowal_lifecycle_v4

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        //Initierar UI-element.
        val emailText = findViewById<EditText>(R.id.email)
        val pwText = findViewById<EditText>(R.id.password)
        val loginButton = findViewById<Button>(R.id.loginButton)


        loginButton.setOnClickListener {
            val email = emailText.text.toString()
            val pw = pwText.text.toString()
            //Kollar så alla fields är ifylda. (Kanske kan göras direkt på komponenten?)
            if(pw.isNotEmpty() && email.isNotEmpty()){
                //Initierar firebase innan den behövs för att skicka infon.
                val firebaseManager = FirebaseManager(this)
                firebaseManager.signIn(email,pw){success,errorMsg ->
                    if(success){
                        Toast.makeText(this,"Login successful!",Toast.LENGTH_SHORT).show()
                        val intent = Intent(this,FormActivity::class.java)
                        startActivity(intent)
                    }else{
                        Toast.makeText(this,"Error: $errorMsg",Toast.LENGTH_SHORT).show()
                    }
                }
            }else{
                Toast.makeText(this,"Please fill out all fields!",Toast.LENGTH_SHORT).show()
            }
        }


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}