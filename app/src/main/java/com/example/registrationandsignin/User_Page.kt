package com.example.registrationandsignin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.registrationandsignin.databinding.ActivityUserPageBinding

class User_Page : AppCompatActivity() {

    private var binding: ActivityUserPageBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityUserPageBinding.inflate(layoutInflater)
        val view = binding?.root

        setContentView(view)

        var Name = intent.getStringExtra("name")
        var UserName = intent.getStringExtra("username")
        var Email = intent.getStringExtra("email")
        var Phone = intent.getStringExtra("phone")

        binding?.ShowName?.text = Name
        binding?.ShowEmail?.text = Email
        binding?.ShowUsername?.text = UserName
        binding?.ShowPhone?.text = Phone

        binding?.logout?.setOnClickListener {
            val intent = Intent(this,MainActivity::class.java)
            startActivity(intent)

            finish()
        }

    }
}