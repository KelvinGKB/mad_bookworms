package com.mad.mad_bookworms

import android.app.Application
import com.mad.mad_bookworms.data.LocalDB

class App : Application() {
    companion object {
        lateinit var db: LocalDB
    }

    override fun onCreate() {
        super.onCreate()
        db = LocalDB.getInstance(this)

    }
}