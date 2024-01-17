package com.vozniak.myshoppinglist.activities

import android.app.Application
import com.vozniak.myshoppinglist.db.MainDataBase

class MainApp : Application() {

        val dataBase by lazy { MainDataBase.getDataBase(this) }

}