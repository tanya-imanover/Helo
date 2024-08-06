package com.example.helo.ViewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.helo.Model.LoginResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class LoginViewModel(application: Application) : AndroidViewModel(application) {
    private val auth = FirebaseAuth.getInstance()
    init {
        auth.addAuthStateListener {
            if (it.currentUser != null) {
                user.value = it.currentUser
            }
        }
    }

    private val error = MutableLiveData<String>()
    private val user = MutableLiveData<FirebaseUser>()

    fun getUser(): LiveData<FirebaseUser>{
        return user
    }

    fun getError(): LiveData<String>{
        return error
    }

    fun login( email : String, password: String ) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener {
            }
            .addOnFailureListener {
                error.value = it.message.toString()

            }
    }
}