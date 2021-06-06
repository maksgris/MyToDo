package com.example.mytodoapp.db

import androidx.room.*
import java.util.List

@Dao
interface TodoDao {

    @Query("SELECT * FROM todo_items")
    fun getAll(): List<Todo>

    @Query("SELECT * FROM todo_items WHERE name LIKE :name")
    fun findByName(name: String): Todo

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(todo:Todo)

    @Delete
    fun deleteTodo(todo:Todo)

    @Update
    fun updateTodo(todo:Todo)
}
