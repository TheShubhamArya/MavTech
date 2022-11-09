package com.example.mavtech

import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mavtech.ui.home.HomeRecyclerViewAdapter
import com.example.mavtech.ui.home.TechDevice
import com.example.mavtech.ui.profile.HistoryRecyclerViewAdapter

class RentalHistoryActivity: AppCompatActivity() {

    private var header : String? = "History"
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
        assert(
            supportActionBar != null //null check
        )

        (supportActionBar ?: return).setDisplayHomeAsUpEnabled(true)
        (supportActionBar ?: return).title = header
        recyclerView = findViewById(R.id.historyRecyclerView)
        recyclerView.setBackgroundColor(Color.DKGRAY)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = HistoryRecyclerViewAdapter(devices) {

        }
//        recyclerView.adapter = HomeRecyclerViewAdapter(devices) { selectedDevice: TechDevice ->
////            listItemClicked(selectedDevice)
//        }

    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}
