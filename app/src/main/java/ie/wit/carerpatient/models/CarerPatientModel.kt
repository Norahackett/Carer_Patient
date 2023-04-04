package ie.wit.carerpatient.models


import android.os.Parcelable
import com.google.firebase.database.Exclude
import com.google.firebase.database.IgnoreExtraProperties

import kotlinx.parcelize.Parcelize

@IgnoreExtraProperties
@Parcelize
data class CarerPatientModel(
    var uid: String? = "",
    var name:  String = "",
    var amount: Int = 0,
    var type: String ="",
    var time: Long=0,
    var duration: Int = 0,
    var profilepic: String = "",
    var firstname: String = "",
    var lastname: String = "",
    var email: String? = "joe@bloggs.com")
    : Parcelable {
    @Exclude
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "uid" to uid,
            "name" to name,
            "amount" to amount,
            "type" to type,
            "time" to time,
            "duration" to duration,
            "profilepic" to profilepic,
            "email" to email,
            "firstname" to firstname,
            "lastname" to lastname,


            )
    }
}
