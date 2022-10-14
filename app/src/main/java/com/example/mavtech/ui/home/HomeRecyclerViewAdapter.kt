package com.example.mavtech.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mavtech.R
import org.w3c.dom.Text
import java.io.Serializable

data class TechDevice (
    val name: String,
    val type: String
) : Serializable


class HomeRecyclerViewAdapter (
    private val devices: List<TechDevice>,
    private val clickListener: (TechDevice)->Unit
): RecyclerView.Adapter<MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val listItem = layoutInflater.inflate(R.layout.list_item, parent, false)
        return MyViewHolder(listItem)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val device = devices[position]
        holder.bind(device, clickListener)
    }

    override fun getItemCount(): Int {
        return devices.size
    }
}

class MyViewHolder(val view: View): RecyclerView.ViewHolder(view) {
    val deviceNameTV = view.findViewById<TextView>(R.id.tvName)
    val deviceImageView = view.findViewById<ImageView>(R.id.deviceImageView)
    val deviceTypeTV = view.findViewById<TextView>(R.id.tvType)
    fun bind(device: TechDevice, clickListener: (TechDevice)->Unit) {
        deviceNameTV.text = "${device.name}"
        deviceTypeTV.text = "${device.type}"
        view.setOnClickListener{
            clickListener(device)
        }
    }
}