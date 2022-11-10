package com.example.mavtech

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mavtech.ui.profile.HistoryRecyclerViewAdapter

enum class HistoryType {
    current, all
}

class RentalHistoryActivity: AppCompatActivity() {

    private var header : String? = "History"
    private var historyType : HistoryType = HistoryType.all

    private lateinit var recyclerView: RecyclerView
    private var devices = arrayListOf<RentalItem>()
    private var currentlyRentedDevices = arrayListOf<RentalItem>()
    private var historyRentedDevices = arrayListOf<RentalItem>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.rental_history)
        header = intent.getSerializableExtra("header") as? String
        historyType = intent.getSerializableExtra("historyType") as HistoryType
        currentlyRentedDevices = intent.getSerializableExtra("currentRentalDevices") as ArrayList<RentalItem>
        historyRentedDevices = intent.getSerializableExtra("allRentedDevices") as ArrayList<RentalItem>
        assert(
            supportActionBar != null //null check
        )

        (supportActionBar ?: return).setDisplayHomeAsUpEnabled(true)
        (supportActionBar ?: return).title = header
        if (historyType == HistoryType.current) {
            recyclerView = findViewById(R.id.historyRecyclerView)
            recyclerView.layoutManager = LinearLayoutManager(this)
            recyclerView.adapter = HistoryRecyclerViewAdapter(currentlyRentedDevices) {
            }
        } else {
            recyclerView = findViewById(R.id.historyRecyclerView)
            recyclerView.layoutManager = LinearLayoutManager(this)
            recyclerView.adapter = HistoryRecyclerViewAdapter(historyRentedDevices) {

            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}
