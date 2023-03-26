package ie.wit.carerpatient.firebase

import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import ie.wit.carerpatient.models.*
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

    //override fun findAllUser(usersList: MutableLiveData<List<UserModel>>) {
      //  database.child("users")
       //     .addValueEventListener(object : ValueEventListener {
        //        override fun onCancelled(error: DatabaseError) {
          //          Timber.i("Firebase Users error : ${error.message}")
          //      }

            //    override fun onDataChange(snapshot: DataSnapshot) {
            //        val localList = ArrayList<UserModel>()
             //       val children = snapshot.children
                //    children.forEach {
               //         val user = it.getValue(UserModel::class.java)
                //        localList.add(user!!)
                //    }
                  //  database.child("medicines")
                  //      .removeEventListener(this)

                  //  usersList.value = localList
                //}
            //})
   // }





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


   // override fun findAllUser(userid: String, usersList: MutableLiveData<List<UserModel>>) {

    //    database.child("user-users").child(userid)
    //        .addValueEventListener(object : ValueEventListener {
      //          override fun onCancelled(error: DatabaseError) {
      ///              Timber.i("Firebase user error : ${error.message}")
       //         }

          //      override fun onDataChange(snapshot: DataSnapshot) {
           //         val localList = ArrayList<UserModel>()
           //         val children = snapshot.children
             //       children.forEach {
              //          val user= it.getValue(UserModel::class.java)
              //          localList.add(user!!)
               //     }
                //    database.child("user-users").child(userid)
                 //       .removeEventListener(this)

                  //  usersList.value = localList
               // }
           // })
    //}



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

  //  override fun findByIdProfile(
//       userid: String,
   //   // profileid:String,
  //      user: MutableLiveData<UserModel>
  //  ) {

    //    database.child("user-users").child(userid)
    //        .child(userid).get().addOnSuccessListener {
      //          user.value = it.getValue(UserModel::class.java)
      //          Timber.i("firebase Got value ${it.value}")
      //      }.addOnFailureListener {
       //         Timber.e("firebase Error getting data $it")
       //     }
    //}





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

    override fun createUser(firebaseUser: MutableLiveData<FirebaseUser>, user: User) {
        Timber.i("Firebase DB Reference : $database")

        val uid = firebaseUser.value!!.uid
        val key = database.child("user").push().key
        if (key == null) {
            Timber.i("Firebase Error : Key Empty")
            return
        }
        user.uid = key
        val userValues = user.toMap()

        val childAdd = HashMap<String, Any>()
        childAdd["/user/$key"] = userValues
        childAdd["/user-user/$uid/$key"] = userValues

        database.updateChildren(childAdd)
    }

   // override fun createUser(firebaseUser: MutableLiveData<FirebaseUser>, user: UserModel) {
      //  Timber.i("Firebase DB Reference : $database")

     //   val uid = firebaseUser.value!!.uid
      //  val key = database.child("users").push().key
      //  if (key == null) {
       //     Timber.i("Firebase Error : Key Empty")
       //     return
       // }
        //user.uid = key
        //val userValues = user.toMap()

       // val childAdd = HashMap<String, Any>()
       // childAdd["/users/$key"] = userValues
       // childAdd["/user-users/$uid/$key"] = userValues

       // database.updateChildren(childAdd)
    //}




    override fun delete(userid: String, medicineid: String) {

        val childDelete: MutableMap<String, Any?> = HashMap()
        childDelete["/medicines/$medicineid"] = null
        childDelete["/user-medicines/$userid/$medicineid"] = null

        database.updateChildren(childDelete)
    }




    //override fun deleteUser(userid: String) {
   //     val childDelete: MutableMap<String, Any?> = HashMap()
    //    childDelete["/users/$userid"] = null
    //    childDelete["/user-users/$userid/"] = null

     //   database.updateChildren(childDelete)
    //}

    override fun update(userid: String, medicineid: String, medicine: CarerPatientModel) {

        val medicineValues = medicine.toMap()

        val childUpdate: MutableMap<String, Any?> = HashMap()
        childUpdate["medicines/$medicineid"] = medicineValues
        childUpdate["user-medicines/$userid/$medicineid"] = medicineValues

        database.updateChildren(childUpdate)
    }




    //override fun updateprofile(userid: String, user: UserModel) {

     //  val userValues = user.toMap()

      //  val childUpdate: MutableMap<String, Any?> = HashMap()
       // childUpdate["users/$userid"] = userValues
       // childUpdate["user-users/$userid/"] = userValues

     //   database.updateChildren(childUpdate)
    //}


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

    }
}