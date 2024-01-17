package com.vozniak.myshoppinglist.dialogs

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import androidx.core.content.ContextCompat
import com.vozniak.myshoppinglist.R
import com.vozniak.myshoppinglist.databinding.DeleteDialogBinding
import com.vozniak.myshoppinglist.databinding.NewListDialogBinding

object DeleteDialog {
    fun showDialog(context: Context, listener: Listener) {
        var dialog: AlertDialog? = null
        val builder = AlertDialog.Builder(context)
        val binding = DeleteDialogBinding.inflate(LayoutInflater.from(context))
        binding.bDelete.setBackgroundColor(ContextCompat.getColor(context,R.color.red2))
        builder.setView(binding.root)
        binding.apply {
            bDelete.setOnClickListener() {
                listener.onClick()
                dialog?.dismiss()
            }
            bCancel.setOnClickListener() {
                dialog?.dismiss()
            }

        }

        dialog = builder.create()
        dialog?.window?.setBackgroundDrawable(null)
        dialog?.show()
    }

    interface Listener {
        fun onClick()
    }
}