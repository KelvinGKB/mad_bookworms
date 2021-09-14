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

    private var list = listOf<Order>() // Original data
    private var name = ""       // Search
//    private var categoryId = "" // Filter
    private var field = ""      // Sort
    private var reverse = false // Sort


    init {
        col.addSnapshotListener {snap, _ ->
            if (snap == null) return@addSnapshotListener

            orders.value = snap.toObjects()
            list = snap.toObjects<Order>()
        }

    }

    suspend fun get(id: String): Order? {
        if(id.isNotBlank()){
            return col.document(id).get().await().toObject<Order>()
        }else{
            return null
        }
    }

    //Admin
    private fun updateResult() {
        var list = this.list

        // TODO(23): Search + filter
        list = list.filter { f ->
            f.id.contains(name, true)
        }

        // TODO(24): Sort
        list = when (field) {
            "id"    -> list.sortedBy { f -> f.id }
            "paymentType"  -> list.sortedBy { f -> f.paymentType }
            "dateTime"  -> list.sortedBy { f -> f.dateTime }
            "amount" -> list.sortedBy { f -> f.amount }
            else    -> list
        }

        if (reverse) list = list.reversed()

        orders.value = list
    }


    // Client side
    fun getAll() = orders

    fun set(o: Order){
        col.document(o.id).set(o)
    }
    // Client side


    fun search(name: String) {
        this.name = name
        updateResult()
    }

    fun sort(field: String): Boolean {
        reverse = if (this.field == field) !reverse else false
        this.field = field
        updateResult()

        return reverse
    }

    fun delete(id: String) {
        // TODO
        col.document(id).delete()
    }


}