package com.example.mavtech.ui.profile

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.mavtech.HistoryType
import com.example.mavtech.RentalHistoryActivity
import com.example.mavtech.RentalItem
import com.example.mavtech.SigninActivity
import com.example.mavtech.databinding.FragmentProfileBinding
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase
import org.w3c.dom.Text

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private lateinit var logoutButton : Button
    private lateinit var currentRentalButton : Button
    private lateinit var rentalHistoryButton : Button
    private lateinit var usernameTV : TextView
    private lateinit var emailTV : TextView
    private lateinit var balanceTV : TextView

    private lateinit var dbref: DatabaseReference
    private lateinit var currentUser : FirebaseUser
    private var currentRentalDevices = arrayListOf<RentalItem>()
    private var allRentalDevices = arrayListOf<RentalItem>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val profileViewModel =
            ViewModelProvider(this).get(ProfileViewModel::class.java)

        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        val root: View = binding.root
        logoutButton = binding.logoutButton
        currentRentalButton = binding.currentButton
        rentalHistoryButton = binding.historyButton
        usernameTV = binding.usernameTV
        emailTV = binding.emailTV
        balanceTV = binding.balanceTV

        currentRentalButton.setOnClickListener {
            val intent = Intent(binding.root.context, RentalHistoryActivity::class.java)
            intent.putExtra("header", "Currently Rented")
            intent.putExtra("historyType", HistoryType.current)
            intent.putExtra("currentRentalDevices",currentRentalDevices)
            intent.putExtra("allRentedDevices",allRentalDevices)
            startActivity(intent)
        }

        rentalHistoryButton.setOnClickListener {
            val intent = Intent(binding.root.context, RentalHistoryActivity::class.java)
            intent.putExtra("header", "Rental History")
            intent.putExtra("historyType", HistoryType.all)
            intent.putExtra("currentRentalDevices",currentRentalDevices)
            intent.putExtra("allRentedDevices",allRentalDevices)
            startActivity(intent)
        }

        logoutButton.setOnClickListener {
            Firebase.auth.signOut()
            val intent = Intent(binding.root.context, SigninActivity::class.java)
            startActivity(intent)
        }

        currentUser = Firebase.auth.currentUser!!
        displayProfileData()
        getCurrentlyRentedItems()
        getAllRentedItems()
        return root
    }

    private fun displayProfileData() {
        val userDbref = FirebaseDatabase.getInstance().getReference("User").child(currentUser.uid)
        userDbref.addValueEventListener(object : ValueEventListener{

            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val name = snapshot.child("name").getValue().toString()
                    val email = snapshot.child("email").getValue().toString()
                    val totalFine = snapshot.child("totalFine").getValue()
                    val finePaid = snapshot.child("finePaid").getValue()
//                    val balance : Float =
                    usernameTV.text = name
                    emailTV.text = email
//                    balanceTV.text = "$${balance}"
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
    }

    private fun getAllRentedItems() {
        dbref = FirebaseDatabase.getInstance().getReference("User").child(currentUser.uid).child("AllRented")

        dbref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                if (snapshot.exists()) {
                    allRentalDevices = arrayListOf<RentalItem>()
                    for (userSnapshot in snapshot.children) {
                        val deviceSnapshot = userSnapshot.child("device")
                        val device = deviceSnapshot.getValue(RentalItem::class.java)

                        if (device != null) {
                            allRentalDevices.add(device)
                        }
                    }
                }
            }
            override fun onCancelled(error: DatabaseError) {

            }

        })
    }

    private fun getCurrentlyRentedItems() {
        dbref = FirebaseDatabase.getInstance().getReference("User").child(currentUser.uid).child("CurrentlyRented")

        dbref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                if (snapshot.exists()) {
                    currentRentalDevices = arrayListOf<RentalItem>()
                    for (userSnapshot in snapshot.children) {
                        val deviceSnapshot = userSnapshot.child("device")
                        val device = deviceSnapshot.getValue(RentalItem::class.java)

                        if (device != null) {
                            currentRentalDevices.add(device)
                        }
                    }
                }
            }
            override fun onCancelled(error: DatabaseError) {

            }

        })
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}