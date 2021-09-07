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

    fun delete(id: String) {
        // TODO
        col.document(id).delete()
    }

    fun deleteAll() {
        // TODO
        // col.get().addOnSuccessListener { snap -> snap.documents.forEach { doc -> delete(doc.id) } }
        vouchers.value?.forEach { f -> delete(f.id) }
    }

    fun set(f: Voucher) {
        // TODO
        if(f.id == "")
        col.document().set(f)
        else
            col.document(f.id).set(f)

    }

    private fun idExists(id: String): Boolean {
        return vouchers.value?.any { f -> f.id == id } ?: false
    }

    fun validate(f: Voucher, insert: Boolean = true): String {
        var e = ""

        e += if (f.requiredPoint == 0) "- Points is required.\n"
        else if (f.requiredPoint < 0) "Points Must more than 0.\n"
        else ""

        return e
    }


}