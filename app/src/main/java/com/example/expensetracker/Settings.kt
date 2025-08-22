package com.example.expensetracker

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import com.example.expensetracker.databinding.FragmentSettingsBinding
import com.google.firebase.auth.FirebaseAuth
import androidx.core.content.edit
import androidx.fragment.app.activityViewModels
import com.example.expensetracker.room.TransactionDatabase
import com.example.expensetracker.room.TransactionRepository
import com.example.expensetracker.viewModel.TransactionViewModel
import com.example.expensetracker.viewModel.TransactionViewModelFactory
import java.util.Calendar

class Settings : Fragment() {
    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!

    val viewModel: TransactionViewModel by activityViewModels {
        TransactionViewModelFactory(
            TransactionRepository(
                TransactionDatabase.getInstance(requireContext()).transactionDAO,
                FirebaseAuth.getInstance().currentUser?.uid ?: ""
            )
        )
    }

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
        val username = trimAfterSymbol(email, '@')
        binding.usernameTextView.text = username


        //Select Year
        yearSelection()

        //theme change and theme save
        theme()

        //logout function
        binding.logoutButton.setOnClickListener {
            logout()
        }
    }

    //Year Selection function
    fun yearSelection(){
        val currentYear = Calendar.getInstance().get(Calendar.YEAR)

        // Generate a list of years
        val years = (2020..currentYear).toList().reversed()
        //set adapter
        val yearAdapter = ArrayAdapter(requireContext(),android.R.layout.simple_spinner_item,years)
        binding.yearSpinner.adapter = yearAdapter

        // Auto select current year
        val position = years.indexOf(currentYear)
        if (position >= 0) {
            binding.yearSpinner.setSelection(position)
        }

        binding.yearSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                val selectedYear = parent.getItemAtPosition(position).toString()
                // Handle the selected year
                viewModel.setYearFilter(selectedYear.toInt())

            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
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

    //trim text
    fun trimAfterSymbol(input: String, symbol: Char): String {
        val index = input.indexOf(symbol)
        return if (index != -1) input.substring(0, index) else input
    }


    //theme function
    fun theme(){
        //  Load saved theme mode
        val prefs = requireContext().getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        val savedMode = prefs.getInt("night_mode", AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)

        // Set switch state based on saved mode
        binding.themeSwitch.isChecked = (savedMode == AppCompatDelegate.MODE_NIGHT_YES)

        // Switch listener
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
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
