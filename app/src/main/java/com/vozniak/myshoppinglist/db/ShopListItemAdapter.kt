package com.vozniak.myshoppinglist.db

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.vozniak.myshoppinglist.R
import com.vozniak.myshoppinglist.databinding.ListNameItemBinding
import com.vozniak.myshoppinglist.databinding.ShopListItemBinding
import com.vozniak.myshoppinglist.entities.ShopListNameItem
import com.vozniak.myshoppinglist.entities.ShopListItem

class ShopListItemAdapter(private val listener: Listener) :
    ListAdapter<ShopListItem, ShopListItemAdapter.ItemHolder>(ItemComparator()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
          return  ItemHolder.createShopItem(parent)
    }

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
            holder.setItemData(getItem(position), listener)
    }


    class ItemHolder(val view: View) : RecyclerView.ViewHolder(view) {
        fun setItemData(shopListItem: ShopListItem,listener: Listener) {
            val binding = ShopListItemBinding.bind(view)
            binding.apply {
                tvName.text = shopListItem.name
                tvInfo.text = shopListItem.itemInfo
                tvInfo.visibility = infoVisibility(shopListItem)
                checkBox.isChecked = shopListItem.itemChecked
                setPaintFlagAndColor(binding)

                checkBox.setOnClickListener(){
                    listener.onClickItem(shopListItem.copy(itemChecked = checkBox.isChecked),
                        CHECK_BOX)
                }
                imEdit.setOnClickListener(){
                    listener.onClickItem(shopListItem, EDIT)
                }

            }
        }

        private fun setPaintFlagAndColor(binding: ShopListItemBinding){
            binding.apply {
                if (checkBox.isChecked){
                    tvName.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
                    tvInfo.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
                    tvName.setTextColor(ContextCompat.getColor(binding.root.context,R.color.grey_light))
                    tvInfo.setTextColor(ContextCompat.getColor(binding.root.context, R.color.grey_light))
                } else {
                    tvName.paintFlags = Paint.ANTI_ALIAS_FLAG
                    tvInfo.paintFlags = Paint.ANTI_ALIAS_FLAG
                    tvName.setTextColor(ContextCompat.getColor(binding.root.context,R.color.grey))
                    tvInfo.setTextColor(ContextCompat.getColor(binding.root.context, R.color.grey))
                }
            }

        }
        private fun infoVisibility(shopListItem: ShopListItem):Int{
            return if (shopListItem.itemInfo.isNullOrEmpty()){
                View.GONE
            }else {
                View.VISIBLE
            }
        }

        companion object {
            fun createShopItem(parent: ViewGroup): ItemHolder {
                return ItemHolder(
                    LayoutInflater.from(parent.context)
                        .inflate(R.layout.shop_list_item, parent, false)
                )
            }
        }

    }

    class ItemComparator : DiffUtil.ItemCallback<ShopListItem>() {
        override fun areItemsTheSame(
            oldItem: ShopListItem,
            newItem: ShopListItem
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: ShopListItem,
            newItem: ShopListItem
        ): Boolean {
            return oldItem == newItem
        }

    }
    interface Listener {
        fun onClickItem(shopListItem: ShopListItem, state: Int){

        }
    }
    companion object {
        const val EDIT = 0
        const val CHECK_BOX = 1
    }
}
