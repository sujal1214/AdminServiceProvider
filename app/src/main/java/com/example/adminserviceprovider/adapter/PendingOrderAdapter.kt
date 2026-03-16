package com.example.adminserviceprovider.adapter

import android.R.id.message
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.adminserviceprovider.databinding.PendingOrderItemsBinding

class PendingOrderAdapter(private val customerNames: ArrayList<String>, private val quantity: ArrayList<String>, private val foodImage: ArrayList<Int>, private val context: Context) : RecyclerView.Adapter<PendingOrderAdapter.PendingOrderViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PendingOrderViewHolder {
        val binding= PendingOrderItemsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PendingOrderViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: PendingOrderViewHolder,
        position: Int
    ) {
        holder.bind(position)
    }

    override fun getItemCount(): Int = customerNames.size

    inner class PendingOrderViewHolder(private val binding: PendingOrderItemsBinding) : RecyclerView.ViewHolder(binding.getRoot()) {
        private var isAccepted = false
        fun bind(position: Int) {
            binding.apply{
                customerName.text = customerNames[position]
                pendingOrderQuantity.text = quantity[position]
                orderFoodImage.setImageResource(foodImage[position])

                orderAcceptButton.apply{
                    if(!isAccepted){
                        text = "Accept"
                    }else{
                        text = "Dispatched"
                    }
                    setOnClickListener {
                        if(!isAccepted){
                            text = "Dispatched"
                            isAccepted = true
                            showToast("Order Accepted")
                         }else{
                            customerNames.removeAt(adapterPosition)
                            notifyItemRemoved(adapterPosition)
                            showToast("Order Dispatched")

                        }
                    }
                }
            }

        }
        private fun showToast(message: String){
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }

    }
}