package ie.wit.carerpatient.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ie.wit.carerpatient.R
import ie.wit.carerpatient.databinding.CardMedicineBinding
import ie.wit.carerpatient.models.CarerPatientModel

interface CarerPatientClickListener {
    fun onCarerPatientClick(medicine: CarerPatientModel)
}

class CarerPatientAdapter constructor(private var medicines: ArrayList<CarerPatientModel>,
                                  private val listener: CarerPatientClickListener ,
                                  private val readOnly: Boolean)
    : RecyclerView.Adapter<CarerPatientAdapter.MainHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainHolder {
        val binding = CardMedicineBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)

        return MainHolder(binding,readOnly)
    }

    override fun onBindViewHolder(holder: MainHolder, position: Int) {
        val medicine = medicines[holder.adapterPosition]
        holder.bind(medicine,listener)
    }

    fun removeAt(position: Int) {
        medicines.removeAt(position)
        notifyItemRemoved(position)
    }

    override fun getItemCount(): Int = medicines.size

    inner class MainHolder(val binding : CardMedicineBinding, private val readOnly : Boolean) :
        RecyclerView.ViewHolder(binding.root) {

        val readOnlyRow = readOnly

        fun bind(medicine: CarerPatientModel, listener: CarerPatientClickListener) {
            binding.root.tag = medicine
            binding.medicine = medicine
            binding.imageIcon.setImageResource(R.mipmap.ic_launcher_round)
            binding.root.setOnClickListener { listener.onCarerPatientClick(medicine) }
            binding.executePendingBindings()
        }
    }
}



