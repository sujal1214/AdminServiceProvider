package com.example.adminserviceprovider.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.adminserviceprovider.databinding.ItemBinding

class AddItemAdapter(private val MenuServiceName:ArrayList<String>, private val MenuServicePrice:ArrayList<String>, private val MenuServiceImage:ArrayList<Int>) : RecyclerView.Adapter<AddItemAdapter.AddItemViewHolder>(){
    private val itemQuantities = IntArray(MenuServiceName.size){1}
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

    override fun getItemCount(): Int = MenuServiceName.size

    inner class AddItemViewHolder(private val binding: ItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            binding.apply {
                servicenameTextView.text = MenuServiceName[position]
                priceTextView.text = MenuServicePrice[position]
                serviceImageView.setImageResource(MenuServiceImage[position])

                quantityTextView.text = itemQuantities[position].toString()
                minusButton.setOnClickListener {
                    decreaseQuantity(position)
                }
                plusButton.setOnClickListener {
                    increaseQuantity(position)
                }
                deleteButton.setOnClickListener {
                    deleteQuantity(position)
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
            MenuServiceName.removeAt(position)
            MenuServicePrice.removeAt(position)
            MenuServiceImage.removeAt(position)
            notifyItemRemoved(position)
            notifyItemRangeChanged(position, MenuServiceName.size)

        }
    }
}