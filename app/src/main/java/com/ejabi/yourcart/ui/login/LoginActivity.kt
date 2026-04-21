package com.ejabi.yourcart.ui.login

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.ejabi.yourcart.databinding.ActivityLoginBinding
import com.ejabi.yourcart.ui.main.MainActivity

class LoginActivity : AppCompatActivity() {
lateinit var binding: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
binding.login.setOnClickListener {
startActivity(Intent(this, MainActivity::class.java))
}
    }
}