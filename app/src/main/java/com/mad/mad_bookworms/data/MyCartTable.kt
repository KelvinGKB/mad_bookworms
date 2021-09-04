package com.mad.mad_bookworms.data


import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity
data class MyCartTable (

    @PrimaryKey (autoGenerate = true)
    var id:Int,

    @ColumnInfo
    var uid: String,

    @ColumnInfo
    var bookId : String,

//    var book : Book,

    @ColumnInfo
    var quantity: Int,


)
