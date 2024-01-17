package com.vozniak.myshoppinglist.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.vozniak.myshoppinglist.entities.LibraryItem
import com.vozniak.myshoppinglist.entities.NoteItem
import com.vozniak.myshoppinglist.entities.ShopListItem
import com.vozniak.myshoppinglist.entities.ShopListNameItem
import kotlinx.coroutines.flow.Flow

@Dao
interface Dao {
    @Insert
    suspend fun insertNote(note: NoteItem)

    @Query("SELECT * FROM note_list")
    fun getAllNotes(): Flow<List<NoteItem>>

    @Update
    suspend fun updateNote(note: NoteItem)

    @Query("DELETE FROM note_list WHERE id IS :id ")
    suspend fun deleteNote(id: Int)

    @Insert
    suspend fun insertShoppingListName(name: ShopListNameItem)

    @Query("SELECT * FROM shopping_list_names")
    fun getAllShoppingListNames(): Flow<List<ShopListNameItem>>

    @Update
    suspend fun updateShopListName(shopListName: ShopListNameItem)

    @Query("DELETE FROM shopping_list_names WHERE id IS :id ")
    suspend fun deleteShopListName(id: Int)

    @Insert
    suspend fun insertItem(shopListItem: ShopListItem)

    @Query("SELECT * FROM shop_list_item WHERE listId = :listId2")
    fun getAllShoppingListItems(listId2: Int): Flow<List<ShopListItem>>

    @Update
    suspend fun updateListItem(item: ShopListItem)

    @Query("DELETE FROM shop_list_item WHERE listId = :listId2")
    suspend fun deleteShopListItemByListId(listId2: Int)

//    @Query("SELECT * FROM library WHERE name = :name")
//      suspend fun getAllLibraryItems(name: String): List<LibraryItem>

  //  @Insert
 //   suspend fun insertLibraryItem(libraryItem: LibraryItem)

}