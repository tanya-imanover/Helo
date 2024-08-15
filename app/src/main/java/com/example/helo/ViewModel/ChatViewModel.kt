package com.example.helo.ViewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.helo.Model.Message
import com.example.helo.Model.User
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ChatViewModel(
    private val currentUserId: String,
    private val otherUserId: String
) : ViewModel() {
    private val messages = MutableLiveData<List<Message>>()
    private val otherUser = MutableLiveData<User>()
    private val isMessageSent = MutableLiveData<Boolean>()
    private val error = MutableLiveData<String>()
    private val firebaseDatabase = FirebaseDatabase.getInstance()
    private val usersReference = firebaseDatabase.getReference("Users")
    private val messagesReference = firebaseDatabase.getReference("Messages")
    init{
        usersReference.child(otherUserId).addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val user = snapshot.getValue(User::class.java)
                otherUser.value = user
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
        messagesReference.child(currentUserId).child(otherUserId).addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val messageList = ArrayList<Message>()
                for (dataSnapshot in snapshot.children){
                    val message = dataSnapshot.getValue(Message::class.java)
                    message?.let {
                        messageList.add(it)
                    }
                }
                messages.value = messageList

            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }
    fun setUserIsOnline(isOnline : Boolean){
        usersReference.child(currentUserId).child("online").setValue(isOnline)
    }

    fun getMessages(): LiveData<List<Message>>{
        return messages
    }
    fun getOtherUser(): LiveData<User>{
        return otherUser
    }
    fun getIsMessageSent(): LiveData<Boolean>{
        return isMessageSent
    }
    fun getError(): LiveData<String>{
        return error
    }

    fun sendMessage (message: Message){
        messagesReference
            .child(message.senderId)
            .child(message.receiverId)
            .push()
            .setValue(message)
            .addOnSuccessListener {
                messagesReference
                    .child(message.receiverId)
                    .child(message.senderId)
                    .push()
                    .setValue(message).addOnSuccessListener {
                        isMessageSent.value = true
                    }
                    .addOnFailureListener {
                        error.value = it.message
                    }
            }
            .addOnFailureListener {
                error.value = it.message
            }

    }


}