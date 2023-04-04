package ie.wit.carerpatient.ui.profile

import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.auth.User
import ie.wit.carerpatient.R
import ie.wit.carerpatient.firebase.FirebaseDBManager
import ie.wit.carerpatient.firebase.FirebaseImageManager
import ie.wit.carerpatient.models.CarerPatientModel
import ie.wit.carerpatient.models.UserModel
import timber.log.Timber


class ProfileViewModel : ViewModel() {

    private val currentUserData = MutableLiveData<User?>()

    val user =
        MutableLiveData<UserModel>()

    val observableUser: LiveData<UserModel>
        get() = user
    //on class init
    //init {
     //   setUser(null)
  //  }

    //fun setUser(currentUser:User?){
    //   this.currentUserData.value = currentUser
   // }

    //fun getUser():LiveData<User?> = currentUserData

    fun deleteAccount(userId: String){
        try {
            FirebaseDBManager.deleteUser(userId)
        }catch (e: Exception) {
            Timber.i("Error : $e.message")
        }
    }

    fun addProfile(firebaseUser: MutableLiveData<FirebaseUser>,
                    ) {
          try {
            //DonationManager.create(donation)
           // medicine.profilepic = FirebaseImageManager.imageUri.value.toString()
            FirebaseDBManager.getUser(firebaseUser, user)
            true
        } catch (e: IllegalArgumentException) {
            false
        }
    }

   // fun updateProfile(userid:String) {
    //    try {
     //       //DonationManager.update(email, id, donation)
       //     FirebaseDBManager.update(userid)
       //     Timber.i("Detail update() Success : $userid")
       // }
       // catch (e: Exception) {
        //    Timber.i("Detail update() Error : $e.message")
        //}
    //}

}





