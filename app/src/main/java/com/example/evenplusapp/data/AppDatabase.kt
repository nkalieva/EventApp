package com.example.evenplusapp.data
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [Event::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class) // Используем класс конвертеров
abstract class AppDatabase : RoomDatabase() {
    abstract fun eventDao(): EventDao
}
