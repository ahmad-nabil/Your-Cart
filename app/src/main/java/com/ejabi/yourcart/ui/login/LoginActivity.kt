package com.ejabi.yourcart.ui.login

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.ejabi.data.local.datastore.SessionPreferences
import com.ejabi.domain.model.AuthSession
import com.ejabi.yourcart.databinding.ActivityLoginBinding
import com.ejabi.yourcart.databinding.DialogProgressBinding
import com.ejabi.yourcart.ui.login.viewmodel.LoginViewModel
import com.ejabi.yourcart.ui.main.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {

    private val viewModel: LoginViewModel by viewModels()
    private lateinit var binding: ActivityLoginBinding
    private var progressDialog: AlertDialog? = null
    @Inject
    lateinit var sessionPreferences: SessionPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupClickListeners()
        observeViewModel()
    }

    private fun setupClickListeners() {
        binding.login.setOnClickListener {
            val username = binding.etUsername.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()

            val validationResult = validateLogin(username, password)

            if (validationResult.isValid) {
                viewModel.login(username, password)
            } else {
                showError(validationResult.message)
            }
        }
    }

    private fun observeViewModel() {
        viewModel.login.observe(this) { authSession ->
            authSession?.let {
                saveSessionAndNavigate(it)
            }
        }

        viewModel.loading.observe(this) { isLoading ->
            binding.login.isEnabled = !isLoading
           if (isLoading) showProgressDialog() else dismissProgressDialog()
        }

        viewModel.error.observe(this) { errorMessage ->
            errorMessage?.let {
                Toast.makeText(this, it, Toast.LENGTH_LONG).show()
            }
        }
    }
    private fun showProgressDialog() {
        if (progressDialog == null) {
            val dialogBinding = DialogProgressBinding.inflate(LayoutInflater.from(this))
            progressDialog = AlertDialog.Builder(this)
                .setView(dialogBinding.root)
                .setCancelable(false)  // Prevent user from dismissing by tapping outside
                .create()
        }
        progressDialog?.show()
    }

    private fun dismissProgressDialog() {
        progressDialog?.dismiss()
    }
    private fun saveSessionAndNavigate(authSession: AuthSession) {
        lifecycleScope.launch {
            try {
                sessionPreferences.saveSession(authSession)

                startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                finish()
            } catch (e: Exception) {
                Toast.makeText(
                    this@LoginActivity,
                    "Error saving session: ${e.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun validateLogin(username: String, password: String): ValidationResult {
        return when {
            username.isBlank() -> ValidationResult(false, "اسم المستخدم فاضي")
            username.length < 3 -> ValidationResult(false, "اسم المستخدم قصير")
            password.isBlank() -> ValidationResult(false, "كلمة السر فاضية")
            password.length < 6 -> ValidationResult(false, "كلمة السر لازم تكون 6 أحرف على الأقل")
            else -> ValidationResult(true)
        }
    }

    private fun showError(message: String?) {
        binding.etUsername.error = null
        binding.etPassword.error = null

        message?.let {
            when {
                it.contains("مستخدم") -> binding.etUsername.error = it
                it.contains("سر") -> binding.etPassword.error = it
                else -> Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
            }
        }
    }

    data class ValidationResult(
        val isValid: Boolean,
        val message: String? = null
    )
}