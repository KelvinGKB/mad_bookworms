package com.mad.mad_bookworms.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mad.mad_bookworms.data.MyCartTable
import com.mad.mad_bookworms.data.MyFavouriteTable
import kotlinx.coroutines.launch
import com.mad.mad_bookworms.App.Companion.db

class MyFavouriteViewModel : ViewModel() {

    fun getAll() = db.MyFavouriteDao.getAll()

    suspend fun get(uid: String,bookId: String) = db.MyFavouriteDao.get(uid,bookId)

    fun insert(b: MyFavouriteTable) = viewModelScope.launch { db.MyFavouriteDao.insert(b) }

    fun update(b: MyFavouriteTable) = viewModelScope.launch { db.MyFavouriteDao.update(b) }

    fun delete(b: MyFavouriteTable) = viewModelScope.launch { db.MyFavouriteDao.delete(b) }

    fun deleteFavourite(uid:String, bookId: String) = viewModelScope.launch { db.MyFavouriteDao.deleteFavourite(uid,bookId) }

}