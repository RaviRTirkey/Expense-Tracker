package com.example.expensetracker

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.expensetracker.databinding.FragmentLaunchScreenBinding

@SuppressLint("CustomSplashScreen")
class LaunchScreen : Fragment() {

    private var _binding: FragmentLaunchScreenBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Check login status and navigate to HomeScreen if logged in
        if (SharedPreferencesManager().isLoggedIn(requireContext())) {
            navigateToHomeScreen()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLaunchScreenBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Handle button click for navigating to SignUpScreen
        if (SharedPreferencesManager().isLoggedIn(requireContext())) {
            navigateToHomeScreen()
        } else {
            binding.btnGetStarted.setOnClickListener {
                // Navigate to SignUpScreen and remove LaunchScreen
                findNavController().navigate(R.id.action_launchScreen_to_signupScreen)
                removeFragment()
            }
        }
    }

    private fun navigateToHomeScreen() {
        // Navigate to HomeScreen and remove LaunchScreen
        findNavController().navigate(R.id.action_launchScreen_to_homeScreen)
        removeFragment()
    }

    private fun removeFragment() {
        // Remove this fragment and commit the transaction
        parentFragmentManager.beginTransaction().remove(this).commit()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
