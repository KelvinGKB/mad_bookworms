package com.mad.mad_bookworms.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface LocalDao {

    @Insert
    fun insertUser(u : User_Table)

    @Query("Select * From user_table")
    fun getAll() : List<User_Table>

    @Query("Select * From user_table Where id = :id")
    fun getUser(id:String) : List<User_Table>

//    @Query("Delete From user_table")
//    suspend fun deleteAll()

//    @Query(" Delete From user_table WHERE id = :id")
//    suspend fun deleteUser(id:String)
//
//    @Delete
//    fun deleteUser(u : User_Table)
//
//    @Query("Select * From user_table Where price < :priceRange")
//    fun getPriceLessThan(priceRange:Double) : List<Product>
}