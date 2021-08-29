package com.mad.mad_bookworms.viewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.firestore.ktx.toObjects
import com.google.firebase.ktx.Firebase
import com.mad.mad_bookworms.data.Book
import kotlinx.coroutines.tasks.await

class BookViewModel : ViewModel() {
    private val col = Firebase.firestore.collection("books")
    private val books = MutableLiveData<List<Book>>()

    init {
        col.addSnapshotListener {snap, _ -> books.value = snap?.toObjects() }
    }

    suspend fun get(id: String): Book? {
        return col.document(id).get().await().toObject<Book>()
    }

//    fun get(id: String): Book? {
//        return books.value?.find { b -> b.id == id}
//    }

    fun getAll() = books


}