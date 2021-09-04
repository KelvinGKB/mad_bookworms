package com.mad.mad_bookworms.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = arrayOf(MyCartTable::class,User_Table::class),version = 4, exportSchema = false)
abstract class LocalDB : RoomDatabase() {

    abstract val LocalDao :  LocalDao
    abstract val MyCartDao: MyCartDao

    companion object {

        @Volatile
        private var INSTANCE: LocalDB? = null

        fun getInstance(context: Context): LocalDB {
            synchronized(this) {
                var instance = INSTANCE

                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        LocalDB::class.java,
                        "LocalDatabase"

                    )
                        .fallbackToDestructiveMigration()
                        .build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }

}
