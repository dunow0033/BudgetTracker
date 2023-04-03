package com.example.budgettracker.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.budgettracker.model.Budget
import kotlinx.coroutines.flow.Flow

@Dao
interface BudgetDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertBudgetItem(budget: Budget)

    @Insert
    fun insertAll(vararg budget: Budget)

    @Query("SELECT * FROM budget_items ORDER BY id ASC")
    fun getBudgetItems(): List<Budget>

    @Query("SELECT * FROM budget_items")
    fun readAllData(): Flow<List<Budget>>

    @Delete
    fun deleteBudget(budget: Budget)

    @Update
    fun updateBudget(budget: Budget)
}