package com.example.helo.View

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.example.helo.R
import com.example.helo.ViewModel.UsersViewModel
import com.google.firebase.auth.FirebaseAuth

class UsersActivity : AppCompatActivity() {
    private lateinit var viewModel : UsersViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_users)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
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