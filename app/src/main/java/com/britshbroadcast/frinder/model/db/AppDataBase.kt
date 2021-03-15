package com.britshbroadcast.frinder.model.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.britshbroadcast.frinder.model.data.Response
import com.britshbroadcast.frinder.model.data.Result

@Database(entities = [Response::class], version = 1, exportSchema = false)
abstract class AppDataBase: RoomDatabase() {
    abstract fun responseDao(): DAO

//    companion object{
//        @Volatile
//        private var INSTANCE: AppDataBase? = null
//        fun getInstance(context: Context): AppDataBase{
//            val tempInstance = INSTANCE
//            if(tempInstance != null){
//                return tempInstance
//            }
//            synchronized(this){
//                var instance =
//                    Room.databaseBuilder(
//                        context.applicationContext,
//                        AppDataBase::class.java,
//                        "data_base"
//                    ).fallbackToDestructiveMigration()
//                        .build()
//                    INSTANCE = instance
//
//                return instance
//            }
//        }
//    }

}