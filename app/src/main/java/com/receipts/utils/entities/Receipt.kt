package com.receipts.utils.entities

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity(tableName = "firstTable")
@Parcelize
data class Receipt(
    @PrimaryKey(autoGenerate = true)
    var id: Long? = null,
    @ColumnInfo(name="name")
    var name: String,
    @ColumnInfo(name="date")
    var date: String,
    @ColumnInfo(name="tariff")
    var tariff: Tariff = Tariff()
) : Parcelable