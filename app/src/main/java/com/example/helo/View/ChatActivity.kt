package com.example.helo.View

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.example.helo.Adapters.MessagesAdapter
import com.example.helo.ViewModel.ChatViewModel
import com.example.helo.Model.Message
import com.example.helo.R
import com.example.helo.ViewModel.ChatViewModelFactory
import com.example.helo.databinding.ActivityChatBinding

class ChatActivity : AppCompatActivity() {
    private lateinit var binding : ActivityChatBinding
    private lateinit var adapter : MessagesAdapter
    private var currentUserId: String = ""
    private var otherUserId: String = ""
    private lateinit var viewModel : ChatViewModel
    private lateinit var chatViewModelFactory: ChatViewModelFactory
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        intent.getStringExtra(EXTRA_CURRENT_USER_ID)?.let {  currentUserId = it }
        intent.getStringExtra(EXTRA_OTHER_USER_ID)?.let { otherUserId = it }
        adapter = MessagesAdapter(currentUserId)
        chatViewModelFactory = ChatViewModelFactory (currentUserId, otherUserId)
        viewModel = ViewModelProvider(this@ChatActivity, chatViewModelFactory)[ChatViewModel::class.java]

        binding.recyclerViewMessages.adapter = adapter

        val mess1 = ArrayList<Message>()
        for(i in 0..10){
            mess1.add(Message("Text $i", currentUserId, otherUserId))
            mess1.add(Message("Text $i", otherUserId, currentUserId))
        }
        adapter.setMessages(mess1)
        binding.imageViewSend.setOnClickListener {
            val text = binding.editTextMessage.text.toString().trim()
            if(text.isNotEmpty()) {
                viewModel.sendMessage(Message(text, currentUserId, otherUserId))
            }
        }
        observeViewModel()
    }

    private fun observeViewModel (){
        viewModel.getMessages().observe(this@ChatActivity){
            adapter.setMessages(it)
        }
        viewModel.getError().observe(this@ChatActivity){
            if(it != null){
                Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
            }
        }
        viewModel.getIsMessageSent().observe(this@ChatActivity){
            if(it){
                binding.editTextMessage.text.clear()
            }
        }
        viewModel.getOtherUser().observe(this@ChatActivity){
            val userName = "${it.name} ${it.lastName}"
            binding.textViewTitle.text = userName
            binding.statusView.background =
                if(it.online){
                    ContextCompat.getDrawable(this, R.drawable.circle_green)
                }
                else {
                    ContextCompat.getDrawable(this, R.drawable.circle_red)
                }
        }
    }
    override fun onResume() {
        super.onResume()
        viewModel.setUserIsOnline(true)
    }

    override fun onPause() {
        super.onPause()
        viewModel.setUserIsOnline(false)
    }

    companion object{
        private const val EXTRA_CURRENT_USER_ID = "current_id"
        private const val EXTRA_OTHER_USER_ID = "other_id"
        fun getIntent(context: Context, currentUserId: String, otherUserId: String) : Intent{
            val intent = Intent(context, ChatActivity::class.java)
            intent.putExtra(EXTRA_CURRENT_USER_ID, currentUserId)
            intent.putExtra(EXTRA_OTHER_USER_ID, otherUserId)
            return intent
        }
    }
}