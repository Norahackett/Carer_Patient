package ie.wit.carerpatient.ui.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ie.wit.carerpatient.firebase.FirebaseDBManager
import ie.wit.carerpatient.models.CarerPatientModel
import timber.log.Timber



class ProfileViewModel : ViewModel() {
    private val medicine = MutableLiveData<CarerPatientModel>()

    var observableMedicine: LiveData<CarerPatientModel>
        get() = medicine
        set(value) {medicine.value = value.value}

    fun getMedicine(userid: String, id: String) {
        try {
            //DonationManager.findById(email, id, donation)
            FirebaseDBManager.findById(userid, id, medicine)
            Timber.i(
                "Profile getMedicine() Success : ${
                    medicine.value.toString()
                }"
            )
        } catch (e: Exception) {
            Timber.i("Profile getMedicine() Error : $e.message")
        }
    }

    fun updateMedicine(userid:String, id: String,medicine: CarerPatientModel) {
        try {
            //DonationManager.update(email, id, donation)
            FirebaseDBManager.update(userid, id, medicine)
            Timber.i("Profile update() Success : $medicine")
        }
        catch (e: Exception) {
            Timber.i("Profile update() Error : $e.message")
        }
    }
}
