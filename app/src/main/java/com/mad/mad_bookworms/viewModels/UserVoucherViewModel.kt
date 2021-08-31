package com.mad.mad_bookworms.viewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.firestore.ktx.toObjects
import com.google.firebase.ktx.Firebase
import com.mad.mad_bookworms.data.Book
import com.mad.mad_bookworms.data.MyVoucher
import com.mad.mad_bookworms.data.User
import kotlinx.coroutines.tasks.await

class UserVoucherViewModel : ViewModel() {

    private val col = Firebase.firestore.collection("UserVoucher")
    private val vouchers = MutableLiveData<List<MyVoucher>>()

    init {
        col.addSnapshotListener {snap, _ -> vouchers.value = snap?.toObjects() }
    }

    fun set(f: MyVoucher) {
        // TODO
        col.document(f.id).set(f)
    }

    suspend fun get(id: String): MyVoucher? {
        return col.document(id).get().await().toObject<MyVoucher>()
    }

    suspend fun getUser(uid: String): MyVoucher? {
//        return vouchers.value?.find { b -> b.uid == uid}
        return col.document("uid").get().await().toObject<MyVoucher>()
    }

    fun getAll() = vouchers

}