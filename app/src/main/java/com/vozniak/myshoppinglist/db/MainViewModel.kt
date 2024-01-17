package com.vozniak.myshoppinglist.db

import android.text.BoringLayout
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.vozniak.myshoppinglist.entities.LibraryItem
import com.vozniak.myshoppinglist.entities.NoteItem
import com.vozniak.myshoppinglist.entities.ShopListItem
import com.vozniak.myshoppinglist.entities.ShopListNameItem
import kotlinx.coroutines.launch

class MainViewModel(database: MainDataBase) : ViewModel() {
    val dao = database.getDao()

    fun insertNote(note: NoteItem) = viewModelScope.launch {
        dao.insertNote(note)
    }

    val allNotes: LiveData<List<NoteItem>> = dao.getAllNotes().asLiveData()

    fun updateNote(note: NoteItem) = viewModelScope.launch {
        dao.updateNote(note)
    }

    fun deleteNote(id: Int) = viewModelScope.launch {
        dao.deleteNote(id)
    }

    fun insertShopListName(listName: ShopListNameItem) = viewModelScope.launch {
        dao.insertShoppingListName(listName)
    }

    val allShopListNames: LiveData<List<ShopListNameItem>> =
        dao.getAllShoppingListNames().asLiveData()

    fun updateListName(shopListNameItem: ShopListNameItem) = viewModelScope.launch {
        dao.updateShopListName(shopListNameItem)
    }

    fun deleteShopListName(id: Int) = viewModelScope.launch {
        dao.deleteShopListName(id)
    }

    fun insertShopItem(shopListItem: ShopListItem) = viewModelScope.launch {
        dao.insertItem(shopListItem)
  /*      if (!isLibraryItemExist(shopListItem.name)) dao.insertLibraryItem(
            LibraryItem(
               null,
               shopListItem.name
            )
        )
*/
    }

    fun getAllItemsFromList(listId: Int): LiveData<List<ShopListItem>> {
        return dao.getAllShoppingListItems(listId).asLiveData()
    }

    fun updateListItem(item: ShopListItem) = viewModelScope.launch {
        dao.updateListItem(item)
    }

    fun deleteShopListItem(listId: Int) = viewModelScope.launch {
        dao.deleteShopListItemByListId(listId)
    }
/*
    private suspend fun isLibraryItemExist(name: String): Boolean {
        return dao.getAllLibraryItems(name).isNotEmpty()
    }

    val libraryItems = MutableLiveData<List<LibraryItem>>()

    fun getAllLibraryItems(name: String) = viewModelScope.launch {
        libraryItems.postValue(dao.getAllLibraryItems(name))
    }
*/




    class MainViewModelFactory(val database: MainDataBase) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return MainViewModel(database) as T
            }
            throw IllegalArgumentException("Unknown ViewModelClass")
        }
    }
}