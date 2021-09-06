package com.mad.mad_bookworms.viewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.firestore.ktx.toObjects
import com.google.firebase.ktx.Firebase
import com.mad.mad_bookworms.data.Order
import kotlinx.coroutines.tasks.await

class OrderViewModel : ViewModel() {
    private val col = Firebase.firestore.collection("orders")
    private val orders = MutableLiveData<List<Order>>()

    init {
        col.addSnapshotListener {snap, _ -> orders.value = snap?.toObjects() }
    }

    suspend fun get(id: String): Order? {
        if(id.isNotBlank()){
            return col.document(id).get().await().toObject<Order>()
        }else{
            return null
        }
    }

    fun getAll() = orders

    fun set(o: Order){
        col.document(o.id).set(o)
    }
}