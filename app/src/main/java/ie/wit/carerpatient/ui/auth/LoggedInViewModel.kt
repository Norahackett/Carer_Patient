package ie.wit.carerpatient.ui.auth

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseUser
import ie.wit.carerpatient.firebase.FirebaseAuthManager
import ie.wit.carerpatient.firebase.FirebaseDBManager
import timber.log.Timber


class LoggedInViewModel(app: Application) : AndroidViewModel(app) {

    var firebaseAuthManager : FirebaseAuthManager = FirebaseAuthManager(app)
    var liveFirebaseUser : MutableLiveData<FirebaseUser> = firebaseAuthManager.liveFirebaseUser
    var loggedOut : MutableLiveData<Boolean> = firebaseAuthManager.loggedOut

    fun logOut() {
        firebaseAuthManager.logOut()
    }

    fun deleteAccount() {
        firebaseAuthManager.deleteAccount()
    }

    fun saveUser(
    firebaseUser: MutableLiveData<FirebaseUser>,

    ) {
        try {
            FirebaseDBManager.saveUser(firebaseUser)
        } catch (e: IllegalArgumentException) {
            Timber.i(e.toString())
        }
    }
           //     (firebaseUser: MutableLiveData<FirebaseUser>) {
      //  try {
       //     FirebaseDBManager.saveUser(firebaseUser)
       // } catch (e: IllegalArgumentException) {
        //    Timber.i(e.toString())
       // }
    //}
}