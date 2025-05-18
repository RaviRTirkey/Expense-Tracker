package com.example.expensetracker

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.expensetracker.databinding.RecyclerviewItemBinding
import com.example.expensetracker.room.Transaction

class TransactionAdapter: ListAdapter<Transaction, TransactionAdapter.TransactionViewHolder>(
    DiffCallback()
) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): TransactionViewHolder {
        val binding = RecyclerviewItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TransactionViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: TransactionViewHolder,
        position: Int
    ) {
        val transaction = getItem(position)

        holder.binding.itemName.text = transaction.name
        holder.binding.noteText.text = transaction.note
        holder.binding.dateAndCost.text = "${transaction.date} | ₹${transaction.amount}"
        holder.binding.textCategory.text = transaction.category

        // ** To HomeScreenDirections to work you have to enable safeargs in build.gradle and also database object should be parcelabe
        holder.itemView.setOnClickListener {
            val action = HomeScreenDirections.actionHomeScreenToEditExpenseScreen(transaction)
            holder.itemView.findNavController().navigate(action)
        }

        when (transaction.category) {
            "Food" -> holder.binding.categoryImage.setImageResource(R.drawable.ic_food)
            "Transportation" -> holder.binding.categoryImage.setImageResource(R.drawable.ic_transportation)
            "Entertainment" -> holder.binding.categoryImage.setImageResource(R.drawable.ic_entertainment)
            "Shopping" -> holder.binding.categoryImage.setImageResource(R.drawable.ic_shopping)
            "Utilities" -> holder.binding.categoryImage.setImageResource(R.drawable.ic_utilities)
            "Health" -> holder.binding.categoryImage.setImageResource(R.drawable.ic_health)
            "Education" -> holder.binding.categoryImage.setImageResource(R.drawable.ic_education)
//            "Other" -> holder.binding.categoryImage.setImageResource(R.drawable.ic_other)
            "Gifts" -> holder.binding.categoryImage.setImageResource(R.drawable.ic_gift)
//            "Rent" -> holder.binding.categoryImage.setImageResource(R.drawable.ic_rent)
//            "Insurance" -> holder.binding.categoryImage.setImageResource(R.drawable.ic_insurance)
            "Fuel" -> holder.binding.categoryImage.setImageResource(R.drawable.ic_fuel)
//            "General" -> holder.binding.categoryImage.setImageResource(R.drawable.ic_general)
            "Investment" -> holder.binding.categoryImage.setImageResource(R.drawable.ic_investment)
            "Loan" -> holder.binding.categoryImage.setImageResource(R.drawable.ic_loan)
            "Subscription" -> holder.binding.categoryImage.setImageResource(R.drawable.ic_subscription)
            "Personal Care" -> holder.binding.categoryImage.setImageResource(R.drawable.ic_personal_care)
            "Other" -> holder.binding.categoryImage.setImageResource(R.drawable.home1_icon_blue)
            "Rent" -> holder.binding.categoryImage.setImageResource(R.drawable.home1_icon_blue)
            "Insurance" -> holder.binding.categoryImage.setImageResource(R.drawable.home1_icon_blue)
            "General" -> holder.binding.categoryImage.setImageResource(R.drawable.home1_icon_blue)
            "Salary" -> holder.binding.categoryImage.setImageResource(R.drawable.home1_icon_blue)
            "Fees" -> holder.binding.categoryImage.setImageResource(R.drawable.home1_icon_blue)
        }


    }

    class TransactionViewHolder(val binding: RecyclerviewItemBinding): RecyclerView.ViewHolder(binding.root)

    class DiffCallback : DiffUtil.ItemCallback<Transaction>() {
        override fun areItemsTheSame(oldItem: Transaction, newItem: Transaction) = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: Transaction, newItem: Transaction) = oldItem == newItem
    }
}