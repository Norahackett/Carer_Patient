package ie.wit.carerpatient.adapters

import android.view.LayoutInflater


import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView


import ie.wit.carerpatient.databinding.CardReminderBinding
import ie.wit.carerpatient.models.ReminderModel


interface ReminderClickListener {
    fun onReminderClick(Reminder: ReminderModel)
}

class ReminderAdapter constructor(private var reminders: ArrayList<ReminderModel>,
                                      private val listener: ReminderClickListener ,
                                      private val readOnly: Boolean)
    : RecyclerView.Adapter<ReminderAdapter.MainHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainHolder {
        val binding = CardReminderBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)

        return MainHolder(binding,readOnly)
    }

    override fun onBindViewHolder(holder: MainHolder, position: Int) {
        val reminder = reminders[holder.adapterPosition]
        holder.bind(reminder,listener)
    }

    fun removeAt(position: Int) {
        reminders.removeAt(position)
        notifyItemRemoved(position)
    }

    override fun getItemCount(): Int = reminders.size

    inner class MainHolder(val binding : CardReminderBinding, private val readOnly : Boolean) :
        RecyclerView.ViewHolder(binding.root) {

        val readOnlyRow = readOnly

        fun bind(reminder: ReminderModel, listener: ReminderClickListener) {
            binding.root.tag = reminder
            binding.reminder = reminder
            binding.root.setOnClickListener { listener.onReminderClick(reminder) }
            binding.executePendingBindings()
        }
    }
}



