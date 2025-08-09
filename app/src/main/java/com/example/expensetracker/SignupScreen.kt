package com.example.expensetracker

import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.expensetracker.databinding.FragmentSignupScreenBinding

class SignupScreen : Fragment() {

    private var _binding: FragmentSignupScreenBinding? = null
    private val binding get() = _binding!!

    private lateinit var app: ThisApplication


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentSignupScreenBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        app = requireActivity().application as ThisApplication

        binding.signupButton.setOnClickListener{
            val email = binding.etCreateEmail.text.toString()
            val password = binding.etCreatePassword.text.toString()
            val checkBox = binding.checkBox.isChecked

            if(email.isNotEmpty() && password.isNotEmpty() && checkBox){
                app.auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener {
                    if (it.isSuccessful){
                        Toast.makeText(requireContext(), "Account created successfully", Toast.LENGTH_SHORT).show()
                        findNavController().navigate(R.id.action_signupScreen_to_logInPage)
                    }
                }.addOnFailureListener {
                    Toast.makeText(requireContext(), it.localizedMessage, Toast.LENGTH_SHORT).show()
                }
            }else{
                Toast.makeText(requireContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show()
            }
        }


        // Navigate to LogInPage when "Log in" text is clicked
        clickLogin()

    }

    // Navigate to LogInPage when "Log in" text is clicked
    private fun clickLogin() {

        val textView = binding.alreadyHaveAccountText

        val text = "Already have an account? Log in"
        // Create a SpannableString for the  "Already have an account? Log in"
        val textSpannable = SpannableString(text)

        //Define the clickable span for "Log in"
        val clickableSpan = object : ClickableSpan() {
            override fun onClick(widget: View) {
                findNavController().navigate(R.id.action_signupScreen_to_logInPage)
            }

        }

        // Set the clickable span for the "Log in" part of the text
        textSpannable.setSpan(clickableSpan, textSpannable.indexOf("Log in"), textSpannable.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

        //set the spannable string to the TextView
        textView.text = textSpannable
        textView.movementMethod = LinkMovementMethod.getInstance()


    }

    override fun onDestroyView() {
        super.onDestroyView()
        // Clean up the binding to avoid memory leaks
        _binding = null
    }

}