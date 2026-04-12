package com.example.adminserviceprovider.adapter

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.adminserviceprovider.databinding.ItemBinding
import com.example.adminserviceprovider.model.AllMenu
import com.google.firebase.database.DatabaseReference

class MenuItemAdapter(
    private val context: Context,
    private val menuList: ArrayList<AllMenu>,
    databaseReference: DatabaseReference,
    private val onDeleteClickListener : (position : Int) -> Unit
) : RecyclerView.Adapter<MenuItemAdapter.AddItemViewHolder>(){
    private val itemQuantities = IntArray(menuList.size){1}
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AddItemViewHolder {
        val binding = ItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AddItemViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: AddItemViewHolder,
        position: Int
    ) {
        holder.bind(position)
    }

    override fun getItemCount(): Int = menuList.size

    inner class AddItemViewHolder(private val binding: ItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            binding.apply {
                val quantity = itemQuantities[position]
                val menuItem = menuList[position]
                val uriString = menuItem.serviceImage
                val uri = Uri.parse(uriString)

                servicenameTextView.text = menuItem.serviceName
                pendingOrderQuantity.text = menuItem.servicePrice
                Glide.with(context).load(uri).into(serviceImageView)

                quantityTextView.text = itemQuantities[position].toString()
                minusButton.setOnClickListener {
                    decreaseQuantity(position)
                }
                plusButton.setOnClickListener {
                    increaseQuantity(position)
                }
                deleteButton.setOnClickListener {
                    onDeleteClickListener(position)
                }
            }
        }

        private fun decreaseQuantity(position: Int) {
            if(itemQuantities[position] > 1){
                itemQuantities[position]--
                binding.quantityTextView.text = itemQuantities[position].toString()
            }
        }
        private fun increaseQuantity(position: Int) {
            if(itemQuantities[position] < 5){
                itemQuantities[position]++
                binding.quantityTextView.text = itemQuantities[position].toString()
            }
        }
        private fun deleteQuantity(position: Int) {
            menuList.removeAt(position)
            menuList.removeAt(position)
            menuList.removeAt(position)
            notifyItemRemoved(position)
            notifyItemRangeChanged(position, menuList.size)

        }
    }
}