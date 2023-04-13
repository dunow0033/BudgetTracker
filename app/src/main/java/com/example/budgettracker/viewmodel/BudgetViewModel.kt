package com.example.budgettracker.viewmodel

import android.app.Application
import androidx.lifecycle.*
import com.example.budgettracker.db.BudgetDatabase
import com.example.budgettracker.model.Budget
import com.example.budgettracker.repository.BudgetRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class BudgetViewModel(app: Application, val repo: BudgetRepository) : AndroidViewModel(app){

    private var _budgetItems: MutableLiveData<List<Budget>> = MutableLiveData()
    var budgetItems: LiveData<List<Budget>> = _budgetItems

    init {
        //val budgetDao = BudgetDatabase.createDatabase(app).getBudgetDao()
       // budgetItems = repo.readAllData
        getAllBudgetItems()
    }

    private fun getAllBudgetItems() {
        viewModelScope.launch {
            repo.readAllData.collect {
                _budgetItems.postValue(it)
            }
        }
    }

    fun getBudgetItems(): List<Budget> {
        return repo.getBudgetItems()
    }

//    fun getAllBudgetItems() {
//        val budgetDao = BudgetDatabase.getAppDatabase(getApplication())?.getBudgetDao()
//        val list = budgetDao?.getBudgetItems()
//
//        _budgetItems.postValue(list)
//    }

    fun addBudgetItem(budget: Budget) {
        viewModelScope.launch(Dispatchers.IO) {
            repo.addBudgetItem(budget)
        }
    }

//    fun addBudgetItem(budget: Budget){
//        val budgetDao = BudgetDatabase.getAppDatabase(getApplication())?.getBudgetDao()
//        viewModelScope.launch {
//            budgetDao?.insertBudgetItem(budget)
//        }
//        getAllBudgetItems()
//    }

    fun deleteBudgetItem(budget: Budget) {
        viewModelScope.launch(Dispatchers.IO) {
            repo.deleteBudgetItem(budget)
        }
    }
//
    fun updateBudgetItem(budget: Budget) {
        viewModelScope.launch(Dispatchers.IO) {
            repo.updateBudgetItem(budget)
        }
    }

}

class BudgetViewModelFactory(
    private val app: Application,
    private val repo: BudgetRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(BudgetViewModel::class.java)) {
            return BudgetViewModel(app, repo) as T
        } else {
            throw IllegalArgumentException("Instance of BudgetViewModel cannot be created!!")
        }
    }
}