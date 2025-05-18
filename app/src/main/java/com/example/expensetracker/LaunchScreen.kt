package com.example.expensetracker

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.fragment.findNavController
import com.example.expensetracker.databinding.FragmentLaunchScreenBinding


class LaunchScreen : Fragment() {

    private var _binding: FragmentLaunchScreenBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLaunchScreenBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        if(SharedPreferencesManager().isLoggedIn(requireContext())){
            binding.btnGetStarted.setOnClickListener {
                findNavController().navigate(R.id.action_launchScreen_to_homeScreen)
            }
        }else {
            binding.btnGetStarted.setOnClickListener {
                findNavController().navigate(R.id.action_launchScreen_to_signupScreen)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}