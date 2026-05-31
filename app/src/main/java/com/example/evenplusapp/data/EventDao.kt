package com.example.evenplusapp.data

import androidx.room.*

@Dao
interface EventDao {

    @Insert
    fun insertEvent(event: Event)

    @Update
    fun updateEvent(event: Event)

    @Delete
    fun deleteEvent(event: Event)

    @Query("SELECT * FROM events")
    fun  getAllEvents(): List<Event>  // Асинхронный запрос, возвращающий список событий

    @Query("SELECT * FROM events WHERE id = :eventId")
    fun getEventById(eventId: Int): Event  // Асинхронный запрос для получения события по ID
}
