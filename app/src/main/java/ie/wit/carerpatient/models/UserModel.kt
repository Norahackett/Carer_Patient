package ie.wit.carerpatient.models



import android.os.Parcelable
import com.google.firebase.database.Exclude
import com.google.firebase.database.IgnoreExtraProperties

import kotlinx.parcelize.Parcelize

@IgnoreExtraProperties
@Parcelize
data class UserModel(
    var userUid: String? = "",
    var profilepic: String ="",
    var UserName: String? = "")

    : Parcelable
{
    @Exclude
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "userUid:" to userUid,
            "profilepic" to profilepic,
            "UserName" to UserName

            )
    }
}