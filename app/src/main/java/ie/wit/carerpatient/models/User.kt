package ie.wit.carerpatient.models



import android.os.Parcelable
import com.google.firebase.database.Exclude
import com.google.firebase.database.IgnoreExtraProperties

import kotlinx.parcelize.Parcelize

@IgnoreExtraProperties
@Parcelize
data class User(
    var uid: String? = "",
    var firstName:  String = "",
    var lastName:  String = "",
    var profilepic: String = "",
    var email: String? = "joe@bloggs.com")
    : Parcelable
{
    @Exclude
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "uid" to uid,
            "firstname" to firstName,
            "lastname" to lastName,
            "profilepic" to profilepic,
            "email" to email,

            )
    }
}