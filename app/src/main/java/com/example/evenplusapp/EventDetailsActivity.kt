package com.example.evenplusapp

import android.os.Build
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.evenplusapp.data.Event

class EventDetailsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_event_details)

        // Получаем объект события с учетом версии API
        val event: Event? = getEventFromIntent()

        // Проверяем, получено ли событие, и отображаем информацию
        event?.let {
            displayEventDetails(it)
        } ?: run {
            // Если событие не передано или не найдено, показываем ошибку
            showError("Event not found")
        }
    }

    // Функция для получения объекта события с учетом версии API
    private fun getEventFromIntent(): Event? {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getSerializableExtra("event", Event::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getSerializableExtra("event") as? Event
        }
    }

    // Функция для отображения деталей события
    private fun displayEventDetails(event: Event) {
        val eventNameTextView: TextView = findViewById(R.id.tvEventName)
        val eventDescriptionTextView: TextView = findViewById(R.id.tvEventDescription)
        val eventDateTextView: TextView = findViewById(R.id.tvEventDate)
        val eventLocationTextView: TextView = findViewById(R.id.tvEventLocation)

        // Отображаем информацию о событии
        eventNameTextView.text = event.eventName
        eventDescriptionTextView.text = event.description
        eventDateTextView.text = event.date
        eventLocationTextView.text = event.location
    }

    // Функция для отображения ошибки, если событие не найдено
    private fun showError(message: String) {

    }
}
