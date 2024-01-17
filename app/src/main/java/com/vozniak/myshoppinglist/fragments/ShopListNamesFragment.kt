package com.vozniak.myshoppinglist.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.vozniak.myshoppinglist.activities.MainApp
import com.vozniak.myshoppinglist.activities.ShopListActivity
import com.vozniak.myshoppinglist.databinding.FragmentShopListNamesBinding
import com.vozniak.myshoppinglist.db.MainViewModel
import com.vozniak.myshoppinglist.db.ShopListNameAdapter
import com.vozniak.myshoppinglist.dialogs.DeleteDialog
import com.vozniak.myshoppinglist.dialogs.NewListDialog
import com.vozniak.myshoppinglist.entities.ShopListNameItem
import com.vozniak.myshoppinglist.utils.TimeManager

class ShopListNamesFragment : BaseFragment(), ShopListNameAdapter.Listener {
    lateinit var adapter: ShopListNameAdapter
    lateinit var binding: FragmentShopListNamesBinding
    private val mainViewModel: MainViewModel by activityViewModels {
        MainViewModel.MainViewModelFactory((context?.applicationContext as MainApp).dataBase)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentShopListNamesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRcView()
        observer()
    }


    override fun onClickNew() {
        NewListDialog.showDialog(activity as AppCompatActivity, object : NewListDialog.Listener {
            override fun onClick(name: String) {
                val shopListName = ShopListNameItem(
                    null,
                    name,
                    TimeManager.getCurrentTime(),
                    0,
                    0,
                    ""
                )
                mainViewModel.insertShopListName(shopListName)
            }
        }, "")
    }


    private fun observer() {
        mainViewModel.allShopListNames.observe(viewLifecycleOwner, {
            adapter.submitList(it)
        })
    }

    private fun initRcView() = with(binding) {
        rcView.layoutManager = LinearLayoutManager(activity)
        adapter = ShopListNameAdapter(this@ShopListNamesFragment)
        rcView.adapter = adapter
    }


    companion object {

        @JvmStatic
        fun newInstance() = ShopListNamesFragment()
    }

    override fun deleteItem(id: Int) {
        DeleteDialog.showDialog(activity as AppCompatActivity, object : DeleteDialog.Listener {
            override fun onClick() {
                mainViewModel.deleteShopListName(id)
            }

        })
    }

    override fun editItem(shopListNameItem: ShopListNameItem) {
        NewListDialog.showDialog(activity as AppCompatActivity, object : NewListDialog.Listener {
            override fun onClick(name: String) {
                mainViewModel.updateListName(shopListNameItem.copy(name = name))
            }

        }, shopListNameItem.name)


    }

    override fun onClickItem(shopListNameItem: ShopListNameItem) {
        val i = Intent(activity, ShopListActivity::class.java).apply {
            putExtra(ShopListActivity.SHOP_LIST_NAME, shopListNameItem)
        }
        startActivity(i)
    }

}