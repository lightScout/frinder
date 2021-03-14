package com.britshbroadcast.frinder.model.db

import androidx.lifecycle.MutableLiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.britshbroadcast.frinder.model.data.Result

@Dao
interface DAO {
    @Insert
    fun insertResultItem(vararg result: Result)

    @Query("SELECT * from result_table WHERE place_id = :placeID")
    fun getResultItemByID(placeID: String): Result?

    @Query("SELECT * FROM result_table")
    fun getAllResults(): MutableLiveData<List<Result>>
}