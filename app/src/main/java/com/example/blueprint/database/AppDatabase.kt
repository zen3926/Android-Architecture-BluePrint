package com.example.blueprint.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.blueprint.database.dao.DrinkDao
import com.example.blueprint.database.dao.FlowerDao
import com.example.blueprint.database.dao.UserDao
import com.example.blueprint.database.entity.DatabaseDrink
import com.example.blueprint.database.entity.DatabaseFlower
import com.example.blueprint.database.entity.DatabaseUser

@Database(
    exportSchema = true,
    entities = [
        DatabaseDrink::class,
        DatabaseFlower::class,
        DatabaseUser::class
    ],
    version = 2
)
@TypeConverters(Converter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun getDrinkDao(): DrinkDao
    abstract fun getFlowerDao(): FlowerDao
    abstract fun getUserDao(): UserDao

    companion object {
        @Volatile
        private var instance: AppDatabase? = null
        private const val DB_NAME = "blue.db"

        fun getInstance(context: Context): AppDatabase {
            return instance ?: synchronized(this) {
                instance ?: buildDatabase(context)
                    .also { instance = it }
            }
        }

        private fun buildDatabase(context: Context) = with(
            Room.databaseBuilder(context.applicationContext, AppDatabase::class.java, DB_NAME)
        ) {
            // Create initial database from asset folder
            createFromAsset("database/init.db")
            // add database migration between versions
            addMigrations(*getMigrations())
            fallbackToDestructiveMigration()
        }.build()

        private fun getMigrations(): Array<Migration> = arrayOf(
            getMigrationFrom1To2()
        )

        private fun getMigrationFrom1To2(): Migration = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("CREATE TABLE IF NOT EXISTS `user` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `userName` TEXT NOT NULL, `password` TEXT NOT NULL, `dob` INTEGER NOT NULL, `gender` INTEGER NOT NULL)")
            }
        }
    }
}
