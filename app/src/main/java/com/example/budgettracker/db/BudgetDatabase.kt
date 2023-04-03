package com.example.budgettracker.db

import android.content.Context
import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.budgettracker.model.Budget
import com.example.budgettracker.utils.DB_NAME




@Database(entities = [Budget::class], version = 1, exportSchema = false)
abstract class BudgetDatabase : RoomDatabase() {

    abstract fun getBudgetDao(): BudgetDao

    companion object {
        @Volatile
        private var instance: BudgetDatabase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            instance ?: createDatabase(context).also { instance = it }
        }

        fun createDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                BudgetDatabase::class.java,
                DB_NAME
            ).build()
    }
}



//@Database(
//    entities = [Budget::class],
//    version = 1,
//    autoMigrations = [
//        AutoMigration (from = 1, to = 2)
//    ]
//)
//abstract class BudgetDatabase : RoomDatabase() {
//
//    abstract fun getBudgetDao() : BudgetDao
//
//    companion object {
//        @Volatile
//        private var instance: BudgetDatabase? = null
//        private val LOCK = Any()
//
//        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
//            instance ?: createDatabase(context).also { instance = it }
//        }
//
////        val migration_1_2: Migration = object: Migration(1, 2) {
////            override fun migrate(database: SupportSQLiteDatabase) {
////                database.execSQL("ALTER TABLE userinfo ADD COLUMN phone TEXT DEFAULT ''")
////            }
////        }
//
//        fun createDatabase(context: Context) =
//            Room.databaseBuilder(
//                context.applicationContext,
//                BudgetDatabase::class.java,
//                DB_NAME
//            ).build()
//
//
////        fun getAppDatabase(context: Context): BudgetDatabase? {
////
////            if(instance == null ) {
////
////                instance = Room.databaseBuilder<BudgetDatabase>(
////                    context.applicationContext, BudgetDatabase::class.java, "AppDBB"
////                )
////                    //.addMigrations(migration_1_2)
////                    .allowMainThreadQueries()
////                    .build()
////
////            }
////            return instance
////        }
//    }
//}