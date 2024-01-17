package com.vozniak.myshoppinglist.dialogs

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import com.vozniak.myshoppinglist.R
import com.vozniak.myshoppinglist.databinding.EditListItemDialogBinding
import com.vozniak.myshoppinglist.databinding.NewListDialogBinding
import com.vozniak.myshoppinglist.entities.ShopListItem

object EditListDialog {
    fun showDialog(context: Context, item: ShopListItem, listener: Listener, ) {
        var dialog: AlertDialog? = null
        val builder = AlertDialog.Builder(context)
        val binding = EditListItemDialogBinding.inflate(LayoutInflater.from(context))
        builder.setView(binding.root)
        binding.apply {
            bUpdate.setOnClickListener(){
                if (edName.text.toString().isNotEmpty()){
                    listener.onClick(item.copy(name = edName.text.toString(), itemInfo = edInfo.text.toString()))
                }
                dialog?.dismiss()

            }

            }

        dialog = builder.create()
        dialog?.window?.setBackgroundDrawable(null)
        dialog?.show()
    }

    interface Listener {
        fun onClick(item: ShopListItem)
    }
}