package com.example.mobilt_java23_karl_kowal_lifecycle_v4

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class FormActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_form)



        //UI-Komponenter
        val ageText = findViewById<EditText>(R.id.ageEditText)
        val driversLicenseCB = findViewById<CheckBox>(R.id.drivingLicenseCheckBox)
        val genderRadioGrp = findViewById<RadioGroup>(R.id.genderRadioGroup)
        val nameEditText = findViewById<EditText>(R.id.nameEditText)
        val phoneNumberEditText = findViewById<EditText>(R.id.phoneEditText)
        val submitButton = findViewById<Button>(R.id.submitButton)

        submitButton.setOnClickListener {
            val age = ageText.text.toString().toIntOrNull()
            val driversLicenes = driversLicenseCB.isChecked
            val selectedGenderID = genderRadioGrp.checkedRadioButtonId
            val gender = findViewById<RadioButton>(selectedGenderID).text.toString()
            val name = nameEditText.text.toString()
            val phone = phoneNumberEditText.text.toString()

            if(age != null || name.isNotEmpty() || selectedGenderID == -1 || phone.isNotEmpty()){
                val firebaseManager = FirebaseManager(this)
                firebaseManager.pushUserData(age!!,driversLicenes,gender,name,phone) { success, errorMsg ->
                    if(success){
                        Toast.makeText(this,"Data updated successfully!",Toast.LENGTH_SHORT).show()
                        ageText.text.clear()
                        driversLicenseCB.isChecked = false
                        genderRadioGrp.clearCheck()
                        nameEditText.text.clear()
                        phoneNumberEditText.text.clear()
                    }else{
                        Toast.makeText(this,"Failed to send data: $errorMsg",Toast.LENGTH_SHORT).show()
                        Log.d("FB Post Error","Error: $errorMsg")
                    }
                }
                Toast.makeText(this,"Update successful!",Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(this,"Please fill out all fields!", Toast.LENGTH_SHORT).show()
            }
        }
    }
}