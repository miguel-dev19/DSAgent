package com.dsagent.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.dsagent.data.local.dao.*
import com.dsagent.data.local.entity.*

@Database(entities = [ChatEntity::class, MessageEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun chatDao(): ChatDao
    abstract fun messageDao(): MessageDao
}
