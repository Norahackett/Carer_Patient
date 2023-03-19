package ie.wit.carerpatient.utils

import android.app.Dialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.text.format.DateFormat
import android.widget.TimePicker
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import ie.wit.carerpatient.ui.medicine.MedicineViewModel
import java.util.*

class TimePickerHelper : DialogFragment(), TimePickerDialog.OnTimeSetListener {

    //-------------------------------| show time picker dialog | ----------------------------------
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val c = Calendar.getInstance()
        return TimePickerDialog(requireActivity(),this,c.get(Calendar.HOUR_OF_DAY),c.get(Calendar.MINUTE), DateFormat.is24HourFormat(context))
    }
    //=============================================================================================


    //-------------------------------| handle set time and setup it into view model |----------------------------------
    override fun onTimeSet(p0: TimePicker?, p1: Int, p2: Int) {
        val timeViewModel : MedicineViewModel = ViewModelProvider(requireActivity()).get(MedicineViewModel::class.java)
        val c = Calendar.getInstance()
        c.set(2000,1,1,p1,p2)
        timeViewModel.setTime(c.timeInMillis)
    }
    //=================================================================================================================


}