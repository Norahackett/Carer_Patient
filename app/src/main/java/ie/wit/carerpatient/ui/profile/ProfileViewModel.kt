package ie.wit.carerpatient.ui.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.auth.User

import ie.wit.carerpatient.firebase.FirebaseDBManager
import ie.wit.carerpatient.models.CarerPatientModel
import timber.log.Timber



class ProfileViewModel : ViewModel() {
    private val currentUserData = MutableLiveData<CarerPatientModel>()

    //on class init
    init {
       // setUser(null)
    }

    fun setUser(currentUser: CarerPatientModel) {
        this.currentUserData.value = currentUser
    }

    fun getUser():LiveData<CarerPatientModel?> = currentUserData

}