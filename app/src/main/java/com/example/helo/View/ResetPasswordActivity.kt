package com.example.helo.View

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.transition.Visibility
import com.example.helo.R
import com.example.helo.ViewModel.ResetPasswordViewModel
import com.example.helo.databinding.ActivityResetPasswordBinding

enum class VisibilityMode {MODE_RESET_PASSWORD, MODE_VERIFY_CODE, MODE_SAVE_NEW_PASS}
class ResetPasswordActivity : AppCompatActivity() {
    private lateinit var binding: ActivityResetPasswordBinding
    private lateinit var viewModel: ResetPasswordViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityResetPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        viewModel = ViewModelProvider(this)[ResetPasswordViewModel::class.java]
        observeViewModel()
        with(binding) {
            buttonResetPassword.setOnClickListener {
                if (checkEmail(editTextLogin.text.toString())) {
                   viewModel.resetPasswordUsingEmail(editTextLogin.text.trim().toString())
                } else {
                    editTextLogin.error = "Email is bad format"
                }
            }
        }
    }

    private fun observeViewModel(){
        viewModel.getError().observe(this@ResetPasswordActivity){
            Toast.makeText(
                this@ResetPasswordActivity,
                it,
                Toast.LENGTH_SHORT)
                .show()
        }
        viewModel.getSuccess().observe(this@ResetPasswordActivity){
            if(it) {
                Toast.makeText(
                    this@ResetPasswordActivity,
                    getString(R.string.the_reset_link_sent),
                    Toast.LENGTH_SHORT)
                    .show()
                startActivity(LoginActivity.getIntent(this@ResetPasswordActivity))
                finish()
            }
        }
    }

    private fun checkEmail(email: String): Boolean =
        (email.isNotEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches())




    companion object{
        fun getIntent(context: Context): Intent {
            return Intent(context, ResetPasswordActivity::class.java)
        }
    }
}