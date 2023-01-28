package ie.wit.carerpatient.ui.report

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseUser
import ie.wit.carerpatient.firebase.FirebaseDBManager
import ie.wit.carerpatient.models.CarerPatientModel
import timber.log.Timber


class ReportViewModel : ViewModel() {

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
            FirebaseDBManager.findAll(liveFirebaseUser.value?.uid!!,medicinesList)

            Timber.i("Report Load Success : ${medicinesList.value.toString()}")
        }
        catch (e: Exception) {
            Timber.i("Report Load Error : $e.message")
        }
    }
    fun delete(userid: String, id: String) {
        try {
            FirebaseDBManager.delete(userid,id)
            Timber.i("Report Delete Success")
        }
        catch (e: java.lang.Exception) {
            Timber.i("Report Delete Error : $e.message")
        }
    }

    fun loadAll() {
        try {
            readOnly.value = true
            FirebaseDBManager.findAll(medicinesList)
            Timber.i("Report LoadAll Success : ${medicinesList.value.toString()}")
        }
        catch (e: Exception) {
            Timber.i("Report LoadAll Error : $e.message")
        }
    }
}
