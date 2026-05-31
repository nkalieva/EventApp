package com.example.evenplusapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.evenplusapp.data.Event
import android.widget.ImageButton

class EventAdapter(
    private val eventList: MutableList<Event>,  // Список событий
    private val onEventClick: (Event) -> Unit,   // Обработчик нажатия на событие для редактирования
    private val onDeleteClick: (Event) -> Unit   // Обработчик нажатия на кнопку удаления
) : RecyclerView.Adapter<EventAdapter.EventViewHolder>() {
    // ViewHolder для каждого элемента в RecyclerView
    inner class EventViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val eventName: TextView = itemView.findViewById(R.id.eventName)  // Название события
        val deleteButton: ImageButton = itemView.findViewById(R.id.btnDeleteEvent)  // Кнопка удаления
        val editButton: ImageButton = itemView.findViewById(R.id.btnEditEvent)  // Кнопка редактирования
        // Метод для связывания данных с элементами интерфейса
        fun bind(event: Event) {
            eventName.text = event.eventName  //  название события
            deleteButton.setOnClickListener {
                onDeleteClick(event)  // Вызов метода удаления
            }
            // Устанавливаем обработчик на кнопку редактирования
            editButton.setOnClickListener {
                onEventClick(event)  // Вызов метода редактирования
            }
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_event, parent, false)
        return EventViewHolder(view)  // Возвращаем ViewHolder
    }

    // Привязка данных события к соответствующему ViewHolder
    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        val event = eventList[position]
        holder.bind(event)  // Связываем данные события с элементом интерфейса
        holder.itemView.setOnClickListener {
            onEventClick(event)  // Вызываем функцию для обработки клика
        }
    }

    // Получаем количество элементов в списке
    override fun getItemCount(): Int = eventList.size

    // Метод для обновления списка событий в адаптере
    fun updateEventList(newEventList: List<Event>) {
        eventList.clear()  // Очищаем текущий список
        eventList.addAll(newEventList)  // Добавляем новые события
        notifyDataSetChanged()  // Уведомляем адаптер о том, что данные изменились
    }

    // Метод для удаления события из списка
    fun removeEvent(event: Event) {
        val position = eventList.indexOf(event)  // Находим позицию события в списке
        if (position >= 0) {
            eventList.removeAt(position)  // Удаляем событие
            notifyItemRemoved(position)  // Уведомляем адаптер, что элемент был удален
        }
    }
}
