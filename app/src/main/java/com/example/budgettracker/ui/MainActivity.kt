package com.example.budgettracker.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.budgettracker.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    //private val binding: ActivityMainBinding get() = _binding!!

    //private val viewModel: BudgetViewModel = BudgetViewModel(application as Application)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        binding.setOnClickListener {
//            startActivity(Intent(this, AddTransactionActivity::class.java))
//
//            //Toast.makeText(this, "Testing", Toast.LENGTH_SHORT).show()
//        }
//
//        initRecyclerView()
    }

//    private fun initRecyclerView() {
//        val adapter = BudgetAdapter()
//        val recyclerView = binding.rvBudget
//        recyclerView.adapter = adapter
//        recyclerView.layoutManager = LinearLayoutManager(this)
//    }
}