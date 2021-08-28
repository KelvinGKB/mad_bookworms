package com.mad.mad_bookworms.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.firebase.firestore.Blob

@Entity(tableName = "user_table")
data class User_Table(

    @PrimaryKey()
    var id:String = "",

    @ColumnInfo
    var username : String = "",

    @ColumnInfo
    var email : String = "",

    @ColumnInfo
    var level : String = "Silver",

    @ColumnInfo
    var role : String = "normal",

    @ColumnInfo
    var earn_points  : Int = 0,

    @ColumnInfo
    var usable_points  : Int = 0,

    @ColumnInfo
    var referral_code : String = "",

    @ColumnInfo
    var referred_by : String = "",

//    @ColumnInfo
//    var photo: Blob = Blob.fromBytes(ByteArray(0)),

    )

