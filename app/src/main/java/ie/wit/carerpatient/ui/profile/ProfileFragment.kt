package ie.wit.carerpatient.ui.profile


import android.app.*
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.net.Uri

import android.os.Bundle
import android.util.Log

import android.view.*
import android.widget.*
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle

import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import com.google.firebase.auth.FirebaseAuth

import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.auth.User
import com.google.firebase.ktx.Firebase

import ie.wit.carerpatient.R
import ie.wit.carerpatient.databinding.FragmentMedicineBinding
import ie.wit.carerpatient.databinding.FragmentProfileBinding
import ie.wit.carerpatient.databinding.FragmentUserBinding
import ie.wit.carerpatient.databinding.NavHeaderBinding
import ie.wit.carerpatient.firebase.FirebaseImageManager
import ie.wit.carerpatient.main.CarerPatientApp
import ie.wit.carerpatient.models.CarerPatientModel
import ie.wit.carerpatient.models.UserModel

import ie.wit.carerpatient.ui.auth.LoggedInViewModel
import ie.wit.carerpatient.ui.report.ReportViewModel
import ie.wit.carerpatient.utils.AlarmReceiver
import ie.wit.carerpatient.utils.readImageUri
import ie.wit.carerpatient.utils.showImagePicker


import timber.log.Timber

import java.util.*

class ProfileFragment : Fragment() {

    private var _fragBinding: FragmentProfileBinding? = null
    private val profileViewModel: ProfileViewModel by activityViewModels()
    private val fragBinding get() = _fragBinding!!
    private val reportViewModel: ReportViewModel by activityViewModels()
    private lateinit var intentLauncher : ActivityResultLauncher<Intent>


    private val loggedInViewModel: LoggedInViewModel by activityViewModels()
    var user = UserModel()
    lateinit var app: CarerPatientApp

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        app = activity?.application as CarerPatientApp


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _fragBinding = FragmentProfileBinding.inflate(inflater, container, false)
        val root: View = fragBinding.root
        setupMenu()
        //  val userId = app.auth.currentUser!!.uid

        //profileViewModel = ViewModelProvider(this).get(ProfileViewModel::class.java)
        //  p//rofileViewModel.observableStatus.observe(viewLifecycleOwner, Observer { status ->
        // status?.let { render(status) }
        //profileViewModel.observableUserList.observe(viewLifecycleOwner, Observer {
        //        user -> user?.let { render() }
        //})

        // setButtonListener(fragBinding)
        //  updateNavHeader(firebaseUser)

        // setupUserInfo()

        //fragBinding.editProfileName.setOnClickListener {
        //  val user = FirebaseAuth.getInstance().currentUser

        //   val profileUpdates = UserProfileChangeRequest.Builder()
        //     .setDisplayName(fragBinding.editProfileName.text.toString())
        //     .build()

        // user?.updateProfile(profileUpdates)
        //     ?.addOnCompleteListener { task ->
        //        if (task.isSuccessful) {
        //           // Log.d(TAG, "User profile updated.")
        //           fragBinding.editProfileName.toString()
        //       }
        //  }
        // updateProfile(app, fragBinding.editProfileName.toString())
        // getActivity()?.finish()
        // }
        fragBinding.editProfileImage.setOnClickListener {
            showImagePicker(intentLauncher)
        }

        //   fragBinding.saveButton.setOnClickListener {
        //     profileViewModel.updateUser(loggedInViewModel.liveFirebaseUser.value?.uid!!,
        //        args.medicineid, fragBinding.medicinevm?.observableMedicine!!.value!!)
        //    findNavController().navigateUp()
        //}

        loggedInViewModel.liveFirebaseUser.observe(viewLifecycleOwner, Observer { firebaseUser ->
            if (firebaseUser != null) {
                //val currentUser = loggedInViewModel.liveFirebaseUser.value
                /*if (currentUser != null)*/
                updateProfile(firebaseUser)
                fragBinding.resetPasswordButton.setOnClickListener { sendPasswordReset(firebaseUser) }
                Timber.i("Email sent.")

            }
        })



        setButtonListener(fragBinding)
        deleteAccount()
        registerImagePickerCallback()

        return root
    }

    private fun deleteAccount() {
        fragBinding.deleteUserButton.setOnClickListener{
                AlertDialog.Builder(requireContext())
                    .setTitle("Delete Account")
                    .setMessage("Are you sure you want to delete your account? \nAll data will be lost")
                    .setPositiveButton("Confirm") { _, _ ->
                        profileViewModel.deleteAccount(loggedInViewModel.liveFirebaseUser.value!!.uid)
                        loggedInViewModel.deleteAccount()
                        Timber.i("Confirm")}
                    .setNegativeButton("Cancel") { _, _ -> }
                    .show()
                true
            }
    }

    fun setButtonListener(layout: FragmentProfileBinding) {


        fragBinding.saveButton.setOnClickListener {
            val name = fragBinding.editProfileName.text.toString()

            profileViewModel.addProfile(loggedInViewModel.liveFirebaseUser)
            Timber.i(profileViewModel.observableUser.value?.toString())


              //  (UserModel(UserName = name)
                      //  )
         //   )

        }
    }

    private fun SaveProfile() {
        fragBinding.saveButton
    }
       // fragBinding.resetPasswordButton.setOnClickListener({ sendPasswordReset()})


    private fun sendPasswordReset(currentUser: FirebaseUser) {
        //val user = Firebase.auth.currentUser
        val emailAddress = currentUser.email.toString()


        Firebase.auth.sendPasswordResetEmail(emailAddress)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Timber.i("Email sent")

                }
            }
    }


    private fun updateProfile(currentUser: FirebaseUser) {
        FirebaseImageManager.imageUri.observe(viewLifecycleOwner) { result ->
            if (result == Uri.EMPTY) {
                Timber.i("DX NO Existing imageUri")
                if (currentUser.photoUrl != null) {
                    //if you're a google user
                    FirebaseImageManager.updateUserImage(
                        currentUser.uid,
                        currentUser.photoUrl,
                        fragBinding.editProfileImage,
                        false
                    )
                } else {
                    Timber.i("DX Loading Existing Default imageUri")
                    FirebaseImageManager.updateDefaultImage(
                        currentUser.uid,
                        R.drawable.ic_launcher_homer,
                        fragBinding.editProfileImage
                    )
                }
            } else // load existing image from firebase
            {
                Timber.i("DX Loading Existing imageUri")
                FirebaseImageManager.updateUserImage(
                    currentUser.uid,
                    FirebaseImageManager.imageUri.value,
                    fragBinding.editProfileImage, false
                )
            }
        }
        fragBinding.updateEmailInput.text = currentUser.email
        if(currentUser.displayName != null)
            fragBinding.updateEmailInput.text = currentUser.email
    }

    private fun registerImagePickerCallback() {
        intentLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                when(result.resultCode){
                    AppCompatActivity.RESULT_OK -> {
                        if (result.data != null) {
                            Timber.i("DX registerPickerCallback() ${readImageUri(result.resultCode, result.data).toString()}")
                            FirebaseImageManager
                                .updateUserImage(loggedInViewModel.liveFirebaseUser.value!!.uid,
                                    readImageUri(result.resultCode, result.data),
                                    fragBinding.editProfileImage,
                                    true)
                        } // end of if
                    }
                    AppCompatActivity.RESULT_CANCELED -> { } else -> { }
                }
            }
    }



    private fun setupMenu() {
        (requireActivity() as MenuHost).addMenuProvider(object : MenuProvider {
            override fun onPrepareMenu(menu: Menu) {
                // Handle for example visibility of menu items
            }

            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.menu_medicine, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                // Validate and handle the selected menu item
                return NavigationUI.onNavDestinationSelected(
                    menuItem,
                    requireView().findNavController()
                )
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }



}
