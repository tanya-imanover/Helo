package com.example.helo.View

import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.content.res.Resources.Theme
import android.graphics.Color
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.example.helo.MainActivity
import com.example.helo.R
import com.example.helo.ViewModel.RegisterViewModel
import com.example.helo.databinding.ActivityRegisterBinding

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private lateinit var viewModel: RegisterViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        viewModel = ViewModelProvider(this)[RegisterViewModel::class.java]
        with(binding){
            buttonResetPassword.setOnClickListener {
                if(checkRegistrationFields()){
                    val email = editTextLogin.text.trim().toString()
                    val password = editTextPassword.text.trim().toString()
                    val age = editTextAge.text.toString().toInt()
                    val name = editTextName.text.trim().toString()
                    val lastName = editTextLastName.text.trim().toString()
                    viewModel.signUp(email, password, name, lastName, age)
                    observeViewModel()

                }

            }
        }
    }

    private fun observeViewModel(){
        viewModel.getUser().observe(this@RegisterActivity) {
            if (it != null) {
                startActivity(UsersActivity.getIntent(this@RegisterActivity))
                finish()
            }
        }

        viewModel.getError().observe(this@RegisterActivity){
                Toast.makeText(
                    this@RegisterActivity,
                    it,
                    Toast.LENGTH_SHORT
                ).show()
        }
    }


    private fun checkRegistrationFields() : Boolean{
        var isCorrect = true
        with(binding){
            if(editTextLogin.text.trim().isEmpty()){
                editTextLogin.error = "Enter your email"
                isCorrect = false
            }
            if(!Patterns.EMAIL_ADDRESS.matcher(editTextLogin.text.trim()).matches()){
                editTextLogin.error = "Email is bad format"
                isCorrect = false
            }
            if(editTextName.text.trim().isEmpty()){
                editTextName.error = "Empty name"
                isCorrect = false
            }
            if(editTextLastName.text.trim().isEmpty()){
                editTextLastName.error = "Empty last name"
                isCorrect = false
            }
            if(editTextPassword.length() < 6){
                editTextPassword.error = "Password should be at least 6 symbols"
            }
            if (!checkPasswordFields() ){
                editTextRepeatPassword.error = "Passwords do not match"
                isCorrect = false
            }
        }
        return isCorrect
    }
    private fun checkPasswordFields() : Boolean{
        with(binding) {
            return (editTextPassword.text.toString() == editTextRepeatPassword.text.toString())
        }
    }
    companion object{
        fun getIntent(context: Context): Intent {
            return Intent(context, RegisterActivity::class.java)
        }
    }
}