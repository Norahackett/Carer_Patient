package ie.wit.carerpatient.ui.profile

import android.app.AlarmManager
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.ui.NavigationUI
import com.google.firebase.auth.FirebaseUser
import com.itextpdf.xmp.impl.ISO8601Converter.render

import ie.wit.carerpatient.R
import ie.wit.carerpatient.databinding.FragmentDetailBinding
import ie.wit.carerpatient.databinding.FragmentProfileBinding

import ie.wit.carerpatient.firebase.FirebaseImageManager
import ie.wit.carerpatient.models.CarerPatientModel
import ie.wit.carerpatient.ui.auth.LoggedInViewModel
import ie.wit.carerpatient.ui.auth.LoginRegisterViewModel

import ie.wit.carerpatient.ui.detail.DetailViewModel
import ie.wit.carerpatient.ui.medicine.MedicineViewModel
import ie.wit.carerpatient.ui.report.ReportViewModel
//import ie.wit.carerpatient.ui.report.ReportViewModel
import timber.log.Timber

class ProfileFragment : Fragment() {

    private lateinit var profileViewModel: ProfileViewModel

    //private val args by navArgs<ProfileFragmentArgs>()
    private var _fragBinding: FragmentProfileBinding? = null
    private val fragBinding get() = _fragBinding!!
    private val loggedInViewModel: LoggedInViewModel by activityViewModels()
    private val registerViewModel: LoginRegisterViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _fragBinding = FragmentProfileBinding.inflate(inflater, container, false)
        val root = fragBinding.root

        profileViewModel = ViewModelProvider(this).get(ProfileViewModel::class.java)


        // setupUserInfo()

        return root
    }
}


    //private fun render() {
    //    fragBinding.updateEmailInput.setText("Medicine Name")
     //   fragBinding.updateFirstNameInput.setText("Medicine Duration")
     //   fragBinding.updatePasswordInput.setText("Medicine Time")
     //   fragBinding.medicinevm = profileViewModel
  //      Timber.i("Retrofit fragBinding.medicinevm == $fragBinding.medicinevm")
    //}

   // override fun onResume() {
   //     super.onResume()
    //    profileViewModel.getMedicine(loggedInViewModel.liveFirebaseUser.value?.uid!!,
      //     args.uid )
//
   // }

    //override fun onDestroyView() {
      //  super.onDestroyView()
    //    _fragBinding = null
   // }

//private fun setupUserInfo(){

   // profileViewModel.getUser().observe(viewLifecycleOwner, Observer { medicine ->
    //    if (medicine != null) {
      //      fragBinding.updateFirstNameInput.text = currentuser.name
      //  }
    //}
//}
//}





