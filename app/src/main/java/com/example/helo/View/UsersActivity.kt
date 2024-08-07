package com.example.helo.View

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.example.helo.Adapters.UsersAdapter
import com.example.helo.Model.User
import com.example.helo.R
import com.example.helo.ViewModel.UsersViewModel
import com.example.helo.databinding.ActivityUsersBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlin.random.Random

class UsersActivity : AppCompatActivity() {
    private lateinit var viewModel : UsersViewModel
    private lateinit var binding: ActivityUsersBinding
    private lateinit var adapter: UsersAdapter
    private val firebaseDatabase = FirebaseDatabase.getInstance()
    private val databaseReference = firebaseDatabase.getReference("Users")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityUsersBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.recyclerViewUsers)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        adapter = UsersAdapter()
        binding.recyclerViewUsers.adapter = adapter
        viewModel = ViewModelProvider(this@UsersActivity)[UsersViewModel::class.java]
        observeViewModel()
    }
    companion object{
        fun getIntent(context: Context): Intent {
            return Intent(context, UsersActivity::class.java)
        }
    }

    private fun observeViewModel(){
        viewModel.getUser().observe(this@UsersActivity){
            if(it == null){
                startActivity(LoginActivity.getIntent(this@UsersActivity))
                finish()
            }
        }
        viewModel.getUserList().observe(this@UsersActivity){
            adapter.setUsers(it)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.users_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.item_logout){
            viewModel.logout()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onDestroy() {
        super.onDestroy()

    }
}