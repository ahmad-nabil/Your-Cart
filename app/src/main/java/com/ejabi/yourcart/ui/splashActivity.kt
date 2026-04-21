package com.ejabi.yourcart.ui
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.motion.widget.MotionLayout
import com.ejabi.yourcart.databinding.ActivitySplashBinding
import com.ejabi.yourcart.ui.login.LoginActivity

class splashActivity : AppCompatActivity() {
lateinit var  binding: ActivitySplashBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
binding= ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.motionLayout.transitionToEnd()
        binding.motionLayout.postDelayed({
            startActivity(Intent(this@splashActivity, LoginActivity::class.java))
            finish()
        }, 10000)
        binding.motionLayout.setTransitionListener(object : MotionLayout.TransitionListener {
            override fun onTransitionStarted(layout: MotionLayout, startId: Int, endId: Int) {}
            override fun onTransitionChange(layout: MotionLayout, startId: Int, endId: Int, progress: Float) {}

            override fun onTransitionCompleted(layout: MotionLayout, currentId: Int) {
                layout.postDelayed({
                    startActivity(Intent(this@splashActivity, LoginActivity::class.java))
                    finish()
                }, 10000)
            }

            override fun onTransitionTrigger(layout: MotionLayout, triggerId: Int, positive: Boolean, progress: Float) {}
        })
    }
}