package com.britshbroadcast.frinder.model.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Result::class], version = 1, exportSchema = false)
abstract class DataBase: RoomDatabase() {
    abstract val dao: DAO

    companion object{
        @Volatile
        private var INSTANCE: DataBase? = null
        fun getInstance(context: Context): DataBase{
            val tempInstance = INSTANCE
            if(tempInstance != null){
                return tempInstance
            }
            synchronized(this){
                var instance =
                    Room.databaseBuilder(
                        context.applicationContext,
                        DataBase::class.java,
                        "data_base"
                    ).fallbackToDestructiveMigration()
                        .build()
                    INSTANCE = instance

                return instance
            }
        }
    }

}