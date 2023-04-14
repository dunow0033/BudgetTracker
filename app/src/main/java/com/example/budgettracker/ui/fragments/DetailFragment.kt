package com.example.budgettracker.ui.fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.budgettracker.R
import com.example.budgettracker.databinding.FragmentDetailBinding
//import kotlinx.android.synthetic.main.fragment_detailed.*
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

class DetailFragment : Fragment() {

    private var _binding: FragmentDetailBinding? = null
    private val binding: FragmentDetailBinding get() = _binding!!

    private val args by navArgs<DetailFragmentArgs>()

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
    private lateinit var updateBtn: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentDetailBinding.inflate(inflater, container, false)

        labelLayout = binding.labelLayout
        amountLayout = binding.amountLayout
        updateBtn = binding.updateBtn

        return binding.root
    }

    @DelicateCoroutinesApi
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.labelInput.setText(args.budget.label)
        binding.amountInput.setText(args.budget.amount.toString())
        binding.descriptionInput.setText(args.budget.description)

        binding.rootView.setOnClickListener {
            requireActivity().window.decorView.clearFocus()

            val imm = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(it.windowToken, 0)
        }

        binding.updateBtn.setOnClickListener {
            val label = binding.labelInput.text.toString()
            val description = binding.descriptionInput.text.toString()
            val amount = binding.amountInput.text.toString().toDoubleOrNull()

            binding.labelInput.addTextChangedListener {
                binding.updateBtn.visibility = View.VISIBLE
                if(it!!.isNotEmpty())
                    labelLayout.error = null
            }

            binding.amountInput.addTextChangedListener {
                updateBtn.visibility = View.VISIBLE
                if(it!!.isNotEmpty())
                    amountLayout.error = null
            }

            binding.descriptionInput.addTextChangedListener {
                binding.updateBtn.visibility = View.VISIBLE
            }

            if(label.isEmpty())
                labelLayout.error = "Please enter a valid label!!"

            else if(amount == null)
                amountLayout.error = "Please enter a valid amount!!"

            else {
                val budget = Budget(label = label, amount = amount, description = description)
                update(budget)
            }

//            val budgetDao = BudgetDatabase.getAppDatabase(getApplication())?.getBudgetDao()
//            budgetDao?.insertBudgetItem(Budget(label = label, amount = parseDouble(amount), description = description))
            // findNavController().navigate(R.id.AddTransactionFragmentToMainBudget)
        }

        binding.closeBtn.setOnClickListener {
            findNavController().navigate(R.id.DetailFragmentToMainBudget)
        }
    }

    @DelicateCoroutinesApi
    private fun update(budget: Budget) {
//        val budgetDatabase = Room.databaseBuilder(requireContext(),
//            BudgetDatabase::class.java,
//            "budgets").build()

        GlobalScope.launch(Dispatchers.Main) {
            budgetViewModel.updateBudgetItem(budget)

            findNavController().navigate(R.id.DetailFragmentToMainBudget)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}