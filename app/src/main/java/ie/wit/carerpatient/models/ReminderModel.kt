package ie.wit.carerpatient.models


import android.os.Parcelable
import com.google.firebase.database.Exclude
import com.google.firebase.database.IgnoreExtraProperties

import kotlinx.parcelize.Parcelize
import java.util.ArrayList

@IgnoreExtraProperties
@Parcelize
data class ReminderModel(
    var uid: String = "",
    var reminderId: String = "",
    var groupID: String = "",
    var time: Long = 0,
    var requestCode: Int = 0,
    var hour: Int = 0,
    var minute: Int = 0,
    var medName: String = "",
    var medDosage: String = "",
    var groupName: String = "",
    var groupPriorityLevel: Int = 0,
    var active: Boolean = false,
    var repeatDays: MutableList<Int>? = ArrayList(),
    var quantity: Int = 0,
    var unit: String = ""
): Parcelable {
    @Exclude
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "uid" to uid,
            "reminderId" to reminderId,
            "groupID" to groupID,
            "time" to time,
            "requestCode" to requestCode,
            "hour" to hour,
            "minute" to minute,
            "medName" to medName,
            "medDosage" to medDosage,
            "groupName" to groupName,
            "groupPriorityLevel" to groupPriorityLevel,
            "active" to active,
            "repeatDays" to repeatDays,
            "quantity" to quantity,
            "unit" to unit
        )
    }
}