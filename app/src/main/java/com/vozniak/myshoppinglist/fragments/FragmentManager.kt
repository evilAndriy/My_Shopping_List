package com.vozniak.myshoppinglist.fragments

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import com.vozniak.myshoppinglist.R

object FragmentManager {
    var currentFragment: BaseFragment? = null

    fun setFragment(newFrag: BaseFragment, activity: AppCompatActivity) {
        val transaction = activity.supportFragmentManager.beginTransaction()
        transaction.replace(R.id.place_holder, newFrag).commit()
        currentFragment = newFrag
    }
}