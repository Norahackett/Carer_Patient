package ie.wit.carerpatient.main

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import ie.wit.carerpatient.ui.settings.ThemeProvider

import timber.log.Timber

class CarerPatientApp : Application() {

    //lateinit var carerPatientStore: CarerPatientStore
   // lateinit var auth: FirebaseAuth
  //  lateinit var database: DatabaseReference
    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        //carerPatientStore = CarerPatientMemStore()
        Timber.i("CarerPatientApp Application Started")
        val theme = ThemeProvider(this).getThemeFromPreferences()
        AppCompatDelegate.setDefaultNightMode(theme)
    }
}