package com.example.budgettracker.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.room.Room
import com.example.budgettracker.R
//import androidx.lifecycle.ViewModelProvider
import com.example.budgettracker.databinding.FragmentAddTransactionBinding
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

        //budgetViewModel = ViewModelProvider(this).get(BudgetViewModel::class.java)

        labelLayout = binding.labelLayout
        amountLayout = binding.amountLayout

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.addTransactionBtn.setOnClickListener {
            val label = binding.labelInput.text.toString()
            val description = binding.descriptionInput.text.toString()
            val amount = binding.amountInput.text.toString().toDouble()

            binding.labelInput.addTextChangedListener {
                if(it!!.count() > 0)
                    labelLayout.error = null
            }

            binding.amountInput.addTextChangedListener {
                if(it!!.count() > 0)
                    amountLayout.error = null
            }

            if(label.isEmpty())
                labelLayout.error = "Please enter a valid label!!"

            else if(amount == null)
                amountLayout.error = "Please enter a valid amount!!"

            else {
                val budget = Budget(0, label, amount, description)
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
//        val budgetDatabase = Room.databaseBuilder(requireContext(),
//            BudgetDatabase::class.java,
//            "budgets").build()

        //GlobalScope.launch(Dispatchers.Main) {
            budgetViewModel.addBudgetItem(budget)
            //budgetDatabase.getBudgetDao().insertAll(budget)

            findNavController().navigate(R.id.AddTransactionFragmentToMainBudget)
        //}
    }
}