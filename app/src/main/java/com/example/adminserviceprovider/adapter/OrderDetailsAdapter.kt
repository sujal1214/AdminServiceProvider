package com.example.adminserviceprovider.adapter

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.adminserviceprovider.databinding.OrderDetailsItemsBinding

class OrderDetailsAdapter(
    private val context : Context,
    private val serviceNames: ArrayList<String>,
    private val serviceImages: ArrayList<String>,
    private val serviceQuantities: ArrayList<Int>,
    private val servicePrices: ArrayList<String>
    ) : RecyclerView.Adapter<OrderDetailsAdapter.OrderDetailsViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        ViewType: Int
    ): OrderDetailsViewHolder {
        val binding = OrderDetailsItemsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return OrderDetailsViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: OrderDetailsViewHolder,
        position: Int
    ) {
       holder.bind(position)
    }

    override fun getItemCount(): Int = serviceNames.size


    inner class OrderDetailsViewHolder(private val binding: OrderDetailsItemsBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            binding.apply{
                serviceName.text = serviceNames[position]
                serviceQuantity.text = serviceQuantities[position].toString()
                servicePrice.text = servicePrices[position]
                val uriString = serviceImages[position]
                val uri = Uri.parse(uriString)
                Glide.with(context).load(uri).into(serviceImage)
            }
        }

    }
}