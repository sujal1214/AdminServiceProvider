package com.example.adminserviceprovider.adapter

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.adminserviceprovider.databinding.PendingOrderItemsBinding

class PendingOrderAdapter(
    private val context: Context,
    private val customerNames: MutableList<String>,
    private val quantity: MutableList<String>,
    private val listOfServiceImage: MutableList<String>,
    private val itemClicked: OnItemClicked
) : RecyclerView.Adapter<PendingOrderAdapter.PendingOrderViewHolder>() {

    interface OnItemClicked {
        fun onItemClickedListener(position: Int)
        fun onItemAcceptClickListener(position: Int)
        fun onItemDispatchClickListener(position: Int)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PendingOrderViewHolder {
        val binding = PendingOrderItemsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PendingOrderViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: PendingOrderViewHolder,
        position: Int
    ) {
        holder.bind(position)
    }

    override fun getItemCount(): Int = customerNames.size

    inner class PendingOrderViewHolder(private val binding: PendingOrderItemsBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private var isAccepted = false

        fun bind(position: Int) {
            binding.apply {
                customerName.text = customerNames[position]
                pendingOrderQuantity.text = quantity[position]
                
                // Use the constructor parameter listOfServiceImage to avoid conflict with ImageView ID
                val uriString = listOfServiceImage[position]
                val uri = Uri.parse(uriString)
                Glide.with(context).load(uri).into(serviceImage)

                orderAcceptButton.apply {
                    text = if (!isAccepted) "Accept" else "Dispatched"
                    
                    setOnClickListener {
                        if (!isAccepted) {
                            text = "Dispatched"
                            isAccepted = true
                            showToast("Order Accepted")
                            itemClicked.onItemAcceptClickListener(position)
                        } else {
                            customerNames.removeAt(bindingAdapterPosition)
                            notifyItemRemoved(bindingAdapterPosition)
                            showToast("Order Dispatched")
                            itemClicked.onItemDispatchClickListener(position)
                        }
                    }
                }
                itemView.setOnClickListener {
                    itemClicked.onItemClickedListener(position)
                }
            }
        }

        private fun showToast(message: String) {
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }
    }
}
