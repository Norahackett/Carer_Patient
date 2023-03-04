package ie.wit.carerpatient.models

import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseUser

interface CarerPatientStore {
    fun findAll(medicinesList:
                MutableLiveData<List<CarerPatientModel>>)
    fun findAll(userid:String,
                medicinesList:
                MutableLiveData<List<CarerPatientModel>>)
    fun findById(userid:String, medicineid: String,
                 medicine: MutableLiveData<CarerPatientModel>)
    fun create(firebaseUser: MutableLiveData<FirebaseUser>, medicine: CarerPatientModel)
    fun delete(userid:String, medicineid: String)
    fun update(userid:String, medicineid: String, medicine: CarerPatientModel)
    fun findAllAppointment(appointmentsList:
                MutableLiveData<List<AppointmentModel>>)
    fun findAllAppointment(userid:String,
                appointmentsList:
                MutableLiveData<List<AppointmentModel>>)
    fun findByIdAppointment(userid:String, appointmentid: String,
                 appointment: MutableLiveData<AppointmentModel>)
    fun createAppointment(firebaseUser: MutableLiveData<FirebaseUser>, appointment: AppointmentModel)
    fun deleteAppointment(userid:String, appointmentid: String)
    fun updateAppointment(userid:String, appointmentid: String, appointment: AppointmentModel)
}