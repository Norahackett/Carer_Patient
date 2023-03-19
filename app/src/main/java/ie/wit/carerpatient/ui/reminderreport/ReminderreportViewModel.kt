package ie.wit.carerpatient.ui.reminderreport

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseUser
import ie.wit.carerpatient.firebase.FirebaseDBManager
import ie.wit.carerpatient.models.ReminderModel

import timber.log.Timber



class ReminderreportViewModel : ViewModel() {

    private val remindersList = MutableLiveData<List<ReminderModel>>()

    var readOnly = MutableLiveData(false)

    val observableRemindersList: LiveData<List<ReminderModel>>
        get() = remindersList

    var liveFirebaseUser = MutableLiveData<FirebaseUser>()

    init {
        load()
    }

    fun load() {
        try {
            readOnly.value = false
            FirebaseDBManager.findAllReminder(liveFirebaseUser.value?.uid!!, remindersList)

            Timber.i("Report Load Success : ${remindersList.value.toString()}")
        } catch (e: Exception) {
            Timber.i("Report Load Error : $e.message")
        }
    }

    fun loadAll() {
        try {
            readOnly.value = true
            FirebaseDBManager.findAllReminder(remindersList)
            Timber.i("Report LoadAll Success : ${remindersList.value.toString()}")
        } catch (e: Exception) {
            Timber.i("Report LoadAll Error : $e.message")
        }
    }

    fun delete(userid: String, id: String) {
        try {
            //DonationManager.delete(userid,id)
            FirebaseDBManager.delete(userid,id)
            Timber.i("Report Delete Success")
        }
        catch (e: Exception) {
            Timber.i("Report Delete Error : $e.message")
        }
    }
}