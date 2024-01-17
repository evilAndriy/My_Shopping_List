package com.vozniak.myshoppinglist.fragments

import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.activityViewModels
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.vozniak.myshoppinglist.R
import com.vozniak.myshoppinglist.activities.MainActivity
import com.vozniak.myshoppinglist.activities.MainApp
import com.vozniak.myshoppinglist.activities.NewNoteActivity
import com.vozniak.myshoppinglist.databinding.FragmentNoteBinding
import com.vozniak.myshoppinglist.db.MainDataBase
import com.vozniak.myshoppinglist.db.MainViewModel
import com.vozniak.myshoppinglist.db.NoteAdapter
import com.vozniak.myshoppinglist.entities.NoteItem

class NoteFragment : BaseFragment(), NoteAdapter.Listener {
    lateinit var binding: FragmentNoteBinding
    lateinit var editLauncher: ActivityResultLauncher<Intent>
    lateinit var adapter: NoteAdapter
    lateinit var defPreferences: SharedPreferences

    private val mainViewModel: MainViewModel by activityViewModels {
        MainViewModel.MainViewModelFactory((context?.applicationContext as MainApp).dataBase)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        onEditResult()

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNoteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRcView()
        observer()
    }

    override fun onClickNew() {
        editLauncher.launch(Intent(activity, NewNoteActivity::class.java))
    }

    private fun onEditResult() {
        editLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                val editState = it.data?.getStringExtra(EDIT_STATE_KEY)
                if (editState == "update") {
                    mainViewModel.updateNote(
                        it.data?.getSerializableExtra(
                            NEW_NOTE_KEY,
                            NoteItem::class.java
                        ) as NoteItem
                    )
                } else {
                    mainViewModel.insertNote(
                        it.data?.getSerializableExtra(
                            NEW_NOTE_KEY,
                            NoteItem::class.java
                        ) as NoteItem
                    )
                }
            }
        }
    }


    private fun observer() {
        mainViewModel.allNotes.observe(viewLifecycleOwner, {
            adapter.submitList(it)
        })
    }

    private fun initRcView() = with(binding) {

        defPreferences =
            PreferenceManager.getDefaultSharedPreferences(activity as AppCompatActivity)
        rViewNote.layoutManager = getLayoutManager()
        adapter = NoteAdapter(this@NoteFragment, defPreferences)
        rViewNote.adapter = adapter
    }

    private fun getLayoutManager(): RecyclerView.LayoutManager {
        return if (defPreferences.getString("note_style_key", "Linear") == "Linear") {
            LinearLayoutManager(activity)
        } else {
            StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        }
    }

    override fun deleteItem(id: Int) {
        mainViewModel.deleteNote(id)

    }


    override fun onClickItem(note: NoteItem) {
        val intent = Intent(activity, NewNoteActivity::class.java).apply {
            putExtra(NEW_NOTE_KEY, note)
        }
        editLauncher.launch(intent)
    }

    companion object {
        const val NEW_NOTE_KEY = "note_key"
        const val EDIT_STATE_KEY = "edit_state_key"

        @JvmStatic
        fun newInstance() = NoteFragment()
    }

}

