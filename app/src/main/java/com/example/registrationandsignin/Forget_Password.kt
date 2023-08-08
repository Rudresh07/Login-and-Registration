package com.example.registrationandsignin

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.example.registrationandsignin.databinding.ActivityForgetPasswordBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class Forget_Password : AppCompatActivity() {

    private var binding: ActivityForgetPasswordBinding? = null
    private lateinit var dbRef: DatabaseReference

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityForgetPasswordBinding.inflate(layoutInflater)
        val view = binding?.root
        setContentView(view)

        setSupportActionBar(binding?.toolBar)

        //showing back button and implementing the back button
        if (supportActionBar!= null){

            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }


        binding?.submituser?.setOnClickListener {

            var User_name = binding?.username?.text?.toString()

            fetchdata(User_name)

        }


    }

    private fun fetchdata(userName: String?) {

        dbRef = FirebaseDatabase.getInstance().getReference("User")
        dbRef.child(userName!!).get().addOnSuccessListener {

            if (it.exists())
            {
                val email = it.child("email").value.toString()
                val phone = it.child("phone").value.toString()

                val intent = Intent(this,Change_Password::class.java)

                intent.putExtra("email",email)
                intent.putExtra("phone",phone)
                intent.putExtra("username",userName)
                startActivity(intent)
            }

        }

    }

}
