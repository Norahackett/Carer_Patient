package ie.wit.carerpatient.ui.report

import android.app.AlarmManager
import android.app.Application
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.CountDownTimer
import android.os.SystemClock
import androidx.core.app.AlarmManagerCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.*
import com.google.firebase.auth.FirebaseUser
import ie.wit.carerpatient.R
import ie.wit.carerpatient.firebase.FirebaseDBManager
import ie.wit.carerpatient.models.CarerPatientModel
import ie.wit.carerpatient.utils.AlarmReceiver
import ie.wit.carerpatient.utils.cancelNotifications
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber


class ReportViewModel  : ViewModel() {


    private val medicinesList = MutableLiveData<List<CarerPatientModel>>()


    var readOnly = MutableLiveData(false)

    val observableMedicinesList: LiveData<List<CarerPatientModel>>
        get() = medicinesList

    var liveFirebaseUser = MutableLiveData<FirebaseUser>()

    init {

        load()
    }

    fun load() {
        try {
            // FirebaseDBManager.findAll(liveFirebaseUser.value?.uid!!, medicinesList)
            readOnly.value = false
            FirebaseDBManager.findAll(liveFirebaseUser.value?.uid!!, medicinesList)

            Timber.i("Report Load Success : ${medicinesList.value.toString()}")
        } catch (e: Exception) {
            Timber.i("Report Load Error : $e.message")
        }
    }

    fun loadAll() {
        try {
            readOnly.value = true
            FirebaseDBManager.findAll(medicinesList)
            Timber.i("Report LoadAll Success : ${medicinesList.value.toString()}")
        } catch (e: Exception) {
            Timber.i("Report LoadAll Error : $e.message")
        }
    }

    fun delete(userid: String, id: String) {
        try {
            //DonationManager.delete(userid,id)
            FirebaseDBManager.delete(userid, id)
            Timber.i("Report Delete Success")
        } catch (e: Exception) {
            Timber.i("Report Delete Error : $e.message")
        }
    }

}