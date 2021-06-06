package com.example.mytodoapp.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.Room.databaseBuilder
import androidx.room.RoomDatabase
import com.example.mytodoapp.MainActivity

@Database(entities = [Todo::class], version = 1, exportSchema = true)
abstract class TodoListDatabase : RoomDatabase() {

    abstract fun getTodoDao(): TodoDao

    companion object {
        val dbName = "todo_items"
        @Volatile var INSTANCE: TodoListDatabase? = null

        fun getInstance(context: Context): TodoListDatabase {

            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    TodoListDatabase::class.java,
                    dbName
                ).allowMainThreadQueries().build()
                INSTANCE = instance
                instance
            }
        }
    }
}





//            if (TodoListDatabase == null){
//                TodoListDatabase = Room.databaseBuilder(context,
//                    TodoListDatabase::class.java,
//                    dbName)
//                    .allowMainThreadQueries()
//                    .build()
//            }
