package com.mad.mad_bookworms.data

import com.google.firebase.firestore.Blob
import com.google.firebase.firestore.DocumentId

data class Book(
    @DocumentId
    var id: String = "",
    var title: String = "",
    var author: String = "",
    var isTrending: Boolean = false,
    var description: String = "",
    var price: Double = 0.00,
    var requiredPoint: Int = 0,
    var category: String = "",
    var language: String = "",
    var pages: Int = 0,
    //var image: Blob = Blob.fromBytes(ByteArray(0)),
)
