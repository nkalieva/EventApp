package com.example.evenplusapp.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import java.io.Serializable

@Entity(tableName = "events")
data class Event(
    @PrimaryKey(autoGenerate = true) val id: Int = 0, // ID будет генерироваться автоматически
    @ColumnInfo(name = "eventName") var eventName: String, // Изменено на var
    @ColumnInfo(name = "description") var description: String, // Изменено на var
    @ColumnInfo(name = "date") var date: String, // Изменено на var
    @ColumnInfo(name = "location") var location: String, // Изменено на var
    @ColumnInfo(name = "participants") var participants: List<String> // Список участников, изменено на var
) : Serializable
