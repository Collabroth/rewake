package com.codebroth.rewake.core.di

import android.content.Context
import androidx.room.Room
import com.codebroth.rewake.core.data.local.RewakeDatabase
import com.codebroth.rewake.reminder.data.local.ReminderDao
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
    fun provideDatabase(
        @ApplicationContext context: Context
    ): RewakeDatabase = Room.databaseBuilder(
        context,
        RewakeDatabase::class.java,
        "rewake.db"
    )
        .fallbackToDestructiveMigration(false)
        .build()

    @Provides
    fun provideRemindersDao(db: RewakeDatabase): ReminderDao = db.reminderDao()

    @Provides
    fun provideAlarmsDao(db: RewakeDatabase) = db.alarmDao()
}