package com.vozniak.myshoppinglist.activities

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import androidx.preference.PreferenceManager
import com.vozniak.myshoppinglist.R
import com.vozniak.myshoppinglist.databinding.ActivityMainBinding
import com.vozniak.myshoppinglist.fragments.FragmentManager
import com.vozniak.myshoppinglist.fragments.NoteFragment
import com.vozniak.myshoppinglist.fragments.ShopListNamesFragment
import com.vozniak.myshoppinglist.settings.SettingsActivity
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    private var currentMenuItemId = R.id.shop_list
    private lateinit var defPref: SharedPreferences
    private var currentTheme = ""


    override fun onCreate(savedInstanceState: Bundle?) {

        defPref = PreferenceManager.getDefaultSharedPreferences(this)
        currentTheme = defPref.getString("theme_key", "green light").toString()
        setTheme(getSelectedTheme())
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        FragmentManager.setFragment(ShopListNamesFragment.newInstance(), this)
        setBottomNavListener()
    }

    fun setBottomNavListener() {
        binding.bNav.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.settings -> startActivity(Intent(this@MainActivity, SettingsActivity::class.java))

                R.id.notes -> {
                    currentMenuItemId = R.id.notes
                    FragmentManager.setFragment(NoteFragment.newInstance(), this)
                }

                R.id.shop_list -> {
                    currentMenuItemId = R.id.shop_list
                    FragmentManager.setFragment(ShopListNamesFragment.newInstance(), this)
                }

                R.id.new_item -> FragmentManager.currentFragment?.onClickNew()
            }

            true
        }
    }

    override fun onResume() {
        super.onResume()
        binding.bNav.selectedItemId = currentMenuItemId
        if (defPref.getString("theme_key", "green light") != currentTheme) recreate()
    }

    private fun getSelectedTheme(): Int {
        return if (defPref.getString("theme_key", "green light") == "green light") {
            R.style.Base_Theme_Green_light
        } else {
            R.style.Base_Theme_Green
        }
    }
}