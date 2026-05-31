package com.example.evenplusapp

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.evenplusapp.data.Event
import android.widget.Button
import java.util.*
import android.widget.ImageButton


class AddEventActivity : AppCompatActivity() {
    private var eventToEdit: Event? = null  // Переменная для хранения события, которое редактируется

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_event)

        // Получаем событие из Intent, если оно передано для редактирования
        eventToEdit = intent.getSerializableExtra("event") as? Event

        val etEventDate = findViewById<EditText>(R.id.etEventDate)
        val saveButton = findViewById<Button>(R.id.btnSaveEvent)

        // Устанавливаем слушатель для выбора даты
        etEventDate.setOnClickListener {
            showDatePickerDialog(etEventDate)
        }

        saveButton.setOnClickListener {
            onSaveEventClick() // Вызов метода
        }

        // Если событие передано, заполняем поля данными этого события
        eventToEdit?.let {
            findViewById<EditText>(R.id.etEventName).setText(it.eventName)
            findViewById<EditText>(R.id.etEventDescription).setText(it.description)
            etEventDate.setText(it.date)
            findViewById<EditText>(R.id.etEventLocation).setText(it.location)
        }
        val btnBack: ImageButton = findViewById(R.id.btnBack)
        btnBack.setOnClickListener {
            onBackPressed()  // Возврат на предыдущую активность
        }
    }

    // Метод, чтобы показать диалог выбора даты
    private fun showDatePickerDialog(editText: EditText) {
        try {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            val datePickerDialog = DatePickerDialog(
                this,
                { _, selectedYear, selectedMonth, selectedDay ->
                    // Форматируем выбранную дату и устанавливаем в поле
                    val formattedDate = "${selectedDay}/${selectedMonth + 1}/$selectedYear"
                    editText.setText(formattedDate)
                },
                year,
                month,
                day
            )
            datePickerDialog.show()
        } catch (e: Exception) {
            // Логирование и уведомление об ошибке
            Toast.makeText(this, "Failed to load date picker", Toast.LENGTH_SHORT).show()
            e.printStackTrace()
        }
    }

    // Метод, который будет вызван по нажатию на кнопку
    fun onSaveEventClick() {
        try {
            val eventName = findViewById<EditText>(R.id.etEventName).text.toString().trim()
            val eventDescription = findViewById<EditText>(R.id.etEventDescription).text.toString().trim()
            val eventDate = findViewById<EditText>(R.id.etEventDate).text.toString().trim()
            val eventLocation = findViewById<EditText>(R.id.etEventLocation).text.toString().trim()

            // Проверка на пустые поля
            if (eventName.isEmpty()) {
                showErrorMessage("Event name cannot be empty")
                return
            }
            if (eventDescription.isEmpty()) {
                showErrorMessage("Event description cannot be empty")
                return
            }
            if (eventDate.isEmpty()) {
                showErrorMessage("Please select a valid date")
                return
            }
            if (eventLocation.isEmpty()) {
                showErrorMessage("Event location cannot be empty")
                return
            }
            // Проверка формата даты (опционально, если нужна валидация)
            if (!isDateValid(eventDate)) {
                showErrorMessage("Bitte geben Sie das richtige Format ein DD/MM/YYYY")
                return
            }

            // Создаем новый объект события
            val updatedEvent = Event(
                id = eventToEdit?.id ?: 0, // Если редактируем, сохраняем ID
                eventName = eventName,
                description = eventDescription,
                date = eventDate,
                location = eventLocation,
                participants = eventToEdit?.participants ?: mutableListOf() // Сохраняем участников
            )
            // Возвращаем результат с обновленным событием
            val resultIntent = Intent()
            resultIntent.putExtra("event", updatedEvent)
            setResult(RESULT_OK, resultIntent)
            finish()
        } catch (e: Exception) {
            // Обработка непредвиденных ошибок
            Toast.makeText(this, "An error occurred while saving the event", Toast.LENGTH_LONG).show()
            e.printStackTrace()
        }
    }

    // Метод для отображения сообщения об ошибке
    private fun showErrorMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    // Метод для проверки валидности даты
    private fun isDateValid(date: String): Boolean {
        val datePattern = Regex("\\d{1,2}/\\d{1,2}/\\d{4}") // Формат DD/MM/YYYY
        return datePattern.matches(date)
    }
}
