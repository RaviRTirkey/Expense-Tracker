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
import com.example.expensetracker.databinding.FragmentLogInPageBinding


class LogInPage : Fragment() {

    private var _binding: FragmentLogInPageBinding? = null
    private val binding get() = _binding!!

    private lateinit var app: ThisApplication

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentLogInPageBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        app = requireActivity().application as ThisApplication
        
        binding.signinButton.setOnClickListener{
            val email = binding.etLogInEmail.text.toString()
            val password = binding.etLogInPassword.text.toString()

            if(email.isNotEmpty() && password.isNotEmpty()){
                app.auth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
                    if (it.isSuccessful){
                        Toast.makeText(requireContext(), "Sign in successful", Toast.LENGTH_SHORT).show()
                        // After a successful login:
                        val userId = app.auth.currentUser?.uid.toString()
                        SharedPreferencesManager().saveLoginState(requireContext(), true, userId)
                        //go to home page
                        findNavController().navigate(R.id.action_logInPage_to_homeScreen)
                    }
                }.addOnFailureListener {
                    Toast.makeText(requireContext(), it.localizedMessage, Toast.LENGTH_SHORT).show()
                }

            }else{
                Toast.makeText(requireContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show()
            }
        }

        // Navigate to SignupPage when "Sign up" text is clicked
        clickSignup()

    }

    // Navigate to SignupPage when "Sign up" text is clicked
    private fun clickSignup() {

        val textView = binding.needToCreateAccountText

        val text = "Need to create an account? Sign up"
        // Create a SpannableString for the  "Need to create an account? Sign up"
        val textSpannable = SpannableString(text)

        //Define the clickable span for "Signup"
        val clickableSpan = object : ClickableSpan() {
            override fun onClick(widget: View) {
                findNavController().navigate(R.id.action_logInPage_to_signupScreen)
            }

        }

        // Set the clickable span for the "Sign up" part of the text
        textSpannable.setSpan(clickableSpan, textSpannable.indexOf("Sign up"), textSpannable.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

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