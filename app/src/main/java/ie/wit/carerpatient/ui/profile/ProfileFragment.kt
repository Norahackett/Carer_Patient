package ie.wit.carerpatient.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import ie.wit.carerpatient.databinding.FragmentProfileBinding
import ie.wit.carerpatient.ui.auth.LoggedInViewModel
import ie.wit.carerpatient.ui.report.ReportViewModel
import timber.log.Timber

class ProfileFragment : Fragment() {

    private lateinit var profileViewModel: ProfileViewModel
    private val args by navArgs<ProfileFragmentArgs>()
    private var _fragBinding: FragmentProfileBinding? = null
    private val fragBinding get() = _fragBinding!!
    private val loggedInViewModel : LoggedInViewModel by activityViewModels()
    private val reportViewModel : ReportViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?
    ): View? {
        _fragBinding = FragmentProfileBinding.inflate(inflater, container, false)
        val root = fragBinding.root

        profileViewModel = ViewModelProvider(this).get(ProfileViewModel::class.java)
        profileViewModel.observableMedicine.observe(viewLifecycleOwner, Observer { render() })

        fragBinding.editMedicineButton.setOnClickListener {
            profileViewModel.updateMedicine(loggedInViewModel.liveFirebaseUser.value?.uid!!,
                args.medicineid, fragBinding.medicinevm?.observableMedicine!!.value!!)
            findNavController().navigateUp()
        }

        fragBinding.deleteMedicineButton.setOnClickListener {
            reportViewModel.delete(loggedInViewModel.liveFirebaseUser.value?.uid!!,
                profileViewModel.observableMedicine.value?.uid!!)
            findNavController().navigateUp()
        }
        return root
    }

    private fun render() {
        fragBinding.editfirstName.setText("First Name")
        fragBinding.editlastName.setText("Second Name")
        fragBinding.editemail.setText("Email Address")
        fragBinding.medicinevm = profileViewModel
        Timber.i("Retrofit fragBinding.medicinevm == $fragBinding.medicinevm")
    }

    override fun onResume() {
        super.onResume()
        profileViewModel.getMedicine(loggedInViewModel.liveFirebaseUser.value?.uid!!,
            args.medicineid)

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _fragBinding = null
    }
}







