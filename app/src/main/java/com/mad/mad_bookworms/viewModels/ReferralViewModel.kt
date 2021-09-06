package com.mad.mad_bookworms.viewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.firestore.ktx.toObjects
import com.google.firebase.ktx.Firebase
import com.mad.mad_bookworms.data.Referral
import com.mad.mad_bookworms.data.User
import com.mad.mad_bookworms.data.Voucher
import kotlinx.coroutines.tasks.await

class ReferralViewModel :ViewModel() {
    private val col = Firebase.firestore.collection("Referral")
    private val referrals = MutableLiveData<List<Referral>>()

    init {
        col.addSnapshotListener {snap, _ -> referrals.value = snap?.toObjects() }
    }

    fun set(f: Referral) {
        // TODO
        col.document().set(f)
    }

    suspend fun get(id: String): Voucher? {
        return col.document(id).get().await().toObject<Voucher>()
    }

    suspend fun getReferral(uid: String): Voucher? {
//        return vouchers.value?.find { b -> b.uid == uid}
        return col.document("uid").get().await().toObject<Voucher>()
    }

    fun getAll() = referrals

}