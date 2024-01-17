package com.vozniak.myshoppinglist.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "library")
data class LibraryItem(
    @PrimaryKey(autoGenerate = true)
    val id : Int?,
    @ColumnInfo(name = "name")
    val name: String,
    @ColumnInfo(name = "ItemType")
    val ItemType: Int = 1

)
