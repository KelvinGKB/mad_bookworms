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
        if(id.isNotBlank()){
            return col.document(id).get().await().toObject<Book>()
        }else{
            return null
        }
    }

//    fun get(id: String): Book? {
//        return books.value?.find { b -> b.id == id}
//    }

    fun getAll() = books

    fun delete(id: String) {
        // TODO
        col.document(id).delete()
    }

    fun deleteAll() {
        // TODO
        // col.get().addOnSuccessListener { snap -> snap.documents.forEach { doc -> delete(doc.id) } }
        books.value?.forEach { f -> delete(f.id) }
    }

    fun set(f: Book) {
        // TODO
        col.document(f.id).set(f)
    }

    private fun idExists(id: String): Boolean {
        return books.value?.any { f -> f.id == id } ?: false
    }

    fun validate(f: Book, insert: Boolean = true): String {
        val regexId = Regex("""^[0-9A-Z]{4}$""")
        var e = ""

        if (insert) {
            e += if (f.id == "") "- Id is required.\n"
            else if (!f.id.matches(regexId)) "- Id format is invalid.\n"
            else if (idExists(f.id)) "- Id is duplicated.\n"
            else ""
        }

        e += if (f.title == "") "- Name is required.\n"
        else if (f.title.length < 3) "- Name is too short.\n"
        //else if (nameExists(f.name)) "- Name is duplicated.\n"
        else ""

        e += if (f.pages == 0) "- Age is required.\n"
        else if (f.pages < 18) "- Underage.\n"
        else ""

        e += if (f.image.toBytes().isEmpty()) "- Photo is required.\n"
        else ""

        return e
    }


}