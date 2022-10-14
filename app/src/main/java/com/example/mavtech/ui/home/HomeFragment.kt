package com.example.mavtech.ui.home

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.*
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mavtech.ItemDetailActivity
import com.example.mavtech.databinding.FragmentHomeBinding


class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
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

    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!!
    private lateinit var recyclerView : RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        recyclerView = binding.homeRecyclerView
        recyclerView.setBackgroundColor(Color.DKGRAY)
        recyclerView.layoutManager = LinearLayoutManager(binding.homeRecyclerView.context)
        recyclerView.adapter = HomeRecyclerViewAdapter(devices) { selectedDevice: TechDevice ->
            listItemClicked(selectedDevice)
        }
//        val textView: TextView = binding.textHome
        homeViewModel.text.observe(viewLifecycleOwner) {
//            textView.text = it
        }
        setHasOptionsMenu(true)
        return root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {

        inflater.inflate(com.example.mavtech.R.menu.nav_menu,menu)
        val search = menu.findItem(com.example.mavtech.R.id.nav_search)
        val searchView = search?.actionView as SearchView
        searchView.queryHint = "Search by name"

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                val filteredDevices = devices.filter { device ->
                    device.name.lowercase().contains(newText.toString().lowercase())
                }
                recyclerView.adapter = HomeRecyclerViewAdapter(filteredDevices) { selectedDevice: TechDevice ->
                    listItemClicked(selectedDevice)
                }
                return true
            }
        })

    }

    @Deprecated("This func is deprecated")
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            com.example.mavtech.R.id.filter_byLaptop ->
                filterBy("laptop")

            com.example.mavtech.R.id.filter_byHeadphones ->
                filterBy("headphones")

            com.example.mavtech.R.id.filter_byTablet ->
                filterBy("tablet")

            com.example.mavtech.R.id.filter_byMobile ->
                filterBy("mobile")

            com.example.mavtech.R.id.filter_byAll ->
                recyclerView.adapter = HomeRecyclerViewAdapter(devices) { selectedDevice: TechDevice ->
                    listItemClicked(selectedDevice)
                }
        }
        return false
    }

    private fun filterBy(type: String) {
        val filteredDevices = devices.filter { device ->
            device.type == type
        }
        recyclerView.adapter = HomeRecyclerViewAdapter(filteredDevices) { selectedDevice: TechDevice ->
            listItemClicked(selectedDevice)
        }
    }

    private fun listItemClicked(device: TechDevice) {
        val intent = Intent(binding.homeRecyclerView.context, ItemDetailActivity::class.java)
        intent.putExtra("device", device)
        startActivity(intent)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

private fun Fragment.onCreateOptionsMenu(menu: Menu) {

}

private fun LayoutInflater.inflate(navMenu: Int, menu: Menu?) {

}
