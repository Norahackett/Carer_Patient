package ie.wit.carerpatient.ui.appointment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseUser
import ie.wit.carerpatient.firebase.FirebaseDBManager
import ie.wit.carerpatient.models.AppointmentModel
import timber.log.Timber


class AppointmentViewModel : ViewModel() {


    private val status = MutableLiveData<Boolean>()

    val observableStatus: LiveData<Boolean>
        get() = status


    fun addAppointment(firebaseUser: MutableLiveData<FirebaseUser>,
                    appointment: AppointmentModel) {
        status.value = try {
            //DonationManager.create(donation)
           // appointment.profilepic = FirebaseImageManager.imageUri.value.toString()
            FirebaseDBManager.createAppointment(firebaseUser,appointment)
            Timber.i ("sucessfully added appointment")
            true
        } catch (e: IllegalArgumentException) {
            false
        }
    }
}
