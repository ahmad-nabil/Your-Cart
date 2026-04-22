package com.ejabi.yourcart.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.ejabi.data.local.datastore.SessionPreferences
import com.ejabi.yourcart.ui.OrdersActivity
import com.ejabi.yourcart.databinding.ActivityAccountInfoBinding
import com.ejabi.yourcart.ui.login.LoginActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class AccountInfo : AppCompatActivity() {

    private lateinit var binding: ActivityAccountInfoBinding

    @Inject
    lateinit var sessionPreferences: SessionPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAccountInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loadUserData()
        setupClicks()
    }

    private fun loadUserData() {
        lifecycleScope.launch {
            val session = sessionPreferences.getSession()

            session?.let { auth ->

                val user = auth.user

                binding.tvUserName.text =
                    "${user.firstName} ${user.lastName}"

                binding.tvUserEmail.text = user.email
                binding.tvNameValue.text =
                    "${user.firstName} ${user.lastName}"
                binding.tvEmailValue.text = user.email

                binding.tvPhoneValue.text = user.username // (if no phone)

                // image
                user.image?.let { imageUrl ->
                    if (imageUrl.isNotEmpty()) {
                        Glide.with(this@AccountInfo)
                            .load(imageUrl)
                            .into(binding.ivProfile)
                    }
                }
            }
        }
    }

    private fun setupClicks() {

        binding.cardOrders.setOnClickListener {
            startActivity(Intent(this, OrdersActivity::class.java))
        }

        binding.cardLogout.setOnClickListener {
            lifecycleScope.launch {
                sessionPreferences.clear()

                startActivity(
                    Intent(this@AccountInfo, LoginActivity::class.java)
                )
                finishAffinity()
            }
        }

        binding.btnMenu.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }
}