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
import com.example.adminserviceprovider.model.OrderDetails
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class OutForDeliveryActivity : AppCompatActivity() {
    private val binding : ActivityOutForDeliveryBinding by lazy {
        ActivityOutForDeliveryBinding.inflate(layoutInflater)
    }
    private lateinit var database : FirebaseDatabase
    private var listOfComplateOrderList : ArrayList<OrderDetails> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.enableEdgeToEdge()
        setContentView(binding.root)
        binding.backButton.setOnClickListener {
            finish()
        }
        //retrieve and display complete order
        retrieveCompleteOrderDetails()

        ViewCompat.setOnApplyWindowInsetsListener(
            findViewById<View?>(R.id.main),
            OnApplyWindowInsetsListener { v: View?, insets: WindowInsetsCompat? ->
                val systemBars = insets!!.getInsets(WindowInsetsCompat.Type.systemBars())
                v!!.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
                insets
            })
    }

    private fun retrieveCompleteOrderDetails() {
        //initialize firebase database
        database = FirebaseDatabase.getInstance()
        val completeOrderReference = database.reference.child("CompletedOrder")
            .orderByChild("currentTime")
        completeOrderReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                // clear the list before populating it with new data
                listOfComplateOrderList.clear()
                for(orderSnapshot in snapshot.children){
                    val completeOrder =orderSnapshot.getValue(OrderDetails::class.java)
                    completeOrder?.let {
                        listOfComplateOrderList.add(it)
                    }
                }
                //reverse the list to display latest order first
                listOfComplateOrderList.reverse()
                setDataIntoRecyclerView()
            }
            override fun onCancelled(error: DatabaseError) {

            }
        })
    }
    private fun setDataIntoRecyclerView() {
        //Initialization list to hold customer name and payment status
        val customerName = mutableListOf<String>()
        val  moneyStatus = mutableListOf<Boolean>()

        for(order in listOfComplateOrderList){
            order.userName?.let {
                customerName.add(it)
            }
            moneyStatus.add(order.paymentReceived)
        }
        val adapter  = DeliveryAdapter(customerName, moneyStatus)
        binding.deliveryRecyclerView.adapter = adapter
        binding.deliveryRecyclerView.layoutManager = LinearLayoutManager(this)
    }
}