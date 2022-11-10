package com.example.mavtech.ui.profile

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.mavtech.HistoryType
import com.example.mavtech.RentalHistoryActivity
import com.example.mavtech.SigninActivity
import com.example.mavtech.databinding.FragmentProfileBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private lateinit var logoutButton : Button
    private lateinit var currentRentalButton : Button
    private lateinit var rentalHistoryButton : Button

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

        currentRentalButton.setOnClickListener {
            // get the list of devices currently rented by the user
            // send these devices to the RentalHistoryActivity
            val intent = Intent(binding.root.context, RentalHistoryActivity::class.java)
            intent.putExtra("header", "Currently Rented")
            intent.putExtra("historyType", HistoryType.current)
            startActivity(intent)
        }

        rentalHistoryButton.setOnClickListener {
            // get the history list of devices rented by the user
            // send these devices to the RentalHistoryActivity
            val intent = Intent(binding.root.context, RentalHistoryActivity::class.java)
            intent.putExtra("header", "Rental History")
            intent.putExtra("historyType", HistoryType.all)
            startActivity(intent)
        }

        logoutButton.setOnClickListener {
            // Add firebase function here to log out
            // Inside firebase function, when logout is successful, add the bottom 2 lines
            Firebase.auth.signOut()
            val intent = Intent(binding.root.context, SigninActivity::class.java)
            startActivity(intent)
        }
        return root
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}