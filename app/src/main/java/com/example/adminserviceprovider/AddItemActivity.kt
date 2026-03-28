package com.example.adminserviceprovider

import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.OnApplyWindowInsetsListener
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.adminserviceprovider.databinding.ActivityAdditemBinding
import com.example.adminserviceprovider.model.AllMenu
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage

class AddItemActivity : AppCompatActivity() {

    //Service facility details
    private lateinit var serviceName : String
    private lateinit var servicePrice : String
    private lateinit var serviceDescription : String
    private lateinit var serviceFacility : String
    private  var serviceImageUri : Uri? = null

    //Firebase
    private lateinit var auth : FirebaseAuth
    private lateinit var database : FirebaseDatabase
    private val binding: ActivityAdditemBinding by lazy {
        ActivityAdditemBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.enableEdgeToEdge()
        setContentView(binding.root)

        //initialize firebase
        auth = FirebaseAuth.getInstance()
        //initializing Firebase database instance
        database = FirebaseDatabase.getInstance()

        binding.AddItemButton.setOnClickListener {
            //Get Data from Firebase
            serviceName = binding.ServiceName.text.toString().trim()
            servicePrice = binding.ServicePrice.text.toString().trim()
            serviceDescription = binding.description.text.toString().trim()
            serviceFacility = binding.facility.text.toString().trim()

            if(!(serviceName.isBlank() || servicePrice.isBlank() || serviceDescription.isBlank() || serviceFacility.isBlank())){
                uploadData()
                Toast.makeText(this, "Item added Successfully", Toast.LENGTH_SHORT).show()
                finish()
            }else{
                Toast.makeText(this, "Fill all Details", Toast.LENGTH_SHORT).show()
            }
        }

        binding.selectImage.setOnClickListener {
            pickImage.launch("image/*")
        }

        binding.backButton.setOnClickListener {
            finish()
        }

        ViewCompat.setOnApplyWindowInsetsListener(
            findViewById<View>(R.id.main),
            OnApplyWindowInsetsListener { v: View?, insets: WindowInsetsCompat? ->
                val systemBars = insets!!.getInsets(WindowInsetsCompat.Type.systemBars())
                v!!.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
                insets
            })
    }

    fun uploadData() {
        //get a reference to the "menu" node in the database
        val menuRef : DatabaseReference = database.getReference("menu")
        //Generate a unique key for the new menu item
        val newItemKey : String? = menuRef.push().key

        if(serviceImageUri != null){
            val storageRef = FirebaseStorage.getInstance().reference
            val imageRef = storageRef.child("menu_images/${newItemKey}.jpg")
            val uploadTask = imageRef.putFile(serviceImageUri!!)

            uploadTask.addOnFailureListener { //Changed
                imageRef.downloadUrl.addOnFailureListener { //Changed
                    downloadUrl->
                    //Create a new menu item
                    val newItem = AllMenu(
                        serviceName  = serviceName,
                        servicePrice = servicePrice,
                        serviceDescription = serviceDescription,
                        serviceFacility = serviceFacility,
                        serviceImage = downloadUrl.toString()
                    )
                    newItemKey?.let{
                            key ->
                        menuRef.child(key).setValue(newItem).addOnSuccessListener {
                            Toast.makeText(this, "Data Uploaded Successfully", Toast.LENGTH_SHORT).show()
                        }
                            .addOnSuccessListener {
                                Toast.makeText(this, "Data Upload Failed", Toast.LENGTH_SHORT).show()
                            }
                    }
                }
            }
                .addOnFailureListener {
                    Toast.makeText(this, "Image Upload Failed", Toast.LENGTH_SHORT).show()
                }
        } else{
                Toast.makeText(this, "Please Select an Image", Toast.LENGTH_SHORT).show()
            }
    }



    private val pickImage = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        if (uri != null) {
            binding.selectedImage.setImageURI(uri)
            serviceImageUri = uri
        }
    }
}