package com.mad.mad_bookworms.viewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.firestore.ktx.toObjects
import com.google.firebase.ktx.Firebase
import com.mad.mad_bookworms.data.Book
import com.mad.mad_bookworms.data.MyVoucher
import com.mad.mad_bookworms.data.Voucher
import kotlinx.coroutines.tasks.await

class VoucherViewModel : ViewModel() {

    private val col = Firebase.firestore.collection("Voucher")
    private val vouchers = MutableLiveData<List<Voucher>>()

    init {
        col.addSnapshotListener {snap, _ -> vouchers.value = snap?.toObjects() }
    }

    suspend fun get(id: String): Voucher? {
        return col.document(id).get().await().toObject<Voucher>()
    }

    suspend fun getUser(uid: String): Voucher? {
//        return vouchers.value?.find { b -> b.uid == uid}
        return col.document("uid").get().await().toObject<Voucher>()
    }

    fun getAll() = vouchers

}