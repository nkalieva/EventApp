package com.example.evenplusapp.data

import android.content.Context
import androidx.room.Room

object DatabaseInstance {
    // Переменная для хранения базы данных
    @Volatile
    private var INSTANCE: AppDatabase? = null

    // Метод для получения экземпляра базы данных
    fun getDatabase(context: Context): AppDatabase {
        return INSTANCE ?: synchronized(this) { // Блок synchronized гарантирует потокобезопасность
            val instance = Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java , // Ссылка на ваш класс базы данных
                "event_database" // Имя файла базы данных
            ).build()
            INSTANCE = instance
            instance
        }
    }
}
