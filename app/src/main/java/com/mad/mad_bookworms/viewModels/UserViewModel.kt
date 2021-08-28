package com.mad.mad_bookworms.viewModels

import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.firestore.ktx.toObjects
import com.google.firebase.ktx.Firebase
import com.mad.mad_bookworms.data.User
import kotlinx.coroutines.tasks.await

class UserViewModel : ViewModel() {
    // TODO: Initialization
    private val col = Firebase.firestore.collection("Users")
    private val users = MutableLiveData<List<User>>()

    init {
        col.addSnapshotListener { snap, _ -> users.value = snap?.toObjects() }
    }

//    suspend fun get(id: String): Friend? {
//        // TODO
//        return col.document(id).get().await().toObject<Friend>()
//    }

//    fun get(id: String): User? {
//        return users.value?.find { f -> f.id == id }
//    }
    suspend fun get(id: String): User? {
        return col.document(id).get().await().toObject<User>()
    }

    fun getAll() = users

    fun delete(id: String) {
        // TODO
        col.document(id).delete()
    }

    fun deleteAll() {
        // TODO
        // col.get().addOnSuccessListener { snap -> snap.documents.forEach { doc -> delete(doc.id) } }
        users.value?.forEach { f -> delete(f.id) }
    }

    fun set(f: User) {
        // TODO
        col.document(f.id).set(f)
    }

    fun getReferrer(referral: String): User? {
        return users.value?.find { f -> f.referral_code == referral }
    }


    fun updateEarnPoints(id:String,current_points:Int,points:Int) {
        Firebase.firestore
            .collection("Users")
            .document(id)
            .update("earn_points", current_points+points)
            .addOnSuccessListener {}
    }

    fun updateUsablePoints(id:String,current_points:Int,points:Int) {
        Firebase.firestore
            .collection("Users")
            .document(id)
            .update("usable_points", current_points+points)
            .addOnSuccessListener {}
    }

    //----------------------------------------------------------------------------------------------

//    private suspend fun idExists(id: String): Boolean {
//        // TODO: Duplicated id?
//        return col.document(id).get().await().exists()
//    }

    private fun idExists(id: String): Boolean {
        return users.value?.any { f -> f.id == id } ?: false
    }

//    private suspend fun nameExists(name: String): Boolean {
//        // TODO: Duplicated name?
//        return col.whereEqualTo("name", name).get().await().size() > 0
//    }

    private fun usernameExists(name: String): Boolean {
        return users.value?.any { f -> f.username == name } ?: false
    }

    private fun referralExists(referral: String): Boolean {
        return users.value?.any { f -> f.referral_code == referral } ?: false
    }

    private fun emailExists(email: String): Boolean {
        return users.value?.any { f -> f.email == email } ?: false
    }

    fun validate(f: User, insert: Boolean = true): String {
        val emailPattern = Regex("[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+")

        var e = ""

        e += if (f.username == "") "- username is required."
        else if (f.username.length < 8) "\n- username is too short."
        else ""

        e += if (f.email == "") "\n- email is required."
        else if (!f.email.matches(emailPattern)) "\n- email format is invalid."
        else if (emailExists(f.email)) "\n- emails has been used."
        else ""

        if(f.referral_code !="")
        {
            e += if (!referralExists(f.referral_code)) "\n- invalid referral code."
            else ""
        }

        return e
    }
}
