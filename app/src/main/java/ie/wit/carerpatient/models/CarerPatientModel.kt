package ie.wit.carerpatient.models


import android.os.Parcelable
import com.google.firebase.database.Exclude
import com.google.firebase.database.IgnoreExtraProperties

import kotlinx.parcelize.Parcelize

@IgnoreExtraProperties
@Parcelize
data class CarerPatientModel(
    var uid: String? = "",
    var frequency: String = "N/A",
    var medicinename: Int = 0,

    var amount: Int = 0,
    var quantity: String = "N/A",
    var time2: String = "N/A",
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
            "time" to time2,
            "profilepic" to profilepic,
            "email" to email,
            "amount" to amount

        )
    }
}