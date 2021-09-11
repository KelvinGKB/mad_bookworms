package com.mad.mad_bookworms.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class MyFavouriteTable (

    @PrimaryKey
    var bookId : String,

    @ColumnInfo
    var uid: String,


)

