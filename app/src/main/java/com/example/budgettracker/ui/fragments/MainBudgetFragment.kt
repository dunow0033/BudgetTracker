package com.example.budgettracker.ui.fragments

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.budgettracker.R
import com.example.budgettracker.adapter.BudgetAdapter
import com.example.budgettracker.databinding.FragmentMainBudgetBinding
import com.example.budgettracker.db.BudgetDatabase
//import com.example.budgettracker.db.BudgetDatabase
import com.example.budgettracker.model.Budget
import com.example.budgettracker.repository.BudgetRepository
import com.example.budgettracker.viewmodel.BudgetViewModel
import com.example.budgettracker.viewmodel.BudgetViewModelFactory
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class MainBudgetFragment : Fragment() {

    private var _binding: FragmentMainBudgetBinding? = null
    private val binding: FragmentMainBudgetBinding get() = _binding!!

    private lateinit var deletedBudget: Budget
    private lateinit var budgets : List<Budget>
    private lateinit var oldBudgets : List<Budget>
    private lateinit var budgetAdapter: BudgetAdapter
    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var budgetDatabase: BudgetDatabase

    private val budgetViewModel: BudgetViewModel by viewModels {
        BudgetViewModelFactory(
            requireActivity().application,
            BudgetRepository(BudgetDatabase(requireActivity()))
        )
    }

//    private val budgetAdapter: BudgetAdapter by lazy {
//        BudgetAdapter()
//    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainBudgetBinding.inflate(inflater, container, false)

        //budgetViewModel = ViewModelProvider(this).get(BudgetViewModel::class.java)

        budgets = arrayListOf()

        budgetDatabase = Room.databaseBuilder(requireContext(),
        BudgetDatabase::class.java,
        "budgets").build()

        return binding.root
    }

    @DelicateCoroutinesApi
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

       //budgets = budgetDatabase.getBudgetDao().getBudgetItems()

        budgetAdapter = BudgetAdapter()
        linearLayoutManager = LinearLayoutManager(context)

            // swipe to remove
            val itemTouchHelper = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
                override fun onMove(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    target: RecyclerView.ViewHolder
                ): Boolean {
                    return false
                }

                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    //deleteTransaction(budgets[viewHolder.bindingAdapterPosition])

                    val budget = budgetAdapter.differ.currentList[viewHolder.bindingAdapterPosition]
                    deleteTransaction(budget)
                    //budgetViewModel.deleteBudgetItem(budgetAdapter.differ.currentList[viewHolder.bindingAdapterPosition])
                }
            }

            val swipeHelper = ItemTouchHelper(itemTouchHelper)
            swipeHelper.attachToRecyclerView(binding.recyclerview)

            binding.addBtn.setOnClickListener {
                findNavController().navigate(R.id.mainBudgetToAddTransactionFragment)
            }

            budgetViewModel.budgetItems.observe(viewLifecycleOwner) {
                budgetAdapter.differ.submitList(it)
            }

        binding.recyclerview.apply {
            adapter = budgetAdapter
            layoutManager = linearLayoutManager
        }

        //fetchAll()
    }

    @DelicateCoroutinesApi
    private fun fetchAll() {
        GlobalScope.launch {

            //budgets = budgetDatabase.getBudgetDao().getBudgetItems()

            budgets = budgetViewModel.getBudgetItems()

            //budgets = budgetAdapter.differ.currentList

            requireActivity().runOnUiThread{
//                budgetAdapter.setData(budgets)
                updateDashboard()
            }
        }
    }

    private fun updateDashboard() {
        val totalAmount = budgets.map { it.amount }.sum()
        val budgetAmount = budgets.filter { it.amount!! > 0 }.map{ it.amount }.sum()
        val expenseAmount = totalAmount - budgetAmount

        binding.balance.text = "$ %.2f".format(totalAmount)
        binding.budget.text = "$ %.2f".format(budgetAmount)
        binding.expense.text = "$ %.2f".format(expenseAmount)
    }

    @DelicateCoroutinesApi
    private fun undoDelete() {
        GlobalScope.launch {
            //budgetDatabase.getBudgetDao().insertAll(deletedBudget)
            budgetViewModel.addBudgetItem(deletedBudget)

            budgets = oldBudgets

            requireActivity().runOnUiThread{
                //budgetAdapter.setData(budgets)
                updateDashboard()
            }
        }
    }

    @DelicateCoroutinesApi
    private fun showSnackBar() {
        val view = binding.coordinator

        val snackbar = Snackbar.make(view, "Transaction deleted!!", Snackbar.LENGTH_LONG)
        snackbar.setAction("Undo") {
            undoDelete()
        }
            .setActionTextColor(ContextCompat.getColor(requireContext(), R.color.red))
            .setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
            .show()
    }

    @DelicateCoroutinesApi
    private fun deleteTransaction(budget: Budget) {
        deletedBudget = budget
        oldBudgets = budgetAdapter.differ.currentList

        GlobalScope.launch {
            budgetViewModel.deleteBudgetItem(budget)

            budgets = budgets.filter { it.id != budget.id }

            requireActivity().runOnUiThread{
                updateDashboard()
//                budgetAdapter.setData(budgets)
                showSnackBar()
            }
        }
    }

    @DelicateCoroutinesApi
    override fun onResume() {
        super.onResume()
        fetchAll()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}