package com.mad.mad_bookworms.data

import androidx.lifecycle.LiveData
import androidx.room.*

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

@Dao
interface MyCartDao {
    @Query("SELECT * FROM MyCartTable")
    fun getAll(): LiveData<List<MyCartTable>>

    @Query("SELECT * FROM MyCartTable WHERE uid = :uid AND bookId = :bookId")
    suspend fun get(uid: String, bookId: String): List<MyCartTable>

    @Query("UPDATE MyCartTable SET quantity = :qty WHERE bookId = :bookId AND uid = :uid")
    suspend fun updateQty(uid:String, bookId: String, qty: Int)

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(f: MyCartTable) // Long -> row id
    // (fruits: List<Fruit>)  -> List<Long>
    // (vararg fruits: Fruit) -> List<Long>

    @Update
    suspend fun update(f: MyCartTable) // Int -> count

    @Delete
    suspend fun delete(f: MyCartTable) // Int -> count

    @Query("DELETE FROM MyCartTable WHERE bookId = :bookId AND uid = :uid")
    suspend fun deleteCart(uid:String, bookId: String)

//    @Query("DELETE FROM MyCartTable")
//    suspend fun deleteAll()
}