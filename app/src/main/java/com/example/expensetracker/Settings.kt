package com.example.expensetracker

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import com.example.expensetracker.databinding.FragmentSettingsBinding
import com.google.firebase.auth.FirebaseAuth
import androidx.core.content.edit

class Settings : Fragment() {
    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val user = FirebaseAuth.getInstance().currentUser
        val email = user?.email ?: "Unknown User"
        binding.usernameTextView.text = "User Email: $email"

        // 🔹 Load saved theme mode
        val prefs = requireContext().getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        val savedMode = prefs.getInt("night_mode", AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)

        // 🔹 Set switch state based on saved mode
        binding.themeSwitch.isChecked = (savedMode == AppCompatDelegate.MODE_NIGHT_YES)

        // 🔹 Switch listener
        binding.themeSwitch.setOnCheckedChangeListener { _, isChecked ->
            val mode = if (isChecked) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }

            // Save user choice
            prefs.edit {
                putInt("night_mode", mode)
            }

            // Apply theme
            AppCompatDelegate.setDefaultNightMode(mode)

            val intent = requireActivity().intent
            requireActivity().finish()
            startActivity(intent)

        }

        binding.logoutButton.setOnClickListener {
            logout()
        }
    }

    // Logout function
    private fun logout() {
        FirebaseAuth.getInstance().signOut()
        SharedPreferencesManager().clearLoginState(requireContext())

        val restartIntent = requireActivity().packageManager
            .getLaunchIntentForPackage(requireActivity().packageName)

        if (restartIntent != null) {
            restartIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(restartIntent)
            requireActivity().finish()
        } else {
            Toast.makeText(requireContext(), "Error restarting app", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
