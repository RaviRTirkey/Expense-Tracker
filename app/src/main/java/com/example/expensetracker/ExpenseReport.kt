package com.example.expensetracker


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.lifecycle.ViewModelProvider
import com.example.expensetracker.databinding.FragmentExpenseReportBinding
import com.example.expensetracker.room.Transaction
import com.example.expensetracker.room.TransactionDatabase
import com.example.expensetracker.room.TransactionRepository
import com.example.expensetracker.viewModel.TransactionViewModel
import com.example.expensetracker.viewModel.TransactionViewModelFactory
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import java.util.Calendar


class ExpenseReport : Fragment() {

    private var _binding: FragmentExpenseReportBinding? = null
    private val binding get() = _binding!!
    private lateinit var transactionViewModel: TransactionViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentExpenseReportBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Setup ViewModel
        val database = TransactionDatabase.getInstance(requireContext())
        val repository = TransactionRepository(database.transactionDAO,FirebaseAuth.getInstance().currentUser?.uid.toString())
        val factory = TransactionViewModelFactory(repository)
        transactionViewModel = ViewModelProvider(this, factory).get(TransactionViewModel::class.java)

        //Select Year
        yearSelection()


        val currentMonth = java.text.SimpleDateFormat("MMMM", java.util.Locale.getDefault()).format(java.util.Date())
        // Set default selection before adding listener
        val monthIndex = transactionViewModel.getMonthIndex(currentMonth)
        if (monthIndex != -1) {
            binding.monthSpinner.setSelection(monthIndex)
        }

        // Spinner selection
        binding.monthSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                val selectedMonth = parent.getItemAtPosition(position).toString()
                transactionViewModel.setMonthFilter(selectedMonth)
            }
            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        // Observe LiveData
        transactionViewModel.filteredTransactions.observe(viewLifecycleOwner) { transactions ->
            report(transactions)
        }

    }

    private fun report(transactions: List<Transaction>) {
        val grouped = transactions.groupBy { it.category }
            .mapValues { entry -> entry.value.sumOf { it.amount }.toFloat() }
            .map { (category, total) -> CategoryTotal(category, total) }

        // Set total
        val total = grouped.sumOf { it.categoryTotalAmount.toDouble() }.toFloat()
        binding.totalAmountTV.text = "₹ %.2f".format(total)

        val adapter = CategoryTotalAdapter(grouped)
        binding.categoryRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.categoryRecyclerView.adapter = adapter
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
                transactionViewModel.setYearFilter(selectedYear.toInt())

            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        }

    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
