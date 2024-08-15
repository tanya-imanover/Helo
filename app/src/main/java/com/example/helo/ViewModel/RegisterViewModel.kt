package com.example.helo.ViewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.helo.Model.LoginResult
import com.example.helo.Model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.FirebaseDatabase

class RegisterViewModel(application: Application) : AndroidViewModel(application) {
    private val auth = FirebaseAuth.getInstance()
    private val error = MutableLiveData<String>()
    private val user = MutableLiveData<FirebaseUser>()
    private val firebaseDatabase = FirebaseDatabase.getInstance()
    private val usersReference = firebaseDatabase.getReference("Users")
    fun getError(): LiveData<String> {
        return error
    }

    fun getUser() : LiveData<FirebaseUser>{
        return user
    }

    init {
        auth.addAuthStateListener {
            if(it.currentUser != null){
                user.value = it.currentUser
            }
        }
    }

    fun signUp (email: String, password: String, name: String, lastName: String, age: Int){
        auth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                it.user?.let {
                    usersReference.child(it.uid).setValue(
                        User(
                            it.uid,
                            name,
                            lastName,
                            age,
                            false
                        )
                    )
                }
            }
            .addOnFailureListener {
                error.value = it.message
            }
    }

//    fun authRegisteredUser (user: String, password: String){
//        auth.signInWithEmailAndPassword(user, password)
//            .addOnSuccessListener {
//                authResult.value = LoginResult(true, "Success")
//            }
//            .addOnFailureListener {
//                authResult.value = LoginResult(false, "${it.message}")
//            }
//    }
}