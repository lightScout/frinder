package com.britshbroadcast.frinder.model.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.britshbroadcast.frinder.model.data.Response

@Dao
interface DAO {
    @Insert
    fun insertResponseItem(vararg response: Response)

    @Query("SELECT * from response WHERE responseId LIKE :responseId")
    fun getResponseByID(responseId: Int): Response?

    @Query("SELECT * FROM response")
    fun getAllResponses(): List<Response>
}