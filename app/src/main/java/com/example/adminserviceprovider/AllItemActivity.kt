package com.example.adminserviceprovider

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.OnApplyWindowInsetsListener
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.adminserviceprovider.adapter.AddItemAdapter
import com.example.adminserviceprovider.databinding.ActivityAllItemBinding

class AllItemActivity : AppCompatActivity() {
    private val binding: ActivityAllItemBinding by lazy{
        ActivityAllItemBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.enableEdgeToEdge()
        setContentView(binding.root)
        val menuServiceName = listOf("Painter", "Carpenter", "Electrician", "Plumber")
        val menuServicePrice = listOf("$35", "$40", "$45", "$50")
        val menuServiceImage = listOf(R.drawable.painter, R.drawable.carpenter, R.drawable.electrician, R.drawable.plumber)
        binding.backButton.setOnClickListener {
            finish()
        }
        val adapter = AddItemAdapter(ArrayList(menuServiceName), ArrayList(menuServicePrice), ArrayList(menuServiceImage))
        binding.MenuRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.MenuRecyclerView.adapter = adapter
        ViewCompat.setOnApplyWindowInsetsListener(
            findViewById<View?>(R.id.main),
            OnApplyWindowInsetsListener { v: View?, insets: WindowInsetsCompat? ->
                val systemBars = insets!!.getInsets(WindowInsetsCompat.Type.systemBars())
                v!!.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
                insets
            })
    }
}