package com.example.mobilt_java23_karl_kowal_lifecycle_v4

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class UserDataActivity : AppCompatActivity() {

    // Deklarera variabler för FirebaseManager och TextViews
    private lateinit var fb: FirebaseManager
    private lateinit var uidTV: TextView
    private lateinit var ageTV: TextView
    private lateinit var driversLicenseTV: TextView
    private lateinit var genderTV: TextView
    private lateinit var nameTV: TextView
    private lateinit var phoneNumberTV: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_data)
        fb = FirebaseManager(this)
        uidTV = findViewById(R.id.userID)
        ageTV = findViewById(R.id.ageTextView)
        driversLicenseTV = findViewById(R.id.drivingLicenseTextView)
        genderTV = findViewById(R.id.genderTextView)
        nameTV = findViewById(R.id.nameTextView)
        phoneNumberTV = findViewById(R.id.phoneNumberTextView)
        val uid = fb.auth.currentUser?.uid

        if(uid != null){
            fetchAndShowData(uid)
        }else{
            Toast.makeText(this,"No UID found!",Toast.LENGTH_SHORT).show()
        }

        val submitBtn = findViewById<Button>(R.id.submitNewCreds)
        val pwTV = findViewById<TextView>(R.id.NewPw)

        submitBtn.setOnClickListener {
            val pw = pwTV.text.toString()
            fb.newPassword(pw){success,erroMsg ->
                if(success){
                    Toast.makeText(this,"Password changed successfully!",Toast.LENGTH_SHORT).show()
                }else{
                    Toast.makeText(this,"Error: $erroMsg",Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
    private fun fetchAndShowData(userID:String){
        fb.getUserData(userID){user ->
            if(user != null){
                uidTV.text = "UID: $userID"
                ageTV.text = "Age: ${user.age}"
                driversLicenseTV.text = "Drivers License: ${if (user.driversLicense == true)"Yes" else "No"}"
                genderTV.text = "Gender: ${user.gender}"
                nameTV.text = "Name: ${user.name}"
                phoneNumberTV.text ="Phone number: ${user.phoneNumber}"
            }else{
                Toast.makeText(this,"Failed to load user data!",Toast.LENGTH_SHORT).show()
            }
        }
    }
}