package com.example.registrationandsignin

import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.registrationandsignin.databinding.ActivityMainBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.auth.User

class MainActivity : AppCompatActivity() {

    private var binding: ActivityMainBinding? = null
    private lateinit var dbRef: DatabaseReference


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding?.root
        setContentView(view)




        binding?.login?.setOnClickListener {

            var user_name = binding?.username?.text.toString()
            var passcode = binding?.password?.text.toString()


            if (user_name?.isEmpty() == true || passcode?.isEmpty()==true)
            {
                Toast.makeText(this, "Please enter all details", Toast.LENGTH_SHORT).show()
            }
            else
            {

                checklogin(user_name,passcode)

            }


        }

        binding?.newUser?.setOnClickListener {
            val intent = Intent(this, Signup::class.java)
            startActivity(intent)
        }

        binding?.forgetPassword?.setOnClickListener {

            val intent = Intent(this,Forget_Password::class.java)
            startActivity(intent)
        }

    }

    private fun checklogin(userName: String?, passcode: String?) {

        dbRef = FirebaseDatabase.getInstance().getReference("User")
        dbRef.child(userName!!).get().addOnSuccessListener {

            if(it.exists())
            {

                val email = it.child("email").value.toString()
                val name = it.child("name").value.toString()
                val password = it.child("password").value.toString()
                val phone = it.child("phone").value.toString()
                val username = it.child("username").value.toString()

                //check if password matches or not and than go to users page

                if (password == passcode)
                {
                    val intent = Intent(this,User_Page::class.java )
                    intent.putExtra("name", name)
                    intent.putExtra("username", username)
                    intent.putExtra("email", email)
                    intent.putExtra("phone", phone)

                    startActivity(intent)
                }

                else
                {
                    Toast.makeText(this, "Password incorrect", Toast.LENGTH_SHORT).show()
                }

            }
            else
            {
                Toast.makeText(this, "User does not exist please Register first", Toast.LENGTH_SHORT).show()
            }


        }.addOnCanceledListener {
            Toast.makeText(this, "Database under maintenance", Toast.LENGTH_SHORT).show()
        }

    }


    /* private fun checkLogin(username: String, password: String) {
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

                     finish()

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
     }*/




}
