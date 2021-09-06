package com.mad.mad_bookworms.viewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.firestore.ktx.toObjects
import com.google.firebase.ktx.Firebase
import com.mad.mad_bookworms.data.BookOrder
import kotlinx.coroutines.tasks.await

class BookOrderViewModel : ViewModel() {
    private val col = Firebase.firestore.collection("BookOrders")
    private val bookOrders = MutableLiveData<List<BookOrder>>()

    init {
        col.addSnapshotListener {snap, _ -> bookOrders.value = snap?.toObjects() }
    }

    suspend fun get(id: String): BookOrder? {
        if(id.isNotBlank()){
            return col.document(id).get().await().toObject<BookOrder>()
        }else{
            return null
        }
    }

    fun getAll() = bookOrders

    fun set(o: BookOrder){
        col.document(o.id).set(o)
    }
}