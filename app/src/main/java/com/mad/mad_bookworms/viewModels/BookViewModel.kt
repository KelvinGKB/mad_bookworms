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

    private var list = listOf<Book>() // Original data
    private var name = ""       // Search
    private var field = ""      // Sort
    private var reverse = false // Sort


    init {
        col.addSnapshotListener {snap, _ ->
            if (snap == null) return@addSnapshotListener

            books.value = snap?.toObjects()
            list = snap.toObjects<Book>()

        }
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
        col.document(id).delete()
    }

    fun deleteAll() {
        // col.get().addOnSuccessListener { snap -> snap.documents.forEach { doc -> delete(doc.id) } }
        books.value?.forEach { f -> delete(f.id) }
    }

    fun set(f: Book) {
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

        e += if (f.title == "") "- Title is required.\n"
        else if (f.title.length < 3) "- Title is too short.\n"
        //else if (nameExists(f.name)) "- Name is duplicated.\n"
        else ""

        e += if (f.author == "") "- Author is required.\n"
        else if (f.author.length > 50) "- Author is too long.\n"
        else ""

        e += if (f.description == "") "- Description is required.\n"
        else if (f.description.length > 150) "- Title is too long.\n"
        else ""


        e += if (f.price == 0.0) "- Price is required.\n"
        else if (f.price < 0) "Price Must more than 0.\n"
        else ""

        e += if (f.requiredPoint == 0) "- Points is required.\n"
        else if (f.requiredPoint < 0) "Points Must more than 0.\n"
        else ""

        e += if (f.pages == 0) "- Page is required.\n"
        else if (f.pages < 0) "Page Must more than 0.\n"
        else ""


        e += if (f.image.toBytes().isEmpty()) "- Photo is required.\n"
        else ""

        return e
    }

    private fun updateResult() {
        var list = this.list

        // TODO(23): Search + filter
        list = list.filter { f ->
            f.title.contains(name, true)
        }

        // TODO(24): Sort
        list = when (field) {
            "id"    -> list.sortedBy { f -> f.id }
            "title"  -> list.sortedBy { f -> f.title }
            "price"  -> list.sortedBy { f -> f.price }
            else    -> list
        }

        if (reverse) list = list.reversed()

        books.value = list
    }

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



}