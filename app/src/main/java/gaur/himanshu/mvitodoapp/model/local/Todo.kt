package gaur.himanshu.mvitodoapp.model.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "todo_table")
data class Todo(
    val title: String,
    @PrimaryKey
    val id: Int = 0,
    val isDone: Boolean = false
)
