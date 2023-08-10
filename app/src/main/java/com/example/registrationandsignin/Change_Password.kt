package com.example.registrationandsignin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.registrationandsignin.databinding.ActivityChangePasswordBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class Change_Password : AppCompatActivity() {

    private var binding:ActivityChangePasswordBinding? = null
    private lateinit var dbRef : DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityChangePasswordBinding.inflate(layoutInflater)
        val view = binding?.root

        setContentView(view)

        binding?.submit?.setOnClickListener {

            var email = binding?.email?.text?.toString()
            var password = binding?.password?.text?.toString()
            var Cnfpassword = binding?.CnfPassword?.text?.toString()
            var phone = binding?.phone?.text?.toString()


            var Emaildb = intent.getStringExtra("email")
            var Phonedb = intent.getStringExtra("phone")
            var UserDetail = intent.getStringExtra("username")


            if (Emaildb==email && Phonedb==phone)
            {

                if (password!! == Cnfpassword!!)
                {

                    updatePassword(password,UserDetail)
                    val intent = Intent(this,MainActivity::class.java)
                    startActivity(intent)

                }
            }
            else{
                Toast.makeText(this, "Email or Mobile number mismatched", Toast.LENGTH_SHORT).show()
            }

        }

    }

    private fun updatePassword(password: String, UserDetail: String?) {



        val user = mapOf(
            "password" to password
        )

        dbRef = FirebaseDatabase.getInstance().getReference("User")
        dbRef.child(UserDetail!!).updateChildren(user).addOnSuccessListener {
            Toast.makeText(this, "Password changed successfully", Toast.LENGTH_SHORT).show()

            binding?.email?.text?.clear()
            binding?.password?.text?.clear()
            finish()
        }.addOnCanceledListener {
            Toast.makeText(this, "Database connection error!!", Toast.LENGTH_SHORT).show()
        }



    }


}