package ie.wit.carerpatient.ui.appointmentdetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ie.wit.carerpatient.firebase.FirebaseDBManager
import ie.wit.carerpatient.models.AppointmentModel

import timber.log.Timber



class AppointmentDetailViewModel : ViewModel() {
    private val appointment = MutableLiveData<AppointmentModel>()

    var observableAppointment: LiveData<AppointmentModel>
        get() = appointment
        set(value) {appointment.value = value.value}

    fun getAppointment(userid: String, id: String) {
        try {
            //DonationManager.findById(email, id, donation)
            FirebaseDBManager.findByIdAppointment(userid, id, appointment)
            Timber.i(
                "Detail getMedicine() Success : ${
                    appointment.value.toString()
                }"
            )
        } catch (e: Exception) {
            Timber.i("Detail getMedicine() Error : $e.message")
        }
    }

    fun updateAppointment(userid:String, id: String,appointment: AppointmentModel) {
        try {
            //DonationManager.update(email, id, donation)
            FirebaseDBManager.updateAppointment(userid, id, appointment)
            Timber.i("Detail update() Success : $appointment")
        }
        catch (e: Exception) {
            Timber.i("Detail update() Error : $e.message")
        }
    }
}
