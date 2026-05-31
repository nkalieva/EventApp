package com.example.evenplusapp

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.evenplusapp.data.DatabaseInstance
import com.example.evenplusapp.data.Event
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import android.util.Log

class MainActivity : AppCompatActivity() {
    private val eventList = mutableListOf<Event>()
    private lateinit var eventAdapter: EventAdapter

    private val addEventResultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val data = result.data
                val updatedEvent: Event? = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
                    data?.getSerializableExtra("event", Event::class.java)
                } else {
                    @Suppress("DEPRECATION")
                    data?.getSerializableExtra("event") as? Event
                }

                updatedEvent?.let { event ->
                    // Проверяем, существует ли событие в списке (по ID)
                    val existingIndex = eventList.indexOfFirst { it.id == event.id }
                    if (existingIndex != -1) {
                        // Если событие уже есть, обновляем его
                        updateEventInDatabase(event)
                    } else {
                        // Если это новое событие, добавляем его
                        saveEventToDatabase(event)
                    }
                }
            }
        }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Настройка RecyclerView
        val recyclerView: RecyclerView = findViewById(R.id.recyclerViewEvents)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Передаем оба обработчика (для редактирования и удаления)
        eventAdapter = EventAdapter(eventList, { event ->
            // Обработка клика для редактирования события
            val intent = Intent(this, AddEventActivity::class.java)
            intent.putExtra("event", event) // Передаем событие для редактирования
            addEventResultLauncher.launch(intent)
        }, { event ->
            // Обработка клика на кнопку удаления события
            deleteEventFromDatabase(event)
        })

        recyclerView.adapter = eventAdapter

        // Загрузка событий из базы данных
        loadEventsFromDatabase()

        // Кнопка добавления события
        val fabAddEvent: FloatingActionButton = findViewById(R.id.fabAddEvent)
        fabAddEvent.setOnClickListener {
            val intent = Intent(this, AddEventActivity::class.java)
            addEventResultLauncher.launch(intent) // Запуск активности для добавления события
        }
    }


    private fun loadEventsFromDatabase() {
        val database = DatabaseInstance.getDatabase(this)
        val eventDao = database.eventDao()

        lifecycleScope.launch(Dispatchers.IO) {
            val events = eventDao.getAllEvents()

            // Логируем все события, которые были загружены из базы данных
            Log.d("Database", "Loaded events: $events")

            runOnUiThread {
                eventList.clear()
                eventAdapter.notifyDataSetChanged()

                events.forEachIndexed { index, event ->
                    eventList.add(event)
                    eventAdapter.notifyItemInserted(index)

                    // Логируем каждый добавленный элемент в список
                    Log.d("Database", "Event loaded: ${event.eventName}, ${event.date}, ${event.location}")
                }
            }
        }
    }


    private fun saveEventToDatabase(event: Event) {
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val database = DatabaseInstance.getDatabase(this@MainActivity)
                val eventDao = database.eventDao()

                // Вставка события в базу данных
                eventDao.insertEvent(event)

                Log.d("Database", "Event saved: ${event.eventName}, ${event.description}, ${event.date}, ${event.location}")

                // Обновляем UI после сохранения
                runOnUiThread {
                    eventList.add(event)
                    eventAdapter.notifyItemInserted(eventList.size - 1)
                }
            } catch (e: Exception) {
                Log.e("Database", "Error saving event", e)
                runOnUiThread {
                    Toast.makeText(this@MainActivity, "Error saving event", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun updateEventInDatabase(updateEvent: Event) {
        lifecycleScope.launch(Dispatchers.IO) {
            val database = DatabaseInstance.getDatabase(this@MainActivity)
            val eventDao = database.eventDao()

            // Обновляем событие в базе данных
            eventDao.updateEvent(updateEvent)
            Log.d("Database", "Event updated: ${updateEvent.eventName}, ${updateEvent.date}, ${updateEvent.location}, ID: ${updateEvent.id}")
            // Обновляем UI
            runOnUiThread {
                val position = eventList.indexOfFirst { it.id == updateEvent.id }
                if (position != -1) {
                    eventList[position] = updateEvent // Обновляем элемент списка
                    eventAdapter.notifyItemChanged(position) // Уведомляем адаптер
                }
            }
        }
    }



    private fun deleteEventFromDatabase(event: Event) {
        lifecycleScope.launch(Dispatchers.IO) {
            val database = DatabaseInstance.getDatabase(this@MainActivity)
            val eventDao = database.eventDao()

            // Удаляем событие из базы данных
            eventDao.deleteEvent(event)

            // Логируем удаление
            Log.d("Database", "Event deleted: ${event.eventName}, ${event.date}, ${event.location}")

            // Удаляем событие из списка и обновляем RecyclerView
            runOnUiThread {
                removeEventFromList(event)
            }
        }
    }

    private fun removeEventFromList(event: Event) {
        val position = eventList.indexOfFirst { it.id == event.id }
        if (position != -1) {
            eventList.removeAt(position)
            eventAdapter.notifyItemRemoved(position)
        }
    }

}
