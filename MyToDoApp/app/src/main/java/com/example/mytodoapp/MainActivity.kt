package com.example.mytodoapp

import android.content.ContentValues
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.room.*
import com.example.mytodoapp.db.Todo
import com.example.mytodoapp.db.TodoListDatabase
import java.sql.Date
import java.sql.Time
import java.text.SimpleDateFormat
import java.time.LocalDateTime

class MainActivity : AppCompatActivity() {

    /*@Entity(tableName = "todo_items")
    data class todoEntity(
        @PrimaryKey(autoGenerate = true)
        val id: Int = 0,
        @ColumnInfo(name = "name") var name: String,
        @ColumnInfo(name = "date") var date: Long
    )

    @Dao
    interface TodoDao {
        @Query("SELECT * FROM todo_items")
        fun getAll(): List<todoEntity>

        @Query("SELECT * FROM todo_items WHERE name LIKE :name")
        fun findByName(name: String): todoEntity

        @Insert(onConflict = OnConflictStrategy.IGNORE)
        fun insertAll(todo: todoEntity)

        @Delete
        fun deleteTodo(todo: todoEntity)

        @Update
        fun updateTodo(todo: todoEntity)
    }

    @Database(entities = [todoEntity::class], version = 1, exportSchema = true)
    abstract class todoListDatebase : RoomDatabase() {
        abstract fun getTodoDao(): TodoDao

        companion object {
            val dbName = "todo_items"
            var todoListDatebase: todoListDatebase? = null

            fun getInstance(context: Context): todoListDatebase?{
                if (todoListDatebase == null){
                    todoListDatebase = Room.databaseBuilder(context,
                        todoListDatebase!!::class.java,
                        dbName)
                        .allowMainThreadQueries()
                        .build()
                }
                return todoListDatebase
            }
        }
    }*/

    val sdf = SimpleDateFormat("dd/M/yyyy hh:mm:ss")

    var TodoDatabase: TodoListDatabase? = null

    private val tasks = mutableListOf<Todo>()
    private val names = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        TodoDatabase = TodoListDatabase.getInstance(this)
        val todo = TodoDatabase!!.getTodoDao().getAll()

        todo.forEach {
            tasks.add(it)
            names.add(it.name)
        }

        val listView: ListView = findViewById(R.id.listView)

        var arrayAdapter: ArrayAdapter<String> =
            ArrayAdapter(this, android.R.layout.simple_list_item_1, names)

        listView.adapter = arrayAdapter

        listView.setOnItemClickListener { parent, view, i, id ->
            val taskEditText = EditText(this)
            taskEditText.setText(tasks[i].name)
            val date = tasks[i].date
            val builder = AlertDialog.Builder(this)
                .setTitle("Edit a task")
                .setMessage("What do you want to do next?")
                .setView(taskEditText)
                .setMessage("Task was created ${sdf.format(date)}")
                .setPositiveButton("Save", DialogInterface.OnClickListener { dialog, which ->
                    val task = taskEditText.text.toString()
                    tasks[i].name = task
                    names[i] = task
                    TodoDatabase!!.getTodoDao().updateTodo(tasks[i])
                })
                .setNegativeButton("Remove", DialogInterface.OnClickListener { dialog, which ->
                    TodoDatabase!!.getTodoDao().deleteTodo(tasks[i])
                    tasks.remove(tasks[i])
                    names.remove(names[i])
                    arrayAdapter.notifyDataSetChanged()
                })
                .setNeutralButton("Cancel", null)
                .create()
            builder.show()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.nav_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_add -> {
                val taskEditText = EditText(this)
                val date = System.currentTimeMillis()
                val builder = AlertDialog.Builder(this)
                    .setTitle("Add a new task")
                    .setMessage("What do you want to do next?")
                    .setView(taskEditText)
                    .setMessage("Task was created ${sdf.format(date)}")
                    .setPositiveButton("Add", DialogInterface.OnClickListener { dialog, which ->
                        val name = taskEditText.text.toString()
                        val newTask: Todo =
                            Todo(0, name, date)
                        tasks.add(newTask)
                        names.add(name)
                        TodoDatabase!!.getTodoDao().insertAll(newTask)
                    })
                    .setNegativeButton("Cancel", null)
                    .create()
                builder.show()

                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }

    }
}