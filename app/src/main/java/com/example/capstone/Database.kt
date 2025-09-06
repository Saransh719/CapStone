package com.example.capstone

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Delete
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.RoomDatabase


@Entity
data class Item(
    @PrimaryKey val id : Int,
    val title: String,
    val description: String,
    val price : String,
    val image : String,
    val category: String
)

@Dao
interface ItemsDao {
    @Query("Select * from Item")
    fun getReadings() : LiveData<List<Item>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(items: List<Item>)
}

@Database(entities = [Item::class], version = 1, exportSchema = false)
abstract class MenuItems : RoomDatabase(){
    abstract fun ItemsDao() : ItemsDao
}