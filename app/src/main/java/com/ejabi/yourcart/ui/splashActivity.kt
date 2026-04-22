package com.ejabi.yourcart.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.motion.widget.MotionLayout
import com.ejabi.yourcart.R
import com.ejabi.yourcart.databinding.ActivitySplashBinding
import com.ejabi.yourcart.ui.login.LoginActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class splashActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.motionLayout.setTransition(R.id.splash_transition)

        binding.motionLayout.setTransitionListener(object : MotionLayout.TransitionListener {
            override fun onTransitionStarted(layout: MotionLayout, startId: Int, endId: Int) {}

            override fun onTransitionChange(
                layout: MotionLayout,
                startId: Int,
                endId: Int,
                progress: Float
            ) {}

            override fun onTransitionCompleted(layout: MotionLayout, currentId: Int) {
                startActivity(Intent(this@splashActivity, LoginActivity::class.java))
                finish()
            }

            override fun onTransitionTrigger(
                layout: MotionLayout,
                triggerId: Int,
                positive: Boolean,
                progress: Float
            ) {}
        })

        binding.motionLayout.post {
            binding.motionLayout.progress = 0f
            binding.motionLayout.transitionToEnd()
        }
    }
}