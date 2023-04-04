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
    fun deleteUser(userid:String)
    fun update(userid:String, medicineid: String, medicine: CarerPatientModel)
    fun getUser(firebaseUser: MutableLiveData<FirebaseUser>, user: MutableLiveData<UserModel>)
   // fun createUser(firebaseUser: MutableLiveData<FirebaseUser>, user: UserModel)
    //fun findUserById(userid:String,
              //   user: MutableLiveData<UserModel>)
   // fun findAllUser(userid:String,
               // userList:
               // MutableLiveData<List<UserModel>>)

}