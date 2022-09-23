package com.example.ugd3_kelompok15;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.content.SharedPreferences
import android.view.WindowManager;
import androidx.appcompat.app.AppCompatActivity;

class SplashScreen : AppCompatActivity() {

    private val myPreferences = "sPref";
    private val nama = "sName";
    var sharedPreferences: SharedPreferences? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sharedPreferences = getSharedPreferences(myPreferences, MODE_PRIVATE)

        if (sharedPreferences!!.contains(nama)) {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        } else {
            setContentView(R.layout.activity_splash_screen)
            Handler().postDelayed({
                val intent = Intent(
                    this@SplashScreen,
                    LoginActivity::class.java
                )
                startActivity(intent)
                finish()
            }, 2000)
            val editor: SharedPreferences.Editor = sharedPreferences!!.edit()
            editor.putString(nama, "Right")
            editor.apply()
        }
    }
}
