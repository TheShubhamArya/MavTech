package com.example.mavtech.ui.profile

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mavtech.R
import com.example.mavtech.ui.home.TechDevice

class HistoryRecyclerViewAdapter (
    private val devices: List<TechDevice>,
    private val clickListener: (TechDevice)->Unit
): RecyclerView.Adapter<HistoryViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val listItem = layoutInflater.inflate(R.layout.history_list_item, parent, false)
        return HistoryViewHolder(listItem)
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        val device = devices[position]
        holder.bind(device, clickListener)
    }

    override fun getItemCount(): Int {
        return devices.size
    }
}

class HistoryViewHolder(val view: View): RecyclerView.ViewHolder(view) {
    val deviceNameTV = view.findViewById<TextView>(R.id.tvNameHistory)
    val deviceImageView = view.findViewById<ImageView>(R.id.historyDeviceImageView)
    val deviceTypeTV = view.findViewById<TextView>(R.id.tvTypeHistory)
    fun bind(device: TechDevice, clickListener: (TechDevice)->Unit) {
        deviceNameTV.text = "${device.name}"
        deviceTypeTV.text = "${device.type}"
        view.setOnClickListener{
            clickListener(device)
        }
    }
}