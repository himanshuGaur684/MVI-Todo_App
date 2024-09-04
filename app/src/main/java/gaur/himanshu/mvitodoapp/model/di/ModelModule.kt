package gaur.himanshu.mvitodoapp.model.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import gaur.himanshu.mvitodoapp.model.local.TodoDao
import gaur.himanshu.mvitodoapp.model.local.TodoDatabase
import gaur.himanshu.mvitodoapp.model.repository.TodoRepoImpl
import gaur.himanshu.mvitodoapp.model.repository.TodoRepository
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object ModelModule {


    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext context: Context): TodoDatabase {
        return TodoDatabase.getInstance(context)
    }

    @Provides
    fun provideDao(todoDatabase: TodoDatabase): TodoDao {
        return todoDatabase.getTodoDao()
    }


    @Provides
    fun provideRepository(todoDao: TodoDao): TodoRepository {
        return TodoRepoImpl(todoDao)
    }

}