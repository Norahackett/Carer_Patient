package ie.wit.carerpatient.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import ie.wit.carerpatient.databinding.CardAppointmentBinding
import ie.wit.carerpatient.models.AppointmentModel
import ie.wit.carerpatient.utils.customTransformation

interface AppointmentClickListener {
    fun onAppointmentClick(appointment: AppointmentModel)
}

class AppointmentAdaptor constructor(private var appointments: ArrayList<AppointmentModel>,
                                      private val listener: AppointmentClickListener ,
                                      private val readOnly: Boolean)
    : RecyclerView.Adapter<AppointmentAdaptor.MainHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainHolder {
        val binding = CardAppointmentBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)

        return MainHolder(binding,readOnly)
    }

    override fun onBindViewHolder(holder: MainHolder, position: Int) {
        val appointment = appointments[holder.adapterPosition]
        holder.bind(appointment,listener)
    }

    fun removeAt(position: Int) {
        appointments.removeAt(position)
        notifyItemRemoved(position)
    }

    override fun getItemCount(): Int = appointments.size

    inner class MainHolder(val binding : CardAppointmentBinding, private val readOnly : Boolean) :
        RecyclerView.ViewHolder(binding.root) {

        val readOnlyRow = readOnly

        fun bind(appointment: AppointmentModel, listener: AppointmentClickListener) {
            binding.root.tag = appointment
            binding.appointment = appointment
            Picasso.get().load(appointment.profilepic.toUri())
                .resize(200, 200)
                .transform(customTransformation())
                .centerCrop()
                .into(binding.imageIcon)

            binding.root.setOnClickListener { listener.onAppointmentClick(appointment) }
            binding.executePendingBindings()
        }
    }
}
