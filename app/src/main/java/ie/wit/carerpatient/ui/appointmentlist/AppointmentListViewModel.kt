package ie.wit.carerpatient.ui.appointmentlist


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseUser
import ie.wit.carerpatient.firebase.FirebaseDBManager
import ie.wit.carerpatient.models.AppointmentModel

import timber.log.Timber


class AppointmentListViewModel : ViewModel() {

    private val appointmentsList = MutableLiveData<List<AppointmentModel>>()

    var readOnly = MutableLiveData(false)

    val observableAppointmentList: LiveData<List<AppointmentModel>>
        get() = appointmentsList

    var liveFirebaseUser = MutableLiveData<FirebaseUser>()

    init {
        load()
    }

    fun load() {
        try {
            // FirebaseDBManager.findAll(liveFirebaseUser.value?.uid!!, medicinesList)
            readOnly.value = false
            FirebaseDBManager.findAllAppointment(liveFirebaseUser.value?.uid!!, appointmentsList)

            Timber.i("Report Load Success : ${appointmentsList.value.toString()}")
        } catch (e: Exception) {
            Timber.i("Report Load Error : $e.message")
        }
    }

    fun loadAll() {
        try {
            readOnly.value = true
            FirebaseDBManager.findAllAppointment(appointmentsList)
            Timber.i("Report LoadAll Success : ${appointmentsList.value.toString()}")
        } catch (e: Exception) {
            Timber.i("Report LoadAll Error : $e.message")
        }
    }

    fun deleteAppointment(userid: String, id: String) {
        try {
            //DonationManager.delete(userid,id)
            FirebaseDBManager.deleteAppointment(userid,id)
            Timber.i("Report Delete Success")
        }
        catch (e: Exception) {
            Timber.i("Report Delete Error : $e.message")
        }
    }
}