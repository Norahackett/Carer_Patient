package ie.wit.carerpatient.main

import android.app.Application
import ie.wit.carerpatient.models.CarerPatientStore
import timber.log.Timber

class CarerPatientApp : Application() {

    //lateinit var carerPatientStore: CarerPatientStore

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        //carerPatientStore = CarerPatientMemStore()
        Timber.i("CarerPatientApp Application Started")
    }
}