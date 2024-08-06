package com.example.helo.ViewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.helo.Model.LoginResult
import com.example.helo.Model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class RegisterViewModel(application: Application) : AndroidViewModel(application) {
    private val auth = FirebaseAuth.getInstance()
    private val error = MutableLiveData<String>()
    private val user = MutableLiveData<FirebaseUser>()
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