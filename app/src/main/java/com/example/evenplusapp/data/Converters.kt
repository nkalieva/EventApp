package com.example.evenplusapp.data

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Converters {

    // Конвертация List<String> в строку (для хранения в базе данных)
    @TypeConverter
    fun fromParticipantsList(value: List<String>?): String? {
        val gson = Gson()
        return gson.toJson(value)
    }

    // Конвертация строки обратно в List<String> (при извлечении из базы данных)
    @TypeConverter
    fun toParticipantsList(value: String?): List<String>? {
        val gson = Gson()
        val listType = object : TypeToken<List<String>>() {}.type
        return gson.fromJson(value, listType)
    }
}
