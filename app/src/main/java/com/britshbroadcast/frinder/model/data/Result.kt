package com.britshbroadcast.frinder.model.data

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "result_table")
data class Result(

    @PrimaryKey()
    @ColumnInfo(name = "place_id")
    val place_id: String,
    @ColumnInfo(name = "business_status")
    val business_status: String,
    @ColumnInfo(name = "geometry")
    val geometry: Geometry,
    @ColumnInfo(name = "icon")
    val icon: String,
    @ColumnInfo(name = "name")
    val name: String,
    @ColumnInfo(name = "opening_hours")
    val opening_hours: OpeningHours,
    @ColumnInfo(name = "photos")
    val photos: List<Photo>,
    @ColumnInfo(name = "plus_code")
    val plus_code: PlusCode,
    @ColumnInfo(name = "price_level")
    val price_level: Int,
    @ColumnInfo(name = "rating")
    val rating: Double,
    @ColumnInfo(name = "reference")
    val reference: String,
    @ColumnInfo(name = "scope")
    val scope: String,
    @ColumnInfo(name = "types")
    val types: List<String>,
    @ColumnInfo(name = "user_ratings_total")
    val user_ratings_total: Int,
    @ColumnInfo(name = "vicinity")
    val vicinity: String
) : Parcelable