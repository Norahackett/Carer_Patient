package ie.wit.carerpatient.models


import android.os.Parcelable
import com.google.firebase.database.Exclude
import com.google.firebase.database.IgnoreExtraProperties

import kotlinx.parcelize.Parcelize

@IgnoreExtraProperties
@Parcelize
data class AppointmentModel(
    var uid: String? = "",
    var frequency: String = "",
    var name:  String = "",
    var quantity: Int = 0,
    var time2: String = "",
   // var firstname:String = "",
    //var lastname: String = ""
)
    //var email: String? = "joe@bloggs.com")
    : Parcelable
{
    @Exclude
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "uid" to uid,
            "frequency" to frequency,
            "name" to name,
            "quantity" to quantity,
            "time2" to time2,

           // "email" to email,
           // "firstname" to firstname,
            //lastname" to lastname
            //"amount" to amount

        )
    }
}