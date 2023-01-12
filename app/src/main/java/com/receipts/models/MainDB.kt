package com.receipts.models

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.receipts.utils.entities.Receipt

@Database(entities = [Receipt::class], version = 1)
abstract class MainDb : RoomDatabase() {
    abstract fun getDao(): Dao

    companion object {
        fun getDb(context: Context, nameDb: String): MainDb {
            return Room.databaseBuilder(
                context.applicationContext,
                MainDb::class.java,
                "$nameDb.db"
            )
                .build()
        }
    }
}