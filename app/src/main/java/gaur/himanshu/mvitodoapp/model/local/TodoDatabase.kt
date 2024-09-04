package gaur.himanshu.mvitodoapp.model.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Todo::class], version = 1)
abstract class TodoDatabase : RoomDatabase() {

    companion object {
        fun getInstance(context: Context) =
            Room.databaseBuilder(context, TodoDatabase::class.java, "todo")
                .build()
    }

    abstract fun getTodoDao(): TodoDao

}