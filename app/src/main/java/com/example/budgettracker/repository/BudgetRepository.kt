package com.example.budgettracker.repository

import androidx.lifecycle.LiveData
import com.example.budgettracker.db.BudgetDao
import com.example.budgettracker.db.BudgetDatabase
import com.example.budgettracker.model.Budget
import kotlinx.coroutines.flow.Flow

class BudgetRepository(private val db: BudgetDatabase) {

        val readAllData: Flow<List<Budget>> = db.getBudgetDao().readAllData()

        suspend fun addBudgetItem(budget: Budget) {
            db.getBudgetDao().insertBudgetItem(budget)
        }

//    fun getBudgetItems(): List<Budget> {
//        return db.getBudgetDao().readAllData()
//    }

    suspend fun deleteBudgetItem(budget: Budget) {
        db.getBudgetDao().deleteBudget(budget)
    }

    suspend fun updateBudgetItem(budget: Budget) {
        db.getBudgetDao().updateBudget(budget)
    }
}



//    suspend fun addBudgetItem(budget: Budget) {
//        db.getBudgetDao().insertBudgetItem(budget)
//    }
//
//    fun getBudgetItems(): List<Budget> {
//        return db.getBudgetDao().getBudgetItems()
//    }
//
//    suspend fun deleteBudgetItem(budget: Budget) {
//        db.getBudgetDao().deleteBudget(budget)
//    }
//
//    suspend fun updateBudgetItem(budget: Budget) {
//        db.getBudgetDao().updateBudget(budget)
//    }
