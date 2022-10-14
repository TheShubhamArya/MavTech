package com.example.mavtech

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.mavtech.ui.home.TechDevice

class ItemDetailActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.item_detail)
        assert(
            supportActionBar != null //null check
        )
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        val device = intent.getSerializableExtra("device") as? TechDevice
        val tv = findViewById<TextView>(R.id.deviceNameTV)
        tv.text = device?.name

        val deviceImageView = findViewById<ImageView>(R.id.deviceImageView)
        deviceImageView.setImageResource(R.drawable.ic_devices)
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}