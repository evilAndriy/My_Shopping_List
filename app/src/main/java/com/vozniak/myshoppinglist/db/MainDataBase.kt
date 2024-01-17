package com.vozniak.myshoppinglist.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.vozniak.myshoppinglist.entities.LibraryItem
import com.vozniak.myshoppinglist.entities.NoteItem
import com.vozniak.myshoppinglist.entities.ShopListItem
import com.vozniak.myshoppinglist.entities.ShopListNameItem


@Database(
    entities = [LibraryItem::class, NoteItem::class, ShopListItem::class, ShopListNameItem::class],
    version = 1
)
abstract class MainDataBase : RoomDatabase() {
    abstract fun getDao(): Dao
    companion object {
        @Volatile
        private var INSTANCE: MainDataBase? = null
        fun getDataBase(context: Context): MainDataBase {
            return INSTANCE ?: synchronized(this@Companion) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    MainDataBase::class.java,
                    "shopping_list.db"
                ).build()
                return instance

            }
        }
    }
}