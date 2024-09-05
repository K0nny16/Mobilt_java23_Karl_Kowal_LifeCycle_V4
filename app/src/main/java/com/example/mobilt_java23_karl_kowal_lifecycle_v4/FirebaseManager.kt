package com.example.mobilt_java23_karl_kowal_lifecycle_v4

import android.app.Activity
import android.content.Context
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class FirebaseManager(private val activity: Activity) {

    val auth: FirebaseAuth = FirebaseAuth.getInstance()
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
                        saveUID(userID)
                        onComplete(true,null)
                    }else{
                        val errorMsg = task.exception?.message?:"Error posting to DB!"
                        onComplete(false,errorMsg)
                    }
                }
        }
    }
    fun getUserData(userID:String, onComplete: (UserStruct?) -> Unit){
        //Var vi hämtar datan ifrån.
        //Lägger till en lyssnare som hämtar data när metoden körs.(inte kontinuerligt)
        //Skapar ett objekt som har egenskaperna från interfacet VEL.
        db.child("users").child(userID).addListenerForSingleValueEvent(object : ValueEventListener{
            //Hämtar en bild av datan. Ungefär samma som vi gjorde i JS
            override fun onDataChange(snapshot: DataSnapshot) {
                //Mappar datan till ett UserStruct eftersom det är vad vi returnerar.
                val user = snapshot.getValue(UserStruct::class.java)
                onComplete(user)
            }
            //Felhanterar och skickar en Toast det skulle misslyckas.
            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(activity,"Failed to retrieve user data!",Toast.LENGTH_SHORT).show()
                Log.d("UserDataError","Error: ${error.message}")
                onComplete(null)
            }
        })
    }

    private fun saveUID(userID: String){
        //Prefsens filnamet och om man kan skriva till den eller inte (lite som hur en klass funkar med private osv tror jag)
        val sharedPref = activity.getSharedPreferences("UserPreferences", Context.MODE_PRIVATE)
        //Öppnar för redigering.
        with(sharedPref.edit()){
            //Lägger till ett Key value pair i detta objektet av SP.
            putString("FirebaseUID",userID)
            //Apply är async istället för commit() som är sync.
            apply()
        }
    }
    fun newPassword(newPassword:String, onComplete: (Boolean, String?) -> Unit){
        val user:FirebaseUser? = auth.currentUser
        if(user != null){
            user.updatePassword(newPassword)
                .addOnCompleteListener { task ->
                    if(task.isSuccessful) onComplete(true,null) else onComplete(false,task.exception?.message?:"Unknow error!")
                }
        }else{
            onComplete(false,"User not logged in!")
        }
    }
}