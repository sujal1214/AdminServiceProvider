package com.example.adminserviceprovider

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.adminserviceprovider.adapter.PendingOrderAdapter
import com.example.adminserviceprovider.databinding.ActivityPendingOrderBinding

class PendingOrderActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPendingOrderBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 1. Enable Edge-to-Edge
        enableEdgeToEdge()

        // 2. Initialize Binding
        binding = ActivityPendingOrderBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 3. Back Button logic
        binding.backButton.setOnClickListener {
            finish()
        }

        // 4. Dummy Data
        val orderedCustomerName = arrayListOf(
            "Sujal",
            "Piyush",
            "Baccha"
        )
        val orderedQuantity = arrayListOf(
            "3",
            "4",
            "5"
        )
        val orderedFoodImage = arrayListOf(
            R.drawable.sujal,
            R.drawable.piyush,
            R.drawable.baccha
        )

        // 5. Setup Adapter
        val adapter = PendingOrderAdapter(orderedCustomerName, orderedQuantity, orderedFoodImage, this)
        binding.pendingOrderRecyclerView.adapter = adapter
        binding.pendingOrderRecyclerView.layoutManager = LinearLayoutManager(this)

        // 6. Fix Window Insets (using binding.root instead of findViewById)
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}