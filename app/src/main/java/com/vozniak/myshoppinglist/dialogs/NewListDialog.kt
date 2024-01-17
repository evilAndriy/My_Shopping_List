package com.vozniak.myshoppinglist.dialogs

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import com.vozniak.myshoppinglist.R
import com.vozniak.myshoppinglist.databinding.NewListDialogBinding

object NewListDialog {
    fun showDialog(context: Context, listener: Listener, name : String) {
        var dialog: AlertDialog? = null
        val builder = AlertDialog.Builder(context)
        val binding = NewListDialogBinding.inflate(LayoutInflater.from(context))
        builder.setView(binding.root)
        binding.apply {
            edNewListName.setText(name)
            if (name.isNotEmpty()) bCreate.text = context.getString(R.string.update)
            bCreate.setOnClickListener() {
                val listName = edNewListName.text.toString()
                if (listName.isNotEmpty()) {
                    listener.onClick(listName)
                    dialog?.dismiss()
                } else {
                    dialog?.dismiss()
                }
            }
        }
        dialog = builder.create()
        dialog?.window?.setBackgroundDrawable(null)
        dialog?.show()
    }

    interface Listener {
        fun onClick(name: String)
    }
}