package com.example.adminserviceprovider

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.adminserviceprovider.adapter.OrderDetailsAdapter
import com.example.adminserviceprovider.databinding.ActivityOrderDetailsBinding
import com.example.adminserviceprovider.model.OrderDetails

class OrderDetailsActivity : AppCompatActivity() {
    private val binding: ActivityOrderDetailsBinding by lazy {
        ActivityOrderDetailsBinding.inflate(layoutInflater)
    }

    private var userName: String? = null
    private var address: String? = null
    private var phoneNumber: String? = null
    private var totalPrice: String? = null
    private var serviceNames: ArrayList<String> = arrayListOf()
    private var serviceImages: ArrayList<String> = arrayListOf()
    private var serviceQuantity: ArrayList<Int> = arrayListOf()
    private var servicePrices: ArrayList<String> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        binding.backButtonPayout.setOnClickListener {
            finish()
        }
        getDataFromIntent()
    }

    private fun getDataFromIntent() {
        val receivedOrderDetails = intent.getSerializableExtra("UserOrderDetails") as OrderDetails
        receivedOrderDetails?.let { orderDetails ->
            userName = receivedOrderDetails.userName
            address = receivedOrderDetails.address
            phoneNumber = receivedOrderDetails.phoneNumber
            totalPrice = receivedOrderDetails.totalPrice
            serviceNames = receivedOrderDetails.serviceNames as ArrayList<String>
            serviceImages = receivedOrderDetails.serviceImages as ArrayList<String>
            serviceQuantity = receivedOrderDetails.serviceQuantities as ArrayList<Int>
            servicePrices = receivedOrderDetails.servicePrices as ArrayList<String>

            setUserDetails()
            setAdapter()
        }
    }

    fun setAdapter() {
        binding.orderDetailRecyclerView.layoutManager = LinearLayoutManager(this)
        val adapter = OrderDetailsAdapter(this, serviceNames, serviceImages, serviceQuantity, servicePrices)
        binding.orderDetailRecyclerView.adapter = adapter

    }

    private fun setUserDetails() {
        binding.name.text = userName
        binding.address.text  = address
        binding.phone.text = phoneNumber
        binding.totalPay.text = totalPrice
    }
}
