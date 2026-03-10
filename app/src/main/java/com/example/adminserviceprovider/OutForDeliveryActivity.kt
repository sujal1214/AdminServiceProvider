package com.example.adminserviceprovider

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.OnApplyWindowInsetsListener
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.adminserviceprovider.adapter.DeliveryAdapter
import com.example.adminserviceprovider.databinding.ActivityOutForDeliveryBinding
import com.example.adminserviceprovider.databinding.DeliveryItemBinding

class OutForDeliveryActivity : AppCompatActivity() {
    private val binding : ActivityOutForDeliveryBinding by lazy {
        ActivityOutForDeliveryBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.enableEdgeToEdge()
        setContentView(binding.root)
        binding.backButton.setOnClickListener {
            finish()
        }
        val customerName = arrayListOf(
            "Sujal",
            "Piyush",
            "Purvesh"
        )
        val moneyStatus = arrayListOf(
            "recieved",
            "notRecieved",
            "Pending"
        )
        val adapter = DeliveryAdapter(customerName, moneyStatus)
        binding.deliveryRecyclerView.adapter = adapter
        binding.deliveryRecyclerView.layoutManager = LinearLayoutManager(this)

        ViewCompat.setOnApplyWindowInsetsListener(
            findViewById<View?>(R.id.main),
            OnApplyWindowInsetsListener { v: View?, insets: WindowInsetsCompat? ->
                val systemBars = insets!!.getInsets(WindowInsetsCompat.Type.systemBars())
                v!!.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
                insets
            })
    }
}