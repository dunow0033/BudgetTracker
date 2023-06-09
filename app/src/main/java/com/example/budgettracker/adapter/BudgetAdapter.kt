package com.example.budgettracker.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment.Companion.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.budgettracker.R
import com.example.budgettracker.databinding.BudgetListItemBinding
import com.example.budgettracker.model.Budget
import com.example.budgettracker.ui.fragments.DetailFragment
import com.example.budgettracker.ui.fragments.MainBudgetFragment
import com.example.budgettracker.ui.fragments.MainBudgetFragmentDirections

//class BudgetAdapter(private var budgets: List<Budget>) : RecyclerView.Adapter<BudgetAdapter.BudgetViewHolder>() {
class BudgetAdapter() : RecyclerView.Adapter<BudgetAdapter.BudgetViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BudgetViewHolder {
        return BudgetViewHolder(
            BudgetListItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: BudgetViewHolder, position: Int) {
        holder.bind(differ.currentList[holder.bindingAdapterPosition])

        val transaction = differ.currentList[holder.bindingAdapterPosition]
        val context = holder.binding.amount.context

        if(transaction.amount!! >= 0){
            holder.binding.amount.text = "+ $%.2f".format(transaction.amount)
            holder.binding.amount.setTextColor(ContextCompat.getColor(context, R.color.green))
        } else {
            holder.binding.amount.text = "- $%.2f".format(Math.abs(transaction.amount!!))
            holder.binding.amount.setTextColor(ContextCompat.getColor(context, R.color.red))
        }

        holder.binding.label.text = transaction.label

//        holder.itemView.setOnClickListener {
//            findNavController().navigate(R.id.mainBudgetToAddTransactionActivity)
//
//            context.startActivity(intent)
//        }

        holder.itemView.setOnClickListener {
            val action = MainBudgetFragmentDirections.mainBudgetToDetailFragment(transaction)
            holder.itemView.findNavController().navigate(action)
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    private val differCallback = object : DiffUtil.ItemCallback<Budget>() {
        override fun areItemsTheSame(oldItem: Budget, newItem: Budget): Boolean {
            return oldItem.label == newItem.label
        }

        override fun areContentsTheSame(oldItem: Budget, newItem: Budget): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)

//    class BudgetViewHolder(val binding: BudgetListItemBinding) : RecyclerView.ViewHolder(binding.root) {
//        val label = binding.label
//        val amount = binding.amount
//    }

    class BudgetViewHolder(val binding: BudgetListItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(budget: Budget) {
            binding.label.text = budget.label
            binding.amount.text = budget.amount.toString()
        }
    }

//    fun setData(budgets: List<Budget>){
//        this.budgets = budgets
//        notifyDataSetChanged()
//    }

}