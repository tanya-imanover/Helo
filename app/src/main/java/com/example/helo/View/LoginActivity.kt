package com.example.helo.View

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.PatternMatcher
import android.util.Patterns
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.example.helo.R
import com.example.helo.ViewModel.LoginViewModel
import com.example.helo.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {
    private lateinit var binding : ActivityLoginBinding
    private lateinit var viewModel : LoginViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        viewModel = ViewModelProvider(this)[LoginViewModel::class.java]
        observeViewModel()
        setupClickListeners()
    }

    private fun observeViewModel(){
        viewModel.getError().observe(this@LoginActivity){
            if(it != null) {
                Toast.makeText(
                    this@LoginActivity,
                    it,
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
        viewModel.getUser().observe(this@LoginActivity){
            if(it != null){
                startActivity(UsersActivity.getIntent(this@LoginActivity))
                finish()
            }
        }
    }

    private fun setupClickListeners(){
        with(binding){
            buttonLogin.setOnClickListener {
                val email = editTextLogin.text.toString().trim()
                val password = editTextPassword.text.toString().trim()
                if(checkInputs(email, password)) {
                    viewModel.login(email, password)
                }
            }

            textViewForgotPassword.setOnClickListener {
                startActivity(ResetPasswordActivity.getIntent(this@LoginActivity))
            }

            textViewRegister.setOnClickListener {
                startActivity(RegisterActivity.getIntent(this@LoginActivity))
            }
        }
    }

    private fun checkInputs(email: String, password: String) : Boolean{
        with(binding) {
            if (email.isEmpty()) {
                editTextLogin.error = "Empty email"
                return false
            }
            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                editTextLogin.error = "Email is bad format"
                return false
            }
            if (password.isEmpty()) {
                editTextPassword.error = "Empty password"
                return false
            }
            if (password.length < 6) {
                editTextPassword.error = "Password should be at least 6 symbols"
                return false
            }
            else return true
        }
    }

    companion object{
        fun getIntent(context: Context): Intent{
            return Intent(context, LoginActivity::class.java)
        }
    }
}