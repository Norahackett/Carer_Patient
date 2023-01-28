package ie.wit.carerpatient.firebase

import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import ie.wit.carerpatient.models.CarerPatientModel

import ie.wit.carerpatient.models.CarerPatientStore
import timber.log.Timber

object FirebaseDBManager : CarerPatientStore {

    var database: DatabaseReference = FirebaseDatabase.getInstance().reference

    override fun findAll(medicinesList: MutableLiveData<List<CarerPatientModel>>) {
        database.child("donations")
            .addValueEventListener(object : ValueEventListener {
                override fun onCancelled(error: DatabaseError) {
                    Timber.i("Firebase Donation error : ${error.message}")
                }

                override fun onDataChange(snapshot: DataSnapshot) {
                    val localList = ArrayList<CarerPatientModel>()
                    val children = snapshot.children
                    children.forEach {
                        val medicine = it.getValue(CarerPatientModel::class.java)
                        localList.add(medicine!!)
                    }
                    database.child("donations")
                        .removeEventListener(this)

                    medicinesList.value = localList
                }
            })
    }

    override fun findAll(userid: String, medicationsList: MutableLiveData<List<CarerPatientModel>>) {

        database.child("user-medicines").child(userid)
            .addValueEventListener(object : ValueEventListener {
                override fun onCancelled(error: DatabaseError) {
                    Timber.i("Firebase Medicine error : ${error.message}")
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

                    medicationsList.value = localList
                }
            })
    }

    override fun findById(userid: String, medicineid: String, medicine: MutableLiveData<CarerPatientModel>) {

        database.child("user-medicines").child(userid)
            .child(medicineid).get().addOnSuccessListener {
                medicine.value = it.getValue(CarerPatientModel::class.java)
                Timber.i("firebase Got value ${it.value}")
            }.addOnFailureListener{
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

    override fun delete(userid: String, medicineid: String) {

        val childDelete : MutableMap<String, Any?> = HashMap()
        childDelete["/medicines/$medicineid"] = null
        childDelete["/user-medicines/$userid/$medicineid"] = null

        database.updateChildren(childDelete)
    }

    override fun update(userid: String, medicineid: String, medicine: CarerPatientModel) {

        val medicineValues = medicine.toMap()

        val childUpdate : MutableMap<String, Any?> = HashMap()
        childUpdate["medicines/$medicineid"] = medicineValues
        childUpdate["user-medicines/$userid/$medicineid"] = medicineValues

        database.updateChildren(childUpdate)
    }
}