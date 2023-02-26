package ie.wit.carerpatient.models


import android.os.Parcelable
import com.google.firebase.database.Exclude
import com.google.firebase.database.IgnoreExtraProperties

import kotlinx.parcelize.Parcelize

@IgnoreExtraProperties
@Parcelize
data class CarerPatientModel(
    var uid: String? = "",
    var frequency: String = "",
    var medicinename:  String = "",
    //var amount: Int = 0,
    var quantity: Int = 0,
    var time2: String = "",
    var profilepic: String = "",
    var email: String? = "joe@bloggs.com")
    : Parcelable
{
    @Exclude
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "uid" to uid,
            "frequency" to frequency,
            "medicinename" to medicinename,
            "quantity" to quantity,
            "time2" to time2,
            "profilepic" to profilepic,
            "email" to email,
            //"amount" to amount

        )
    }
}