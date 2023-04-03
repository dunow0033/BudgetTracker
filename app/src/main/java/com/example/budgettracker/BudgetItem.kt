package com.example.budgettracker

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.budgettracker.R
import com.example.budgettracker.databinding.FragmentBudgetItemBinding

class BudgetItem : Fragment() {

    private var _binding: FragmentBudgetItemBinding? = null
    private val binding: FragmentBudgetItemBinding get() = _binding!!

    //private lateinit var todoViewModel: TodoViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentBudgetItemBinding.inflate(inflater, container, false)

        //todoViewModel = ViewModelProvider(this).get(TodoViewModel::class.java)

        binding.label.setText("")
        binding.amount.setText("")
        binding.description.setText("")

//        binding.submitBtn.setOnClickListener {
//            insertDataToDatabase()
//        }
//
//        binding.seeList.setOnClickListener {
//            findNavController().navigate(R.id.TodoFormToTodoList)
//        }

        return binding.root
    }

}