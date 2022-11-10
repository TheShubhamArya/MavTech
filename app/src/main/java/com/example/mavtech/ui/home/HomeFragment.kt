package com.example.mavtech.ui.home

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.SearchView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mavtech.ItemDetailActivity
import com.example.mavtech.R
import com.example.mavtech.RentalItem
import com.example.mavtech.databinding.FragmentHomeBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase


class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    var devices = arrayListOf<RentalItem>()
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var dbref: DatabaseReference

    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!!
    private lateinit var recyclerView: RecyclerView

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

        firebaseAuth = Firebase.auth
        val currentUser = firebaseAuth.currentUser

        homeViewModel.text.observe(viewLifecycleOwner) {

        }
        getDataFromFirebase()
        setHasOptionsMenu(true)
        return root
    }

    private fun getDataFromFirebase() {
        dbref = FirebaseDatabase.getInstance().getReference("RentalItem")
        dbref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    devices = arrayListOf<RentalItem>()
                    for (userSnapshot in snapshot.children) {
                        val device = userSnapshot.getValue(RentalItem::class.java)
                        if (device != null) {
                            if (device.rentedByUserID?.isEmpty() == true) {
                                devices.add(device)
                            }
                        }
                    }
                }
                recyclerView.adapter =
                    HomeRecyclerViewAdapter(devices) { selectedDevice: RentalItem ->
                        listItemClicked(selectedDevice)
                    }
            }
            override fun onCancelled(error: DatabaseError) {
            }

        })
    }


    @Deprecated("Deprecated in Java")
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {

        inflater.inflate(R.menu.nav_menu, menu)
        val search = menu.findItem(R.id.nav_search)
        val searchView = search?.actionView as SearchView
        searchView.queryHint = "Search by name"

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                val filteredDevices = devices.filter { device ->
                    device.name!!.lowercase().contains(newText.toString().lowercase())
                }
                recyclerView.adapter =
                    HomeRecyclerViewAdapter(filteredDevices) { selectedDevice: RentalItem ->
                        listItemClicked(selectedDevice)
                    }
                return true
            }
        })
    }

    @Deprecated("This func is deprecated")
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.filter_byLaptop ->
                filterBy("laptop")

            R.id.filter_byHeadphones ->
                filterBy("headphones")

            R.id.filter_byTablet ->
                filterBy("tablet")

            R.id.filter_byMobile ->
                filterBy("mobile")

            R.id.filter_byAll ->
                recyclerView.adapter =
                    HomeRecyclerViewAdapter(devices) { selectedDevice: RentalItem ->
                        listItemClicked(selectedDevice)
                    }
        }
        return false
    }

    private fun filterBy(type: String) {
        val filteredDevices = devices.filter { device ->
            device.type == type
        }
        recyclerView.adapter =
            HomeRecyclerViewAdapter(filteredDevices) { selectedDevice: RentalItem ->
                listItemClicked(selectedDevice)
            }
    }

    private fun listItemClicked(device: RentalItem) {
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
