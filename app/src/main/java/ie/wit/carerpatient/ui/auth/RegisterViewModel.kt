package ie.wit.carerpatient.ui.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseUser
import ie.wit.carerpatient.firebase.FirebaseDBManager
import ie.wit.carerpatient.firebase.FirebaseImageManager
import ie.wit.carerpatient.models.CarerPatientModel
import ie.wit.carerpatient.models.User

class RegisterViewModel : ViewModel() {


    private val status = MutableLiveData<Boolean>()
    private val date = MutableLiveData<Long>()
    private val time = MutableLiveData<Long>()

    val observableStatus: LiveData<Boolean>
        get() = status


    fun addUser(firebaseUser: MutableLiveData<FirebaseUser>,
                    user: User) {
        status.value = try {
            //DonationManager.create(donation)
           // medicine.profilepic = FirebaseImageManager.imageUri.value.toString()
            FirebaseDBManager.createUser(firebaseUser,user)
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
