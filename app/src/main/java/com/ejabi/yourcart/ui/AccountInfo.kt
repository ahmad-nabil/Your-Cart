package com.ejabi.yourcart.ui

import android.content.Intent
import android.os.Bundle
import android.widget.PopupMenu
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.ejabi.data.local.datastore.SessionPreferences
import com.ejabi.yourcart.R
import com.ejabi.yourcart.databinding.ActivityAccountInfoBinding
import com.ejabi.yourcart.ui.login.LoginActivity
import com.ejabi.yourcart.ui.main.MainActivity
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

    private fun setupClicks() = with(binding) {
        btnMenu.setOnClickListener { showPopupMenu() }

        cardOrders.setOnClickListener {
            navigateTo(OrdersActivity::class.java)
        }

        cardLogout.setOnClickListener {
            logout()
        }
    }

    private fun showPopupMenu() {
        PopupMenu(this, binding.btnMenu).apply {
            menuInflater.inflate(R.menu.profile_menu, menu)

            setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.menu_cart -> {
                        navigateTo(CartActivity::class.java)
                        true
                    }

                    R.id.menu_orders -> {
                        navigateTo(OrdersActivity::class.java)
                        true
                    }

                    R.id.menu_home -> {
                        navigateTo(MainActivity::class.java, clearStack = true)
                        true
                    }

                    R.id.menu_logout -> {
                        logout()
                        true
                    }

                    else -> false
                }
            }
        }.show()
    }

    private fun loadUserData() {
        lifecycleScope.launch {
            sessionPreferences.getSession()?.let { auth ->
                val user = auth.user
                val fullName = "${user.firstName} ${user.lastName}"

                with(binding) {
                    tvUserName.text = fullName
                    tvUserEmail.text = user.email
                    tvNameValue.text = fullName
                    tvEmailValue.text = user.email
                    tvPhoneValue.text = user.username

                    if (!user.image.isNullOrBlank()) {
                        Glide.with(this@AccountInfo)
                            .load(user.image)
                            .placeholder(R.drawable.profile_placeholder)
                            .error(R.drawable.profile_placeholder)
                            .into(ivProfile)
                    }
                }
            }
        }
    }

    private fun logout() {
        lifecycleScope.launch {
            sessionPreferences.clear()
            navigateTo(LoginActivity::class.java, clearStack = true)
        }
    }

    private fun navigateTo(destination: Class<*>, clearStack: Boolean = false) {
        val intent = Intent(this, destination)
        if (clearStack) {
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        startActivity(intent)
    }
}