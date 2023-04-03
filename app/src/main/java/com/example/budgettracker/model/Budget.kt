package com.example.budgettracker.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

//@Parcelize
//@Entity(tableName = "budget_items")
//data class Budget (
//    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id") val id: Int? = null,
//    @ColumnInfo(name = "label") var label: String?,
//    @ColumnInfo(name = "amount") var amount: Double?,
//    @ColumnInfo(name = "description") var description: String?
// ) : Parcelable


@Parcelize
@Entity(tableName = "budget_items")
data class Budget (
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id") val id: Int? = null,
    val label: String,
    val amount: Double,
    val description: String
    ) : Parcelable