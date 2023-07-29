package com.example.registrationandsignin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.registrationandsignin.databinding.ActivityMainBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MainActivity : AppCompatActivity() {

    private var binding: ActivityMainBinding? = null
    private lateinit var dbRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding?.root
        setContentView(view)

        // Initialize Firebase database reference
        dbRef = FirebaseDatabase.getInstance().getReference("User")// this line is used to establish the connection with the database we created

        // Retrieve the passed username and password from the Intent extras
        val username = intent.getStringExtra("username")
        val password = intent.getStringExtra("password")
        // here we get the username and password to match it with database username and password

        // Check if username and password are not null before proceeding with login
        if (!username.isNullOrEmpty() && !password.isNullOrEmpty()) {
            binding?.username?.setText(username)
            binding?.password?.setText(password)
            checkLogin(username, password)
        }

        binding?.login?.setOnClickListener {
            val user_name = binding?.username?.text?.toString()
            val passcode = binding?.password?.text?.toString()

            checkLogin(user_name!!, passcode!!)
        }

        binding?.newUser?.setOnClickListener {
            val intent = Intent(this, Signup::class.java)
            startActivity(intent)
        }
    }

    private fun checkLogin(username: String, password: String) {
        // Step 1: Ask Firebase to find information about the specific username
        val userRef = dbRef.child(username)

        // Step 2: Tell Firebase to give us information about the username just one time
        userRef.addListenerForSingleValueEvent(object : ValueEventListener {
            // Step 4: When Firebase finds the username, it tells us what it knows (the information is called 'snapshot')
            override fun onDataChange(snapshot: DataSnapshot) {
                // Step 5: Take the information from Firebase and put it in a special box called 'user'
                val user = snapshot.getValue(UserDetails::class.java)

                // Step 6: Check if the password in the box matches the password you entered
                if (user?.password == password) {
                    // If it matches, say "Login successful!"
                    Toast.makeText(this@MainActivity, "Login successful", Toast.LENGTH_SHORT).show()

                    val Name = user?.name
                    val UserName = user?.username
                    val Email = user?.email
                    val Phone = user?.phone

                    val intent = Intent(this@MainActivity,User_Page::class.java)

                    intent.putExtra("name",Name)
                    intent.putExtra("username",UserName)
                    intent.putExtra("email",Email)
                    intent.putExtra("phone",Phone)

                    startActivity(intent)

                } else {
                    // If it doesn't match, say "Invalid username or password!"
                    Toast.makeText(this@MainActivity, "Invalid username or password", Toast.LENGTH_SHORT).show()
                }
            }

            // Step 8: If something goes wrong and Firebase can't find the username, show an error message
            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@MainActivity, "Error getting user data", Toast.LENGTH_SHORT).show()
            }
        })
    }


    override fun onDestroy() {
        super.onDestroy()
        binding = null // Avoid memory leaks by nullifying the binding
    }
}
