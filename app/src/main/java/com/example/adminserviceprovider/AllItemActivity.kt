package com.example.adminserviceprovider

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.adminserviceprovider.adapter.MenuItemAdapter
import com.example.adminserviceprovider.databinding.ActivityAllItemBinding
import com.example.adminserviceprovider.model.AllMenu
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class AllItemActivity : AppCompatActivity() {

    private lateinit var databaseReference: DatabaseReference
    private lateinit var database: FirebaseDatabase
    private var menuItems: ArrayList<AllMenu> = ArrayList()
    private val binding: ActivityAllItemBinding by lazy {
        ActivityAllItemBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.enableEdgeToEdge()
        setContentView(binding.root)
        
        // Initialize database and databaseReference
        database = FirebaseDatabase.getInstance()
        databaseReference = database.reference
        
        retrieveMenuItem()

        binding.backButton.setOnClickListener {
            finish()
        }
        
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun retrieveMenuItem() {
        val foodRef: DatabaseReference = database.reference.child("menu")

        //fetch data from database
        foodRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                //Clear existing data before populating
                menuItems.clear()

                //loop through each food item
                for (serviceSnapshot in snapshot.children) {
                    val menuItem = serviceSnapshot.getValue(AllMenu::class.java)
                    menuItem?.let {
                        // Important: Set the key from the snapshot if it's not in the object
                        val itemWithKey = it.copy(key = serviceSnapshot.key)
                        menuItems.add(itemWithKey)
                    }
                }
                setAdapter()
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("DatabaseError", "Error: ${error.message}")
            }
        })
    }

    private fun setAdapter() {
        val adapter = MenuItemAdapter(context = this@AllItemActivity, menuItems, databaseReference) { position ->
            deleteMenuItems(position)
        }
        binding.MenuRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.MenuRecyclerView.adapter = adapter
    }

    private fun deleteMenuItems(position: Int) {
        if (position < 0 || position >= menuItems.size) return

        val menuItemDelete = menuItems[position]
        val menuItemKey = menuItemDelete.key
        
        if (menuItemKey != null) {
            val serviceMenuReference = database.reference.child("menu").child(menuItemKey)
            serviceMenuReference.removeValue().addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    menuItems.removeAt(position)
                    binding.MenuRecyclerView.adapter?.notifyItemRemoved(position)
                    Toast.makeText(this, "Item deleted", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Item not deleted", Toast.LENGTH_SHORT).show()
                }
            }
        } else {
            Toast.makeText(this, "Error: Item key not found", Toast.LENGTH_SHORT).show()
        }
    }
}
