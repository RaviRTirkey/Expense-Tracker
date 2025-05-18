package com.example.expensetracker

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.expensetracker.databinding.FragmentEditExpenseScreenBinding
import com.example.expensetracker.room.Transaction
import com.example.expensetracker.room.TransactionDatabase
import com.example.expensetracker.room.TransactionRepository
import com.example.expensetracker.viewModel.TransactionViewModel
import com.example.expensetracker.viewModel.TransactionViewModelFactory
import com.google.firebase.auth.FirebaseAuth


class editExpenseScreen : Fragment() {

    private val args: editExpenseScreenArgs by navArgs()
    private var _binding: FragmentEditExpenseScreenBinding? = null
    private val binding get() = _binding!!
    lateinit var transactionViewModel : TransactionViewModel
    private var id: Int = 0

    private val categories = listOf("Food", "Transportation", "Entertainment", "Shopping", "Utilities", "Other",
        "Health", "Education", "Gifts", "Rent", "Insurance", "Fuel", "General", "Investment",
        "Loan", "Subscription", "Personal Care")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentEditExpenseScreenBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            titleEditText.setText(args.transaction.name)
            amountEditText.setText(args.transaction.amount.toString())
            categoryDropdown.setText(args.transaction.category, false)
            dateEditText.setText(args.transaction.date)
            noteEditText.setText(args.transaction.note)
        }

        // Setting up database, repository, ViewModelFactory, and ViewModel
        val database = TransactionDatabase.getInstance(requireContext())
        val repository = TransactionRepository(database.transactionDAO,FirebaseAuth.getInstance().currentUser?.uid.toString())
        val factory = TransactionViewModelFactory(repository)
        transactionViewModel = ViewModelProvider(this, factory).get(TransactionViewModel::class.java)

        setupDropdown()

        setUpClickListners()
    }

    private fun setUpClickListners(){
        //Save Button
        binding.submitButton.setOnClickListener {
            submitTransaction()
        }

        binding.deleteButton.setOnClickListener {
            transactionViewModel.delete(args.transaction)
            Toast.makeText(requireContext(), "Expense deleted successfully!", Toast.LENGTH_SHORT).show()
            findNavController().navigateUp()
        }
    }

    private fun submitTransaction() {
        id = args.transaction.id
        val amountText = binding.amountEditText.text.toString().toDoubleOrNull() ?: 0.0
        val category = binding.categoryDropdown.text.toString()
        val date = binding.dateEditText.text.toString()
        val note = binding.noteEditText.text.toString()
        val title = binding.titleEditText.text.toString()

        if (amountText == 0.0 || category.isBlank() || date.isBlank() || title.isBlank()) {
            Toast.makeText(requireContext(), "Please fill all fields", Toast.LENGTH_SHORT).show()
            return
        }

        if (amountText < 0) {
            Toast.makeText(requireContext(), "Amount cannot be negative", Toast.LENGTH_SHORT).show()
            return
        }

        if(title.length > 12){
            Toast.makeText(requireContext(), "Title cannot be more than 12 characters", Toast.LENGTH_SHORT).show()
            return
        }

        if(note.isBlank()){
            binding.noteEditText.setText("No note")
        }

        val transaction = Transaction(
            id = id,
            name = title,
            amount = amountText,
            category = category,
            date = date,
            note = note,
            userId = args.transaction.userId
        )

        transactionViewModel.update(transaction)

        Toast.makeText(requireContext(),"Expense updated successfully!", Toast.LENGTH_SHORT).show()

        closeFragment()
    }

    private fun setupDropdown()
    {
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, categories)
        binding.categoryDropdown.setAdapter(adapter)
    }

    fun closeFragment()
    {
        findNavController().navigateUp()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}