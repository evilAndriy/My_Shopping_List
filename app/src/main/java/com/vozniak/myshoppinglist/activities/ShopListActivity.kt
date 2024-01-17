package com.vozniak.myshoppinglist.activities

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import androidx.activity.OnBackPressedCallback
import androidx.activity.OnBackPressedDispatcher
import androidx.activity.viewModels
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.vozniak.myshoppinglist.R
import com.vozniak.myshoppinglist.databinding.ActivityShopListBinding
import com.vozniak.myshoppinglist.db.MainViewModel
import com.vozniak.myshoppinglist.db.ShopListItemAdapter
import com.vozniak.myshoppinglist.dialogs.EditListDialog
import com.vozniak.myshoppinglist.entities.ShopListItem
import com.vozniak.myshoppinglist.entities.ShopListNameItem
import com.vozniak.myshoppinglist.utils.ShareHelper

class ShopListActivity : AppCompatActivity(), ShopListItemAdapter.Listener {
    lateinit var binding: ActivityShopListBinding
    private var shopListNameItem: ShopListNameItem? = null
    private lateinit var saveItem: MenuItem
    private var edItem: EditText? = null
    private var adapter: ShopListItemAdapter? = null
    private lateinit var textWatcher: TextWatcher
    private lateinit var defPref : SharedPreferences
    val mainViewModel: MainViewModel by viewModels {
        MainViewModel.MainViewModelFactory((applicationContext as MainApp).dataBase)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityShopListBinding.inflate(layoutInflater)
        defPref = PreferenceManager.getDefaultSharedPreferences(this)
        setTheme(getSelectedTheme())
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        init()
        initRcView()
        listItemObserver()
        onBackPressedDispatcher.addCallback(this@ShopListActivity, onBackPressedCallback)
    }

    private fun init() {
        shopListNameItem = intent.getSerializableExtra(SHOP_LIST_NAME, ShopListNameItem::class.java)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.shop_list_menu, menu)
        saveItem = menu?.findItem(R.id.save_item) as MenuItem
        saveItem.isVisible = false
        val newItem = menu.findItem(R.id.new_item) as MenuItem
        edItem = newItem.actionView?.findViewById(R.id.edNewShopItem) as EditText
        newItem.setOnActionExpandListener(expandActionView())
        textWatcher = textWatcher()
        return true

    }

    private fun textWatcher(): TextWatcher {
        return object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
    //            mainViewModel.getAllLibraryItems("%$p0%")
            }

            override fun afterTextChanged(p0: Editable?) {

            }

        }
    }

    private fun addNewShopItem() {
        if (edItem?.text.toString().isEmpty()) return
        val item = ShopListItem(
            null,
            edItem?.text.toString(),
            "",
            false,
            shopListNameItem?.id as Int,
            0
        )
        edItem?.setText("")
        mainViewModel.insertShopItem(item)
    }

    private fun listItemObserver() {
        mainViewModel.getAllItemsFromList(shopListNameItem?.id as Int)
            .observe(this@ShopListActivity, {
                adapter?.submitList(it)
                binding.tvEmpty.visibility = if (it.isEmpty()) {
                    View.VISIBLE
                } else {
                    View.GONE
                }
            })
    }

/*     private fun libraryItemObserver() {
        mainViewModel.libraryItems.observe(this, {
            val tempShopList = ArrayList<ShopListItem>()
            it.forEach() { item ->
                val shopItem = ShopListItem(
                    item.id,
                    item.name,
                    "",
                    false,
                    0,
                    1
                )
                tempShopList.add(shopItem)
            }
            adapter?.submitList(tempShopList)
        })
    }
*/
    private fun initRcView() = with(binding) {
        adapter = ShopListItemAdapter(this@ShopListActivity)
        rcView.layoutManager = LinearLayoutManager(this@ShopListActivity)
        rcView.adapter = adapter

    }


    private fun expandActionView(): MenuItem.OnActionExpandListener {
        return object : MenuItem.OnActionExpandListener {
            override fun onMenuItemActionExpand(p0: MenuItem): Boolean {
                saveItem.isVisible = true
                edItem?.addTextChangedListener(textWatcher)
  //              libraryItemObserver()
       //         mainViewModel.getAllItemsFromList(shopListNameItem?.id as Int).removeObservers(this@ShopListActivity)
    //            mainViewModel.getAllLibraryItems("%%")
                return true
            }

            override fun onMenuItemActionCollapse(p0: MenuItem): Boolean {
                saveItem.isVisible = false
                edItem?.removeTextChangedListener(textWatcher)
                invalidateOptionsMenu()
      //          mainViewModel.libraryItems.removeObservers(this@ShopListActivity)
                edItem?.setText("")
        //        libraryItemObserver()
                return true
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
           android.R.id.home -> {
               saveItemCount()
               finish()
               return true
           }
            R.id.save_item -> addNewShopItem()
            R.id.delete_list -> {
                mainViewModel.deleteShopListItem(shopListNameItem?.id as Int)
                mainViewModel.deleteShopListName(shopListNameItem?.id as Int)
                finish()
            }
            R.id.clear_list -> mainViewModel.deleteShopListItem(shopListNameItem?.id as Int)
            R.id.share_list -> startActivity(
                Intent.createChooser(
                    ShareHelper.shareShopList(
                        adapter?.currentList as List<ShopListItem>,
                        shopListNameItem?.name as String
                    ), "Share by "
                )
            )
        }
        return super.onOptionsItemSelected(item)
    }

    companion object {
        const val SHOP_LIST_NAME = "shop_list_name"
    }


    override fun onClickItem(shopListItem: ShopListItem, state: Int) {
        when (state) {
            ShopListItemAdapter.CHECK_BOX -> mainViewModel.updateListItem(shopListItem)
            ShopListItemAdapter.EDIT -> editListItem(shopListItem)
        }
    }

    private fun editListItem(shopListItem: ShopListItem) {
        EditListDialog.showDialog(this, shopListItem, object : EditListDialog.Listener {
            override fun onClick(item: ShopListItem) {
                mainViewModel.updateListItem(item)
            }

        })
    }

 //   override fun onBackPressed() {
 //       super.onBackPressed()
 //   }
    private fun saveItemCount(){
     var checkedItemCounter = 0
     adapter?.currentList?.forEach(){
         if (it.itemChecked) checkedItemCounter++
     }
        val tempShopListNameItem = shopListNameItem?.copy(
            allItemCounter = adapter?.itemCount as Int,
            checkedItemCounter = checkedItemCounter
        )
     mainViewModel.updateListName(tempShopListNameItem!!)
    }

    private val onBackPressedCallback = object : OnBackPressedCallback( true ) {
        override fun handleOnBackPressed() {
            saveItemCount()
            finish()

        }
    }
    private fun getSelectedTheme(): Int {
        return if (defPref.getString("theme_key", "green light")=="green light"){
            R.style.Base_Theme_Green_light
        } else {
            R.style.Base_Theme_Green
        }
    }
}