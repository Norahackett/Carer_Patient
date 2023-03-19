package ie.wit.carerpatient.ui.appointmentdetail

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
import ie.wit.carerpatient.databinding.FragmentAppointmentdetailBinding

import ie.wit.carerpatient.ui.appointmentlist.AppointmentListViewModel
import ie.wit.carerpatient.ui.auth.LoggedInViewModel

import timber.log.Timber

class AppointmentDetailFragment : Fragment() {

    private lateinit var appointmentdetailViewModel: AppointmentDetailViewModel
    private val args by navArgs<AppointmentDetailFragmentArgs>()
    private var _fragBinding: FragmentAppointmentdetailBinding? = null
    private val fragBinding get() = _fragBinding!!
    private val loggedInViewModel : LoggedInViewModel by activityViewModels()
    private val appointmentlistViewModel : AppointmentListViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?
    ): View? {
        _fragBinding = FragmentAppointmentdetailBinding.inflate(inflater, container, false)
        val root = fragBinding.root

        appointmentdetailViewModel = ViewModelProvider(this).get(AppointmentDetailViewModel::class.java)
        appointmentdetailViewModel.observableAppointment.observe(viewLifecycleOwner, Observer { render() })

        fragBinding.editAppointmentButton.setOnClickListener {
            appointmentdetailViewModel.updateAppointment(loggedInViewModel.liveFirebaseUser.value?.uid!!,
                args.appointmentid, fragBinding.appointmentvm?.observableAppointment!!.value!!)
            findNavController().navigateUp()
        }

        fragBinding.deleteAppointmentButton.setOnClickListener {
            appointmentlistViewModel.deleteAppointment(loggedInViewModel.liveFirebaseUser.value?.uid!!,
                appointmentdetailViewModel.observableAppointment.value?.uid!!)
            findNavController().navigateUp()
        }
        return root
    }

    private fun render() {
        fragBinding.editAppointmentName.setText(" Name")
        fragBinding.editFrequency.setText("Medicine Frequency")
        fragBinding.editTime.setText("Medicine Time")
        fragBinding.editQuantity.setText("Medicine Quantity")
        fragBinding.appointmentvm = appointmentdetailViewModel
        Timber.i("Retrofit fragBinding.appointmentvm == $fragBinding.appointmentvm")
    }

    override fun onResume() {
        super.onResume()
        appointmentdetailViewModel.getAppointment(loggedInViewModel.liveFirebaseUser.value?.uid!!,
            args.appointmentid)

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _fragBinding = null
    }
}