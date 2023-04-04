package ie.wit.carerpatient.utils


import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.graphics.Color
import android.net.Uri

import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.fragment.app.FragmentActivity
import com.makeramen.roundedimageview.RoundedTransformationBuilder
import com.squareup.picasso.Transformation
import ie.wit.carerpatient.R
import ie.wit.carerpatient.main.CarerPatientApp
import ie.wit.carerpatient.models.UserModel
import java.io.IOException

fun createLoader(activity: FragmentActivity) : AlertDialog {
    val loaderBuilder = AlertDialog.Builder(activity)
        .setCancelable(true) // 'false' if you want user to wait
        .setView(R.layout.loading)
    var loader = loaderBuilder.create()
    loader.setTitle(R.string.app_name)
    loader.setIcon(R.mipmap.ic_launcher_round)

    return loader
}



fun showLoader(loader: AlertDialog, message: String) {
    if (!loader.isShowing) {
        loader.setTitle(message)
        loader.show()
    }
}

fun showLoaderApp(loader: AlertDialog, message: String) {
    if (!loader.isShowing) {
        loader.setTitle(message)
        loader.show()
    }
}

fun hideLoader(loader: AlertDialog) {
    if (loader.isShowing)
        loader.dismiss()
}

fun hideLoaderApp(loader: AlertDialog) {
    if (loader.isShowing)
        loader.dismiss()
}

fun serviceUnavailableMessage(activity: FragmentActivity) {
    Toast.makeText(
        activity,
        "CarePatient Service Unavailable. Try again later",
        Toast.LENGTH_LONG
    ).show()
}

fun serviceAvailableMessage(activity: FragmentActivity) {
    Toast.makeText(
        activity,
        "CarePatient Contacted Successfully",
        Toast.LENGTH_LONG
    ).show()
}

fun customTransformation() : Transformation =
    RoundedTransformationBuilder()
        .borderColor(Color.WHITE)
        .borderWidthDp(2F)
        .cornerRadiusDp(35F)
        .oval(false)
        .build()

//fun updateProfile(app: CarerPatientApp,  profilepic: String,) {
  //  val userId = app.auth.currentUser!!.uid
    //val name = app.auth.currentUser!!.displayName
    //val values = UserModel(userId, profilepic, name = String()).toMap()
    //val childUpdates = HashMap<String, Any>()

    //childUpdates["/user-photos/$userId"] = values
      //app.database.updateChildren(childUpdates)

//}

fun showImagePicker(intentLauncher : ActivityResultLauncher<Intent>) {
    var chooseFile = Intent(Intent.ACTION_OPEN_DOCUMENT)
    chooseFile.type = "image/*"
    chooseFile = Intent.createChooser(chooseFile, R.string.select_profile_image.toString())
    intentLauncher.launch(chooseFile)
}

fun readImageUri(resultCode: Int, data: Intent?): Uri? {
    var uri: Uri? = null
    if (resultCode == Activity.RESULT_OK && data != null && data.data != null) {
        try { uri = data.data }
        catch (e: IOException) {
            e.printStackTrace()
        }
    }
    return uri
}