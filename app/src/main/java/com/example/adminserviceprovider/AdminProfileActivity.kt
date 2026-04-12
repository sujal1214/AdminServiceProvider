package com.example.adminserviceprovider

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.OnApplyWindowInsetsListener
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.adminserviceprovider.databinding.ActivityAdminProfileBinding
import com.example.adminserviceprovider.model.UserModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class AdminProfileActivity : AppCompatActivity() {
    private val binding : ActivityAdminProfileBinding by lazy {
        ActivityAdminProfileBinding.inflate(layoutInflater)
    }
    private lateinit var auth : FirebaseAuth
    private lateinit var database : FirebaseDatabase
    private lateinit var adminReference : DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.enableEdgeToEdge()

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        adminReference = database.reference.child("users")

        setContentView(binding.root)
        binding.backButton.setOnClickListener {
            finish()
        }

        binding.saveInfoButton.setOnClickListener {
            updateUserData()
        }

        binding.name1.isEnabled = false
        binding.address.isEnabled = false
        binding.email.isEnabled = false
        binding.phone.isEnabled = false
        binding.password1.isEnabled = false
        binding.saveInfoButton.isEnabled = false

        var isEnable = false
        binding.editButton.setOnClickListener {
            isEnable = ! isEnable
            binding.name1.isEnabled = isEnable
            binding.address.isEnabled = isEnable
            binding.email.isEnabled = isEnable
            binding.phone.isEnabled = isEnable
            binding.password1.isEnabled = isEnable
            if(isEnable){
                binding.name1.requestFocus()
            }
            binding.saveInfoButton.isEnabled = isEnable
        }

        retrieveUserData()

        ViewCompat.setOnApplyWindowInsetsListener(
            findViewById<View?>(R.id.main),
            OnApplyWindowInsetsListener { v: View?, insets: WindowInsetsCompat? ->
                val systemBars = insets!!.getInsets(WindowInsetsCompat.Type.systemBars())
                v!!.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
                insets
            })
    }

    private fun retrieveUserData() {
        val currentUserUid = auth.currentUser?.uid
        if(currentUserUid != null) {
            val userReference = adminReference.child(currentUserUid)

            userReference.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    var ownerName = snapshot.child("name").getValue()
                    var email = snapshot.child("email").getValue()
                    var password = snapshot.child("password").getValue()
                    var address = snapshot.child("address").getValue()
                    var phone = snapshot.child("phone").getValue()
                    setDataToTextView(ownerName, email, password, address, phone)
                }

                override fun onCancelled(error: DatabaseError) {

                }
            })
        }
    }

    private fun setDataToTextView(
        ownerName: Any?,
        email: Any?,
        password: Any?,
        address: Any?,
        phone: Any?
    ) {
        binding.name1.setText(ownerName.toString())
        binding.email.setText(email.toString())
        binding.password1.setText(password.toString())
        binding.phone.setText(phone.toString())
        binding.address.setText(address.toString())
    }

    private fun updateUserData() {
        val updateName = binding.name1.text.toString()
        val updateEmail = binding.address.text.toString()
        val updatePassword = binding.password1.text.toString()
        val updateAddress = binding.address.text.toString()
        val updatePhone = binding.phone.text.toString()
        val currentUserUid = auth.currentUser?.uid
        if(currentUserUid != null) {
            val userReference = adminReference.child(currentUserUid)
            userReference.child("name").setValue(updateName)
            userReference.child("email").setValue(updateEmail)
            userReference.child("password").setValue(updatePassword)
            userReference.child("phone").setValue(updateEmail)
            userReference.child("address").setValue(updateAddress)

            Toast.makeText(this, "Profile Updated Successfully", Toast.LENGTH_SHORT).show()
            //update the  email and password for firebase Authentication
            auth.currentUser?.updateEmail(updateEmail)
            auth.currentUser?.updatePassword(updatePassword)

        } else {
                Toast.makeText(this, "Profile Update Failed", Toast.LENGTH_SHORT)
        }
    }
}