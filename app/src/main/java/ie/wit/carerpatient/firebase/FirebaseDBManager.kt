package ie.wit.carerpatient.firebase

import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import ie.wit.carerpatient.models.AppointmentModel
import ie.wit.carerpatient.models.CarerPatientModel

import ie.wit.carerpatient.models.CarerPatientStore
import timber.log.Timber

object FirebaseDBManager : CarerPatientStore {

    var database: DatabaseReference = FirebaseDatabase.getInstance().reference

    override fun findAll(medicinesList: MutableLiveData<List<CarerPatientModel>>) {
        database.child("medicines")
            .addValueEventListener(object : ValueEventListener {
                override fun onCancelled(error: DatabaseError) {
                    Timber.i("Firebase Medicines error : ${error.message}")
                }

                override fun onDataChange(snapshot: DataSnapshot) {
                    val localList = ArrayList<CarerPatientModel>()
                    val children = snapshot.children
                    children.forEach {
                        val medicine = it.getValue(CarerPatientModel::class.java)
                        localList.add(medicine!!)
                    }
                    database.child("medicines")
                        .removeEventListener(this)

                    medicinesList.value = localList
                }
            })
    }

    override fun findAllAppointment(appointmentsList: MutableLiveData<List<AppointmentModel>>) {
        database.child("appointments")
            .addValueEventListener(object : ValueEventListener {
                override fun onCancelled(error: DatabaseError) {
                    Timber.i("Firebase Appointment error : ${error.message}")
                }

                override fun onDataChange(snapshot: DataSnapshot) {
                    val localList = ArrayList<AppointmentModel>()
                    val children = snapshot.children
                    children.forEach {
                        val appointment = it.getValue(AppointmentModel::class.java)
                        localList.add(appointment!!)
                    }
                    database.child("appointments")
                        .removeEventListener(this)

                    appointmentsList.value = localList
                }
            })
    }


    override fun findAll(userid: String, medicinesList: MutableLiveData<List<CarerPatientModel>>) {

        database.child("user-medicines").child(userid)
            .addValueEventListener(object : ValueEventListener {
                override fun onCancelled(error: DatabaseError) {
                    Timber.i("Firebase medicine error : ${error.message}")
                }

                override fun onDataChange(snapshot: DataSnapshot) {
                    val localList = ArrayList<CarerPatientModel>()
                    val children = snapshot.children
                    children.forEach {
                        val medicine = it.getValue(CarerPatientModel::class.java)
                        localList.add(medicine!!)
                    }
                    database.child("user-medicines").child(userid)
                        .removeEventListener(this)

                    medicinesList.value = localList
                }
            })
    }

    override fun findAllAppointment(
        userid: String,
        appointmentList: MutableLiveData<List<AppointmentModel>>
    ) {

        database.child("user-appointments").child(userid)
            .addValueEventListener(object : ValueEventListener {
                override fun onCancelled(error: DatabaseError) {
                    Timber.i("Firebase appointment error : ${error.message}")
                }

                override fun onDataChange(snapshot: DataSnapshot) {
                    val localList = ArrayList<AppointmentModel>()
                    val children = snapshot.children
                    children.forEach {
                        val appointment = it.getValue(AppointmentModel::class.java)
                        localList.add(appointment!!)
                    }
                    database.child("user-appointments").child(userid)
                        .removeEventListener(this)

                    appointmentList.value = localList
                }
            })
    }

    override fun findById(
        userid: String,
        medicineid: String,
        medicine: MutableLiveData<CarerPatientModel>
    ) {

        database.child("user-medicines").child(userid)
            .child(medicineid).get().addOnSuccessListener {
                medicine.value = it.getValue(CarerPatientModel::class.java)
                Timber.i("firebase Got value ${it.value}")
            }.addOnFailureListener {
                Timber.e("firebase Error getting data $it")
            }
    }

    override fun findByIdAppointment(
        userid: String,
        appointmentid: String,
        appointment: MutableLiveData<AppointmentModel>
    ) {

        database.child("user-appointments").child(userid)
            .child(appointmentid).get().addOnSuccessListener {
                appointment.value = it.getValue(AppointmentModel::class.java)
                Timber.i("firebase Got value ${it.value}")
            }.addOnFailureListener {
                Timber.e("firebase Error getting data $it")
            }
    }

    override fun create(firebaseUser: MutableLiveData<FirebaseUser>, medicine: CarerPatientModel) {
        Timber.i("Firebase DB Reference : $database")

        val uid = firebaseUser.value!!.uid
        val key = database.child("medicines").push().key
        if (key == null) {
            Timber.i("Firebase Error : Key Empty")
            return
        }
        medicine.uid = key
        val medicineValues = medicine.toMap()

        val childAdd = HashMap<String, Any>()
        childAdd["/medicines/$key"] = medicineValues
        childAdd["/user-medicines/$uid/$key"] = medicineValues

        database.updateChildren(childAdd)
    }

    override fun createAppointment(
        firebaseUser: MutableLiveData<FirebaseUser>,
        appointment: AppointmentModel
    ) {
        Timber.i("Firebase DB Reference : $database")

        val uid = firebaseUser.value!!.uid
        val key = database.child("appointments").push().key
        if (key == null) {
            Timber.i("Firebase Error : Key Empty")
            return
        }
        appointment.uid = key
        val appointmentValues = appointment.toMap()

        val childAdd = HashMap<String, Any>()
        childAdd["/appointments/$key"] = appointmentValues
        childAdd["/user-appointments/$uid/$key"] = appointmentValues

        database.updateChildren(childAdd)
    }

    override fun delete(userid: String, medicineid: String) {

        val childDelete: MutableMap<String, Any?> = HashMap()
        childDelete["/medicines/$medicineid"] = null
        childDelete["/user-medicines/$userid/$medicineid"] = null

        database.updateChildren(childDelete)
    }

    override fun deleteAppointment(userid: String, appointmentid: String) {

        val childDelete: MutableMap<String, Any?> = HashMap()
        childDelete["/appointments/$appointmentid"] = null
        childDelete["/user-appointments/$userid/$appointmentid"] = null

        database.updateChildren(childDelete)
    }

    override fun update(userid: String, medicineid: String, medicine: CarerPatientModel) {

        val medicineValues = medicine.toMap()

        val childUpdate: MutableMap<String, Any?> = HashMap()
        childUpdate["medicines/$medicineid"] = medicineValues
        childUpdate["user-medicines/$userid/$medicineid"] = medicineValues

        database.updateChildren(childUpdate)
    }

    override fun updateAppointment(
        userid: String,
        appointmentid: String,
        appointment: AppointmentModel
    ) {

        val appointmentValues = appointment.toMap()

        val childUpdate: MutableMap<String, Any?> = HashMap()
        childUpdate["appointments/$appointmentid"] = appointmentValues
        childUpdate["user-appointments/$userid/$appointmentid"] = appointmentValues

        database.updateChildren(childUpdate)
    }

    fun updateImageRef(userid: String, imageUri: String) {

        val userMedicines = database.child("user-medicines").child(userid)
        val allMedicines = database.child("medicines")

        userMedicines.addListenerForSingleValueEvent(
            object : ValueEventListener {
                override fun onCancelled(error: DatabaseError) {}
                override fun onDataChange(snapshot: DataSnapshot) {
                    snapshot.children.forEach {
                        //Update Users imageUri
                        it.ref.child("profilepic").setValue(imageUri)
                        //Update all medicines that match 'it'
                        val medicine = it.getValue(CarerPatientModel::class.java)
                        allMedicines.child(medicine!!.uid!!)
                            .child("profilepic").setValue(imageUri)
                    }
                }
            })

        fun updateImageRefAppointment(userid: String, imageUri: String) {

            val userAppointments = database.child("user-appointment").child(userid)
            val allAppointments = database.child("appointments")

            userAppointments.addListenerForSingleValueEvent(
                object : ValueEventListener {
                    override fun onCancelled(error: DatabaseError) {}
                    override fun onDataChange(snapshot: DataSnapshot) {
                        snapshot.children.forEach {
                            //Update Users imageUri
                            it.ref.child("profilepic").setValue(imageUri)
                            //Update all medicines that match 'it'
                            val appointment = it.getValue(AppointmentModel::class.java)
                            allAppointments.child(appointment!!.uid!!)
                                .child("profilepic").setValue(imageUri)
                        }
                    }
                })
        }
    }
}