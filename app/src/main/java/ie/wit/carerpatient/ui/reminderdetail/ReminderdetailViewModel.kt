package ie.wit.carerpatient.ui.reminderdetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ie.wit.carerpatient.firebase.FirebaseDBManager
import ie.wit.carerpatient.models.ReminderModel
import timber.log.Timber


class ReminderdetailViewModel : ViewModel() {
    private val Reminder = MutableLiveData<ReminderModel>()

    var observableReminder: LiveData<ReminderModel>
        get() = Reminder
        set(value) {Reminder.value = value.value}

    fun getReminder(userid: String, id: String) {
        try {
            //DonationManager.findById(email, id, donation)
            FirebaseDBManager.findByIdReminder(userid, id, Reminder)
            Timber.i(
                "ReminderDetail getReminder() Success : ${
                    Reminder.value.toString()
                }"
            )
        } catch (e: Exception) {
            Timber.i("ReminderDetail getReminder() Error : $e.message")
        }
    }

    fun updateReminder(userid:String, id: String,Reminder: ReminderModel) {
        try {
            //DonationManager.update(email, id, donation)
            FirebaseDBManager.updateReminder(userid, id, Reminder)
            Timber.i("ReminderDetail updateReminder() Success : $Reminder")
        }
        catch (e: Exception) {
            Timber.i("ReminderDetail updateReminder() Error : $e.message")
        }
    }
}
