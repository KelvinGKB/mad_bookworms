package com.mad.mad_bookworms.viewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObjects
import com.google.firebase.ktx.Firebase
import com.mad.mad_bookworms.data.Category

class CategoryViewModel : ViewModel() {
    private val col = Firebase.firestore.collection("category")
    private val categories = MutableLiveData<List<Category>>()

    init {
        col.addSnapshotListener {snap, _ -> categories.value = snap?.toObjects() }
    }

    fun getAll() = categories

}