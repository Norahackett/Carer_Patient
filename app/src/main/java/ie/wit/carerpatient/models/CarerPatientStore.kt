package ie.wit.carerpatient.models

import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseUser

interface CarerPatientStore {
    fun findAll(medicinesList:
                MutableLiveData<List<CarerPatientModel>>)
    fun findAll(userid:String,
                medicationsList:
                MutableLiveData<List<CarerPatientModel>>)
    fun findById(userid:String, medicineid: String,
                 medicine: MutableLiveData<CarerPatientModel>)
    fun create(firebaseUser: MutableLiveData<FirebaseUser>, medicine: CarerPatientModel)
    fun delete(userid:String, medicineid: String)
    fun update(userid:String, medicineid: String, medicine: CarerPatientModel)
}