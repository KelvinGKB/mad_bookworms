package com.mad.mad_bookworms.viewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.firestore.ktx.toObjects
import com.google.firebase.ktx.Firebase
import com.mad.mad_bookworms.data.Feedback
import kotlinx.coroutines.tasks.await

class FeedbackViewModel : ViewModel() {
    private val col = Firebase.firestore.collection("Feedback")
    private val feedbacks = MutableLiveData<List<Feedback>>()

    init {
        col.addSnapshotListener {snap, _ -> feedbacks.value = snap?.toObjects() }
    }

    suspend fun get(id: String): Feedback? {
        if(id.isNotBlank()){
            return col.document(id).get().await().toObject<Feedback>()
        }else{
            return null
        }
    }

//    fun get(id: String): Book? {
//        return books.value?.find { b -> b.id == id}
//    }

    fun getAll() = feedbacks

    fun delete(id: String) {
        col.document(id).delete()
    }

    fun set(f: Feedback) {
        col.document().set(f)
    }

//    private fun idExists(id: String): Boolean {
//        return books.value?.any { f -> f.id == id } ?: false
//    }

    fun validate(f: Feedback, insert: Boolean = true): String {
        var e = ""

        e += if (f.desc == "") "- Description is required.\n"
        else if (f.desc.length < 10) "- Description is too short.\n"
        else if (f.desc.length > 250) "- Description is too long.\n"
        else ""

        e += if (f.type == "") "- Type is required.\n"
        else ""

        e += if (f.rating == 0) "- Points is required.\n"
        else ""

        return e
    }


}