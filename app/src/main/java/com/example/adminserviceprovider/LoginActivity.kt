package com.example.adminserviceprovider

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.OnApplyWindowInsetsListener
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.adminserviceprovider.databinding.ActivityLoginBinding
import com.example.adminserviceprovider.model.UserModel
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.database

class LoginActivity : AppCompatActivity() {
    private lateinit var email : String
    private lateinit var password : String
    private var userName : String ?= null
    private var nameOfService : String ?= null
    private lateinit var auth : FirebaseAuth
    private lateinit var database : DatabaseReference
    private val binding: ActivityLoginBinding by lazy {
        ActivityLoginBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.enableEdgeToEdge()
        setContentView(binding.root)

        //initializing firebase auth
        auth = FirebaseAuth.getInstance()
        //initializing firebase database
        database = Firebase.database.reference


        binding.loginButton.setOnClickListener {
            email = binding.email.text.toString().trim()
            password = binding.passwordLogin.text.toString().trim()

            if(email.isBlank() || password.isBlank()){
                Toast.makeText(this, "Please fill all the fields", Toast.LENGTH_SHORT).show()
            }else{
                createAccount(email, password)
            }
        }
        binding.dontHaveAccountButton.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }
        ViewCompat.setOnApplyWindowInsetsListener(
            findViewById<View?>(R.id.main),
            OnApplyWindowInsetsListener { v: View?, insets: WindowInsetsCompat? ->
                val systemBars = insets!!.getInsets(WindowInsetsCompat.Type.systemBars())
                v!!.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
                insets
            })
    }

    private fun createAccount(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if(task.isSuccessful){
                val user : FirebaseUser? = auth.currentUser
                Toast.makeText(this, "Login Successful", Toast.LENGTH_SHORT).show()
                updateUi(user)
            }else{
                auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener {task ->
                      if(task.isSuccessful){
                          val user : FirebaseUser? = auth.currentUser
                          Toast.makeText(this, "User Created Successfully", Toast.LENGTH_SHORT).show()
                          saveUserData()
                          updateUi(user)

                      }else{
                          Toast.makeText(this, "Account Creation Failed", Toast.LENGTH_SHORT).show()
                          Log.d("Account", "createAccount: Failure", task.exception)
                      }
                }
            }
        }
    }

    private fun saveUserData() {
        email = binding.email.text.toString().trim()
        password = binding.passwordLogin.text.toString().trim()

        val user = UserModel(userName, nameOfService, email, password)
        val userId : String? = FirebaseAuth.getInstance().currentUser?.uid
        userId?.let{
            database.child("users").child(it).setValue(user)
        }
    }

    private fun updateUi(user: FirebaseUser?) {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}