package ie.wit.carerpatient.ui.medicine

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseUser
import ie.wit.carerpatient.firebase.FirebaseDBManager
import ie.wit.carerpatient.firebase.FirebaseImageManager
import ie.wit.carerpatient.models.CarerPatientModel



class MedicineViewModel : ViewModel() {


    private val status = MutableLiveData<Boolean>()
    private val date = MutableLiveData<Long>()
    private val time = MutableLiveData<Long>()

    val observableStatus: LiveData<Boolean>
        get() = status


    fun addMedicine(firebaseUser: MutableLiveData<FirebaseUser>,
                    medicine: CarerPatientModel) {
        status.value = try {
            //DonationManager.create(donation)
            medicine.profilepic = FirebaseImageManager.imageUri.value.toString()
            FirebaseDBManager.create(firebaseUser,medicine)
            true
        } catch (e: IllegalArgumentException) {
            false
        }
    }

    fun setDate(setDate:Long){
        this.date.value = setDate
    }

    fun getDate():LiveData<Long> = date

    fun setTime(setTime:Long) {
        this.time.value = setTime
    }

    fun getTime():LiveData<Long> = time
}
