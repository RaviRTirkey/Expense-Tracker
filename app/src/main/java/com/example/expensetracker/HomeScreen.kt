package com.example.expensetracker

import android.os.Bundle
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.expensetracker.databinding.FragmentHomeScreenBinding
import com.example.expensetracker.room.TransactionDatabase
import com.example.expensetracker.room.TransactionRepository
import com.example.expensetracker.viewModel.TransactionViewModel
import com.example.expensetracker.viewModel.TransactionViewModelFactory
import com.google.firebase.auth.FirebaseAuth


class HomeScreen : Fragment() {

    private var _binding: FragmentHomeScreenBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: TransactionViewModel

    private val months = listOf(
        "All Months", "January", "February", "March", "April", "May", "June",
        "July", "August", "September", "October", "November", "December", "All Months"
    )



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeScreenBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val userId = FirebaseAuth.getInstance().currentUser?.uid
        if (userId == null) {
            findNavController().navigate(R.id.launchScreen)
            return
        }

        // Setting up database, repository, ViewModelFactory, and ViewModel
        val database = TransactionDatabase.getInstance(requireContext())
        val repository = TransactionRepository(database.transactionDAO,userId)
        val factory = TransactionViewModelFactory(repository)
        viewModel = ViewModelProvider(this, factory).get(TransactionViewModel::class.java)

        // Setting up RecyclerView
        val transactionAdapter = TransactionAdapter()
        binding.allTransactionRecyclerView.apply {
            adapter = transactionAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }

        // Listen for input changes using TextWatcher
        binding.searchView2.addTextChangedListener(object : android.text.TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun afterTextChanged(s: Editable?) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val query = s.toString()
                viewModel.searchTransactions(query).observe(viewLifecycleOwner) { results ->
                    transactionAdapter.submitList(results)
                }
            }
        })

        setUpMonthSpinner()

        viewModel.filteredTransactions.observe(viewLifecycleOwner) { list ->
            transactionAdapter.submitList(list)
        }

    }

    private fun setUpMonthSpinner(){

        binding.monthSpinner.adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            months
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }

        val currentMonth = java.text.SimpleDateFormat("MMMM", java.util.Locale.getDefault()).format(java.util.Date())
        val monthIndex = months.indexOfFirst { it.equals(currentMonth, ignoreCase = true) }
        if (monthIndex != -1) {
            binding.monthSpinner.setSelection(monthIndex)
        }

        binding.monthSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                val selectedMonth = months[position]
                viewModel.setMonthFilter(selectedMonth)
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
