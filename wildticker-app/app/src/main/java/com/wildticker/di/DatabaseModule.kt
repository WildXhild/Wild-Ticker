package com.wildticker.di

import android.content.Context
import androidx.room.Room
import com.wildticker.data.AppDatabase
import com.wildticker.data.NewsDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext ctx: Context): AppDatabase =
        Room.databaseBuilder(ctx, AppDatabase::class.java, "wildticker-db").build()

    @Provides
    fun provideNewsDao(db: AppDatabase): NewsDao = db.newsDao()
}
