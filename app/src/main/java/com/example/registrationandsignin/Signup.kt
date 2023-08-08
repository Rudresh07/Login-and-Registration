package com.example.registrationandsignin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.registrationandsignin.databinding.ActivitySignupBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class Signup : AppCompatActivity() {

    private var binding: ActivitySignupBinding? = null
    private lateinit var dbRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySignupBinding.inflate(layoutInflater)
        val view = binding?.root
        setContentView(view)

        dbRef = FirebaseDatabase.getInstance().getReference("User")

        binding?.ExistingUser?.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        binding?.submit?.setOnClickListener {
            // Check if all EditText fields are not empty
            if (Entryvalid()) {
                if (binding?.password?.text?.toString() == binding?.CnfPassword?.text?.toString()) {
                    saveUserData()
                } else {
                    Toast.makeText(this, "Password mismatched", Toast.LENGTH_SHORT).show()
                }
            } else {
                // Show an error message if any EditText is empty
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun saveUserData() {
        val name = binding?.name?.text?.toString()
        val userName = binding?.userName?.text?.toString()
        val email = binding?.email?.text?.toString()
        val password = binding?.password?.text?.toString()
        val phone = binding?.phone?.text?.toString()

        val userRef = dbRef.child(userName!!)//here we set username to be the child node from where data start loading

        // Check if the username already exists in the database
        userRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                // If the snapshot exists, it means the username already exists
                if (snapshot.exists()) {
                    Toast.makeText(this@Signup, "Username already exists", Toast.LENGTH_SHORT).show()
                } else {
                    // If the username doesn't exist, save the user's data to the database
                    val user = UserDetails(name, userName, email, password, phone)

                    userRef.setValue(user)//here we set user data to database
                        .addOnCompleteListener {
                            Toast.makeText(this@Signup, "Registration successful", Toast.LENGTH_SHORT).show()

                            val intent = Intent(this@Signup,MainActivity::class.java)
                            startActivity(intent)

                            finish()
                        }
                        .addOnFailureListener {
                            Toast.makeText(this@Signup, "Error", Toast.LENGTH_SHORT).show()
                        }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@Signup, "Error checking username", Toast.LENGTH_SHORT).show()
            }
        })

    }

    // Check if all input fields are filled or not
    private fun Entryvalid(): Boolean {
        return (binding?.name?.text?.toString()?.isNotEmpty() == true
                && binding?.userName?.text?.toString()?.isNotEmpty() == true
                && binding?.password?.text?.toString()?.isNotEmpty() == true
                && binding?.CnfPassword?.text?.toString()?.isNotEmpty() == true
                && binding?.email?.text?.toString()?.isNotEmpty() == true
                && binding?.phone?.text?.toString()?.isNotEmpty() == true)
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null // Avoid memory leaks by nullifying the binding
    }
}
