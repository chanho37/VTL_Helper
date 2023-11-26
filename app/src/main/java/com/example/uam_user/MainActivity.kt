package com.example.uam_user

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val btn_map = findViewById<Button>(R.id.btn_map)
        btn_map.setOnClickListener {
            val intent = Intent(this, MapActivity::class.java)
            startActivity(intent)
        }

        val btn_landing = findViewById<Button>(R.id.btn_landing)
        btn_landing.setOnClickListener {
            val intent = Intent(this, LandingActivity::class.java)
            startActivity(intent)
        }

        val btn_takeoff = findViewById<Button>(R.id.btn_takeoff)
        btn_takeoff.setOnClickListener {
            val intent = Intent(this, TakeoffActivity::class.java)
            startActivity(intent)
        }


    }
}