package hu.bencelaszlo.notes

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.content.Intent
import android.os.Handler

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        supportActionBar!!.hide()
        val i = Intent(this@SplashActivity, MainActivity::class.java)
        Handler().postDelayed({
            startActivity(i)
            finish()
        }, 1000)
    }
}