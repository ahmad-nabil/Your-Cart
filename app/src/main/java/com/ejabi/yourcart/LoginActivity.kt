package com.ejabi.yourcart

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.ejabi.yourcart.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {
lateinit var binding: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
binding.login.setOnClickListener {

}
    }
}