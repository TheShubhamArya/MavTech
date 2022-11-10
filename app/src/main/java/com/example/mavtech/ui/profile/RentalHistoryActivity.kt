package com.example.mavtech

import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mavtech.ui.home.HomeRecyclerViewAdapter
import com.example.mavtech.ui.home.TechDevice
import com.example.mavtech.ui.profile.HistoryRecyclerViewAdapter

enum class HistoryType {
    current, all
}

class RentalHistoryActivity: AppCompatActivity() {

    private var header : String? = "History"
    private var historyType : HistoryType = HistoryType.all

    private lateinit var recyclerView: RecyclerView
    var devices = listOf<TechDevice>(
        TechDevice("Macbook Pro 2022", "laptop"),
        TechDevice("Macbook Air", "laptop"),
        TechDevice("iPhone 14 Pro", "mobile"),
        TechDevice("iPhone 14", "mobile"),
        TechDevice("iPad", "tablet"),
        TechDevice("Apple Watch Series 8", "smart watch"),
        TechDevice("Airpods Pro", "headphones"),
        TechDevice("Airpods Pro 2", "headphones"),
        TechDevice("iPhone 13 Pro", "mobile"),
        TechDevice("iPhone 13", "mobile"),
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.rental_history)
        header = intent.getSerializableExtra("header") as? String
        historyType = intent.getSerializableExtra("historyType") as HistoryType
        assert(
            supportActionBar != null //null check
        )

        (supportActionBar ?: return).setDisplayHomeAsUpEnabled(true)
        (supportActionBar ?: return).title = header
        if (historyType == HistoryType.current) {
            recyclerView = findViewById(R.id.historyRecyclerView)
            recyclerView.layoutManager = LinearLayoutManager(this)
            recyclerView.adapter = HistoryRecyclerViewAdapter(devices) {

            }
        } else {
            recyclerView = findViewById(R.id.historyRecyclerView)
            recyclerView.layoutManager = LinearLayoutManager(this)
            recyclerView.adapter = HistoryRecyclerViewAdapter(devices) {

            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}
