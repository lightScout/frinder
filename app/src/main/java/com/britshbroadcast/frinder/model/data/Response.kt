package com.britshbroadcast.frinder.model.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Response (
    @PrimaryKey(autoGenerate = true) val responseId: Int,
    @ColumnInfo(name = "info") val info: String
){
    constructor(info: String): this(0, info)
}