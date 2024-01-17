package com.vozniak.myshoppinglist.activities

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Spannable
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.view.ActionMode
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.MenuItem.OnMenuItemClickListener
import android.view.View
import android.view.animation.Animation
import android.view.animation.Animation.AnimationListener
import android.view.animation.AnimationUtils
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.core.text.getSpans
import androidx.core.view.MenuProvider
import androidx.preference.PreferenceManager
import com.vozniak.myshoppinglist.R
import com.vozniak.myshoppinglist.databinding.ActivityNewNoteBinding
import com.vozniak.myshoppinglist.entities.NoteItem
import com.vozniak.myshoppinglist.fragments.NoteFragment
import com.vozniak.myshoppinglist.utils.HtmlManager
import com.vozniak.myshoppinglist.utils.MyTouchListener
import com.vozniak.myshoppinglist.utils.TimeManager
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class NewNoteActivity : AppCompatActivity() {
    private lateinit var binding: ActivityNewNoteBinding
    private var note: NoteItem? = null
    private var pref:SharedPreferences? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        pref = PreferenceManager.getDefaultSharedPreferences(this)
        setTheme(getSelectedTheme())
        super.onCreate(savedInstanceState)
        binding = ActivityNewNoteBinding.inflate(layoutInflater)
        setContentView(binding.root)
        actionBarSettings()
        getNote()
        init()
        setTextSize() //!
        onClickColorPicker()
        actionMenuCallBack()
    }

    private fun getNote() {
        note = intent.getSerializableExtra(NoteFragment.NEW_NOTE_KEY, NoteItem::class.java)
        fillNote()
    }

    private fun fillNote() = with(binding) {
        if (note != null) {
            edTitle.setText(note?.title)
            edDescription.setText(HtmlManager.getFromHtml(note?.content as String))
        }
    }

    private fun setMainResult() {
        var editState = "new"
        val tempNote: NoteItem? = if (note == null) {
            createNewNote()
        } else {
            editState = "update"
            updateNote()
        }
        val i = Intent().apply {
            putExtra(NoteFragment.NEW_NOTE_KEY, tempNote)
            putExtra(NoteFragment.EDIT_STATE_KEY, editState)
        }
        setResult(RESULT_OK, i)
        finish()

    }

    private fun updateNote(): NoteItem? = with(binding) {
        return note?.copy(
            title = edTitle.text.toString(),
            content = HtmlManager.toHtml(edDescription.text)
        )

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()
            R.id.id_save -> setMainResult()
            R.id.id_bold -> setBoldForSelectedText()
            R.id.id_color -> if (binding.colorPicker.isShown) {
                closeColorPicker()
            } else {
                openColorPicker()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setBoldForSelectedText() = with(binding) {
        val startPos = edDescription.selectionStart
        val endPos = edDescription.selectionEnd
        val styles = edDescription.text.getSpans(startPos, endPos, StyleSpan::class.java)
        var boldStyle: StyleSpan? = null
        if (styles.isNotEmpty()) {
            edDescription.text.removeSpan(styles[0])
        } else {
            boldStyle = StyleSpan(Typeface.BOLD)

        }
        edDescription.text.setSpan(boldStyle, startPos, endPos, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        edDescription.text.trim()
        edDescription.setSelection(startPos)

    }

    private fun setColorForSelectedText(colorId: Int) = with(binding) {
        val startPos = edDescription.selectionStart
        val endPos = edDescription.selectionEnd

        val styles = edDescription.text.getSpans(startPos, endPos, ForegroundColorSpan::class.java)

        if (styles.isNotEmpty()) edDescription.text.removeSpan(styles[0])

        edDescription.text.setSpan(
            ForegroundColorSpan(
                ContextCompat.getColor(this@NewNoteActivity, colorId)
            ),
            startPos,
            endPos,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        edDescription.text.trim()
        edDescription.setSelection(startPos)

    }

    private fun onClickColorPicker() = with(binding) {
        ibRed.setOnClickListener() {
            setColorForSelectedText(R.color.picker_red)
        }
        ibGreen.setOnClickListener() {
            setColorForSelectedText(R.color.picker_green)
        }
        ibYellow.setOnClickListener() {
            setColorForSelectedText(R.color.picker_yellow)
        }
        ibPurple.setOnClickListener() {
            setColorForSelectedText(R.color.picker_purple)
        }
        ibOrange.setOnClickListener() {
            setColorForSelectedText(R.color.picker_orange)
        }
        ibBlue.setOnClickListener() {
            setColorForSelectedText(R.color.picker_blue)
        }
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.nw_note_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }


    private fun createNewNote(): NoteItem {
        return NoteItem(
            null,
            binding.edTitle.text.toString(),
            HtmlManager.toHtml(binding.edDescription.text),
            TimeManager.getCurrentTime(),
            ""
        )
    }


    private fun actionBarSettings() {
        val ab = supportActionBar
        ab?.setDisplayHomeAsUpEnabled(true)

    }

    private fun openColorPicker() {
        binding.colorPicker.visibility = View.VISIBLE
        val openAnim = AnimationUtils.loadAnimation(this, R.anim.open_color_picker)
        binding.colorPicker.startAnimation(openAnim)
    }

    private fun closeColorPicker() {
        val openAnim = AnimationUtils.loadAnimation(this, R.anim.close_color_picker)
        openAnim.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(p0: Animation?) {
            }

            override fun onAnimationEnd(p0: Animation?) {
                binding.colorPicker.visibility = View.GONE
            }

            override fun onAnimationRepeat(p0: Animation?) {
            }
        })
        binding.colorPicker.startAnimation(openAnim)
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun init() {
        binding.colorPicker.setOnTouchListener(MyTouchListener())
        pref = PreferenceManager.getDefaultSharedPreferences(this)
    }

    private fun actionMenuCallBack() {
        val actionCallBack = object : ActionMode.Callback {
            override fun onCreateActionMode(p0: ActionMode?, p1: Menu?): Boolean {
                p1?.clear()
                return true

            }

            override fun onPrepareActionMode(p0: ActionMode?, p1: Menu?): Boolean {
                p1?.clear()
                return true
            }

            override fun onActionItemClicked(p0: ActionMode?, p1: MenuItem?): Boolean {
                return true
            }

            override fun onDestroyActionMode(p0: ActionMode?) {

            }
        }
        binding.edDescription.customSelectionActionModeCallback = actionCallBack
    }
    private fun setTextSize() = with(binding){
        edTitle.setTextSize(pref?.getString("title_size_key","16"))
        edDescription.setTextSize(pref?.getString("content_size_key", "14"))
    }
    private fun EditText.setTextSize(size:String?){
        if(size!= null) this.textSize = size.toFloat()
    }
    private fun getSelectedTheme(): Int {
        return if (pref?.getString("theme_key", "green light")=="green light"){
            R.style.Base_Theme_Green_light
        } else {
            R.style.Base_Theme_Green
        }
    }
}