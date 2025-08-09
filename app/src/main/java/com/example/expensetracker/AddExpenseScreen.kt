package com.example.expensetracker

import android.app.DatePickerDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.expensetracker.databinding.FragmentAddExpenseScreenBinding
import com.example.expensetracker.room.Transaction
import com.example.expensetracker.room.TransactionDatabase
import com.example.expensetracker.room.TransactionRepository
import com.example.expensetracker.viewModel.TransactionViewModel
import com.example.expensetracker.viewModel.TransactionViewModelFactory
import com.google.firebase.auth.FirebaseAuth
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import kotlin.jvm.java


class AddExpenseScreen : Fragment() {

    private var _binding: FragmentAddExpenseScreenBinding? = null
    val binding get() = _binding!!

    private val categories = listOf("Food", "Transportation", "Entertainment", "Shopping", "Utilities", "Other",
        "Health", "Education", "Gifts", "Rent", "Insurance", "Fuel", "General", "Investment",
        "Loan", "Subscription", "Personal Care")

    private val calendar = Calendar.getInstance()

    //initializing viewModel
    lateinit var transactionViewModel : TransactionViewModel



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentAddExpenseScreenBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val userId = FirebaseAuth.getInstance().currentUser?.uid
        if (userId == null) {
            findNavController().navigate(R.id.launchScreen)
            return
        }

        val database = TransactionDatabase.getInstance(requireContext())
        val repository = TransactionRepository(database.transactionDAO, userId)
        val factory = TransactionViewModelFactory(repository)
        transactionViewModel = ViewModelProvider(this, factory).get(TransactionViewModel::class.java)

        setUpCategoryDropdown()
        setUpDatePicker()
        setUpClickListners()

    }

    private fun setUpCategoryDropdown(){
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, categories)
        binding.categoryDropdown.setAdapter(adapter)
    }
    private fun setUpDatePicker(){
        val dateFormat = SimpleDateFormat("yyyy-MM-dd",Locale.getDefault())

        val dateSetListener = DatePickerDialog.OnDateSetListener{_,year, month, dayOfMonth ->
            calendar.set(year, month, dayOfMonth)
            binding.dateEditText.setText(dateFormat.format(calendar.time))
        }
        binding.dateEditText.setOnClickListener {
            DatePickerDialog(
                requireContext(),
                dateSetListener,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        }
    }
    private fun setUpClickListners(){
        binding.submitButton.setOnClickListener {
            submitTransaction()
        }
    }

    private fun submitTransaction() {
        val amountText = binding.amountEditText.text.toString().toDoubleOrNull() ?: 0.0
        val category = binding.categoryDropdown.text.toString()
        val date = binding.dateEditText.text.toString()
        val note = binding.noteEditText.text.toString()
        val title = binding.titleEditText.text.toString()
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        if (userId == null) {
            findNavController().navigate(R.id.launchScreen)
            return
        }

        if (amountText == 0.0 || category.isBlank() || date.isBlank() || title.isBlank()) {
            Toast.makeText(requireContext(), "Please fill all fields", Toast.LENGTH_SHORT).show()
            return
        }

        if (amountText < 0) {
            Toast.makeText(requireContext(), "Amount cannot be negative", Toast.LENGTH_SHORT).show()
            return
        }

        if(title.length > 14){
            Toast.makeText(requireContext(), "Title cannot be more than 14 characters", Toast.LENGTH_SHORT).show()
            return
        }

        if(note.isBlank()){
            binding.noteEditText.setText("No note")
        }

        val transaction = Transaction(
            id = 0,
            name = title,
            amount = amountText,
            category = category,
            date = date,
            note = note,
            userId = userId
        )

        transactionViewModel.insert(transaction)

        Toast.makeText(requireContext(),"Expense added successfully!", Toast.LENGTH_SHORT).show()

        clearFields()
    }


    fun clearFields(){
        binding.amountEditText.text.clear()
        binding.categoryDropdown.text.clear()
        binding.dateEditText.text?.clear()
        binding.noteEditText.text.clear()
        binding.titleEditText.text.clear()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}