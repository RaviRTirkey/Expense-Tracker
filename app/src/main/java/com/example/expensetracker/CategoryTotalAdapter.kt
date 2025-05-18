package com.example.expensetracker
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.expensetracker.databinding.ReportItemBinding
import com.example.expensetracker.databinding.FragmentExpenseReportBinding

data class CategoryTotal(
    val category: String,
    val categoryTotalAmount: Float
)

class CategoryTotalAdapter(private val items: List<CategoryTotal>) :
    RecyclerView.Adapter<CategoryTotalAdapter.CategoryTotalViewHolder>() {

    inner class CategoryTotalViewHolder(private val binding: ReportItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: CategoryTotal) {
            binding.categoryTextView.text = item.category
            binding.amountTextView.text = "₹ %.2f".format(item.categoryTotalAmount)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryTotalViewHolder {
        val binding = ReportItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CategoryTotalViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CategoryTotalViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size
}
