package com.example.mobilt_java23_karl_kowal_lifecycle_v4

import android.app.Activity
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase


class FirebaseManager(private val activity: Activity) {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val db = FirebaseDatabase.getInstance("https://lifecyclev4-b7069-default-rtdb.europe-west1.firebasedatabase.app").reference

    //Görs Async med onComplete. Alltså det som finns i onComplete är det som returneras.
    //Unit är kotlins motsvarighet till Void.
    fun signIn(email: String,password:String, onComplete:(Boolean,String?) -> Unit){
        auth.signInWithEmailAndPassword(email,password)
            .addOnCompleteListener(activity){ task ->
                if(task.isSuccessful){
                    onComplete(true,null)
                }else{
                    //Hämtar felmedelandet ifall det finns annars sätter vi ett custom.
                    val errorMessage = task.exception?.message?:"Unknown Error!"
                    onComplete(false,errorMessage)
                }
            }
    }
     fun pushUserData(age: Int, driversLicense:Boolean, gender: String, email: String, phoneNumber: String, onComplete: (Boolean, String?) -> Unit){
        val userID = auth.currentUser?.uid
        val userData = UserStruct(age,driversLicense,gender,email,phoneNumber)
        if (userID != null) {
            db.child("users").child(userID).setValue(userData)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful){
                        Log.d("Testxxx","ID: $userID OBJ: $userData")
                        onComplete(true,null)
                    }else{
                        val errorMsg = task.exception?.message?:"Error posting to DB!"
                        onComplete(false,errorMsg)
                    }
                }
        }
    }
}