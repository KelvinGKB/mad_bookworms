package com.mad.mad_bookworms.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mad.mad_bookworms.App.Companion.db
import com.mad.mad_bookworms.data.MyCartTable
import kotlinx.coroutines.launch

class CartOrderViewModel : ViewModel() {

    fun getAll() = db.MyCartDao.getAll()

    suspend fun get(uid: String,bookId: String) = db.MyCartDao.get(uid,bookId)

    fun insert(b: MyCartTable) = viewModelScope.launch { db.MyCartDao.insert(b) }

    fun updateQty (uid: String, bookId: String, qty: Int) = viewModelScope.launch { db.MyCartDao.updateQty(uid,bookId,qty) }

    fun update(b: MyCartTable) = viewModelScope.launch { db.MyCartDao.update(b) }

    fun delete(b: MyCartTable) = viewModelScope.launch { db.MyCartDao.delete(b) }

    fun deleteCart(uid:String, bookId: String) = viewModelScope.launch { db.MyCartDao.deleteCart(uid,bookId) }
}