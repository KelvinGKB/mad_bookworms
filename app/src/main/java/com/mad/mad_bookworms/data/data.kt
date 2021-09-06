package com.mad.mad_bookworms.data

import androidx.room.PrimaryKey
import com.google.firebase.firestore.Blob
import com.google.firebase.firestore.DocumentId
import java.util.*
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

// TODO: Specify document id
// TODO: Add date and photo

data class Book(
    @DocumentId
    var id: String = "",
    var title: String = "",
    var author: String = "",
    var trending: Boolean? = null,
    var description: String = "",
    var price: Double = 0.00,
    var requiredPoint: Int = 0,
    var category: String = "",
    var language: String = "",
    var pages: Int = 0,
    var image: Blob = Blob.fromBytes(ByteArray(0)),
)


data class User(
    @DocumentId
    var id   : String = "",
    var username : String = "",
    var email : String = "",
    var level : String = "Silver",
    var role : String = "normal",
    var earn_points  : Int = 0,
    var usable_points  : Int = 0,
    var referral_code : String = "",
    var referred_by : String = "",
    var address: String ="",
    var postal : String = "",
    var state : String = "",
    var city : String = "",
    var fullname : String = "",
    var contact : String = "",
    var photo: Blob = Blob.fromBytes(ByteArray(0)),
)

data class MyVoucher(
    @DocumentId
    var id   : String = "",
    var uid : String = "",
    var discount : Int = 0,
    var expiry_date : Date = Date(),
    var type : String = "",
    var status : String = "",
)

data class Voucher(
    @DocumentId
    var id   : String = "",
    var level : Int = 1,
    var discount : Int = 0,
    var type : String = "",
    var requiredPoint : Int = 0,
)

data class Referral(
    var referred_by: String = "",
    var referred_to : String = "",
)

@Parcelize
data class PendingOrder(
    var bookId: String,
    var quantity: Int): Parcelable


data class PaymentMethod(
    var PaymentName: String,
    var PaymentIcon: Int
)

data class SelectedPayMethod(
    var PaymentMethod: Int
)

