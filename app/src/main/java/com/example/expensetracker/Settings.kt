package com.example.expensetracker

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.expensetracker.databinding.FragmentSettingsBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.auth

class Settings : Fragment() {
    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val user = Firebase.auth.currentUser
        val email = user?.email ?: "Unknown User"

        binding.usernameTextView.text = "User Email: ${email}"

        binding.logoutButton.setOnClickListener {
            Firebase.auth.signOut()
            findNavController().navigate(R.id.launchScreen)

            SharedPreferencesManager().clearLoginState(requireContext())
            Toast.makeText(requireContext(), "Logged Out", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}