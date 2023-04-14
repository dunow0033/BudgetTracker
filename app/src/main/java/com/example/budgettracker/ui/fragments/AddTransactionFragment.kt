package com.example.budgettracker.ui.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.room.Room
import com.example.budgettracker.R
import com.example.budgettracker.adapter.BudgetAdapter
//import androidx.lifecycle.ViewModelProvider
import com.example.budgettracker.databinding.FragmentAddTransactionBinding
import com.example.budgettracker.databinding.FragmentMainBudgetBinding
import com.example.budgettracker.db.BudgetDatabase
import com.example.budgettracker.model.Budget
import com.example.budgettracker.repository.BudgetRepository
import com.example.budgettracker.viewmodel.BudgetViewModel
import com.example.budgettracker.viewmodel.BudgetViewModelFactory
import com.google.android.material.textfield.TextInputLayout
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class AddTransactionFragment : Fragment() {

    private var _binding: FragmentAddTransactionBinding? = null
    private val binding: FragmentAddTransactionBinding get() = _binding!!

    private lateinit var budgets : List<Budget>

    private lateinit var budgetAdapter : BudgetAdapter

//    private var label = binding.labelInput.text.toString()
//    private var amount = binding.amountInput.text.toString().toDoubleOrNull()
//    private var description = binding.descriptionInput.text.toString()

    private val budgetViewModel: BudgetViewModel by viewModels {
        BudgetViewModelFactory(
            requireActivity().application,
            BudgetRepository(BudgetDatabase(requireActivity()))
        )
    }

    private lateinit var labelLayout: TextInputLayout
    private lateinit var amountLayout: TextInputLayout

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentAddTransactionBinding.inflate(inflater, container, false)

        budgets = arrayListOf()

        labelLayout = binding.labelLayout
        amountLayout = binding.amountLayout

        return binding.root
    }

    @DelicateCoroutinesApi
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rootView.setOnClickListener {
            requireActivity().window.decorView.clearFocus()

            val imm = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(it.windowToken, 0)
        }

        budgetAdapter = BudgetAdapter()

        binding.addTransactionBtn.setOnClickListener {
            val label = binding.labelInput.text.toString()
            val description = binding.descriptionInput.text.toString()
            val amount = binding.amountInput.text.toString().toDoubleOrNull()

            binding.labelInput.addTextChangedListener {
                if(it!!.isNotEmpty())
                    labelLayout.error = null
            }

            binding.amountInput.addTextChangedListener {
                if(it!!.isNotEmpty())
                    amountLayout.error = null
            }

            if(label.isEmpty())
                labelLayout.error = "Please enter a valid label!!"

            else if(amount == null)
                amountLayout.error = "Please enter a valid amount!!"

            else {
                val budget = Budget(label = label, amount = amount, description = description)
                insert(budget)
            }



//            val budgetDao = BudgetDatabase.getAppDatabase(getApplication())?.getBudgetDao()
//            budgetDao?.insertBudgetItem(Budget(label = label, amount = parseDouble(amount), description = description))
        // findNavController().navigate(R.id.AddTransactionFragmentToMainBudget)
        }

        binding.closeBtn.setOnClickListener {
            findNavController().navigate(R.id.AddTransactionFragmentToMainBudget)
        }
    }

    @DelicateCoroutinesApi
    private fun insert(budget: Budget) {

        GlobalScope.launch(Dispatchers.Main) {
            budgetViewModel.addBudgetItem(budget)

            findNavController().navigate(R.id.AddTransactionFragmentToMainBudget)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}