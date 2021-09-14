package com.mad.mad_bookworms

import android.app.Application
import android.content.Context
import com.mad.mad_bookworms.data.LocalDB

class App : Application() {
    companion object {
        lateinit var db: LocalDB
        var instance: App? = null
        const val PREFS: String = "SHARED_PREFS"
        const val LOCALE: String = "LOCALE"
    }

    override fun onCreate() {
        super.onCreate()
        db = LocalDB.getInstance(this)
        instance = this

    }
    fun setLanguagePref(localeKey: String) {
        val pref = getSharedPreferences(PREFS, Context.MODE_PRIVATE).edit()
        pref.putString(LOCALE, localeKey)
        pref.apply()
    }

    fun getLanguagePref():String? {
        val pref = getSharedPreferences(PREFS, Context.MODE_PRIVATE)
        return pref.getString(LOCALE,"en")
    }

}