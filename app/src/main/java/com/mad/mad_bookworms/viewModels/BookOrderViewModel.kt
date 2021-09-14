package com.mad.mad_bookworms.viewModels

import android.os.Handler
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.firestore.ktx.toObjects
import com.google.firebase.ktx.Firebase
import com.mad.mad_bookworms.data.BookOrder
import com.mad.mad_bookworms.data.Order
import kotlinx.coroutines.tasks.await

class BookOrderViewModel : ViewModel() {
    private val col = Firebase.firestore.collection("BookOrders")
    private val bookOrders = MutableLiveData<List<BookOrder>>()
    private var list = listOf<BookOrder>() // Original data


    init {
        col.addSnapshotListener {snap, _ ->
            if (snap == null) return@addSnapshotListener

            bookOrders.value = snap.toObjects()
            list = snap.toObjects<BookOrder>()
        }
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

    fun getOrder(id: String): MutableLiveData<List<BookOrder>> {

        var list = this.list
        list = list.filter { f ->
            f.order_id.equals(id, true)
        }

        bookOrders.value = list

        return bookOrders

    }
}