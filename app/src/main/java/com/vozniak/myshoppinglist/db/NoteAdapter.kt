package com.vozniak.myshoppinglist.db

import android.content.SharedPreferences
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.vozniak.myshoppinglist.R
import com.vozniak.myshoppinglist.databinding.NoteListItemBinding
import com.vozniak.myshoppinglist.entities.NoteItem
import com.vozniak.myshoppinglist.utils.HtmlManager
import com.vozniak.myshoppinglist.utils.TimeManager

class NoteAdapter(val listenr : Listener, private val defPref:SharedPreferences) : ListAdapter<NoteItem, NoteAdapter.ItemHolder>(ItemComparator()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        return ItemHolder.create(parent)
    }

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        holder.setData(getItem(position), listenr, defPref)
    }

    class ItemHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val binding = NoteListItemBinding.bind(view)
        fun setData(note: NoteItem,listenr: Listener,defPref:SharedPreferences) = with(binding) {
            tvTitle.text = note.title
            tvDescription.text = HtmlManager.getFromHtml(note.content).trim()
            tvTime.text = TimeManager.getCurrentTime()
            imDelete.setOnClickListener(){
                listenr.deleteItem((note.id) as Int)
            }
            itemView.setOnClickListener(){
                listenr.onClickItem(note)
            }
        }

        companion object {
            fun create(parent: ViewGroup): ItemHolder {
                return ItemHolder(
                    LayoutInflater.from(parent.context)
                        .inflate(R.layout.note_list_item, parent, false)
                )
            }

        }

    }

    class ItemComparator : DiffUtil.ItemCallback<NoteItem>() {
        override fun areItemsTheSame(oldItem: NoteItem, newItem: NoteItem): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: NoteItem, newItem: NoteItem): Boolean {
            return oldItem == newItem
        }

    }

    interface Listener {
        fun deleteItem(id : Int)
        fun onClickItem(note : NoteItem)
    }
}