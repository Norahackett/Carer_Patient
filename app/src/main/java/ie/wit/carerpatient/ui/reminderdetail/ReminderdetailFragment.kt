package ie.wit.carerpatient.ui.reminderdetail

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
import ie.wit.carerpatient.databinding.FragmentReminderdetailBinding
import ie.wit.carerpatient.ui.auth.LoggedInViewModel

import ie.wit.carerpatient.ui.reminderreport.ReminderreportViewModel
import timber.log.Timber



class ReminderdetailFragment : Fragment() {

    private lateinit var reminderdetailViewModel: ReminderdetailViewModel
    private val args by navArgs<ReminderdetailFragmentArgs>()
    private var _fragBinding: FragmentReminderdetailBinding? = null
    private val fragBinding get() = _fragBinding!!
    private val loggedInViewModel : LoggedInViewModel by activityViewModels()
    private val reminderreportViewModel : ReminderreportViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?
    ): View? {
        _fragBinding = FragmentReminderdetailBinding.inflate(inflater, container, false)
        val root = fragBinding.root

        reminderdetailViewModel = ViewModelProvider(this).get(ReminderdetailViewModel::class.java)
        reminderdetailViewModel.observableReminder.observe(viewLifecycleOwner, Observer { render() })

        fragBinding.editMedicineButton.setOnClickListener {
            reminderdetailViewModel.updateReminder(loggedInViewModel.liveFirebaseUser.value?.uid!!,
                args.reminderid, fragBinding.remindervm?.observableReminder!!.value!!)
            findNavController().navigateUp()
        }

        fragBinding.deleteMedicineButton.setOnClickListener {
            reminderreportViewModel.delete(loggedInViewModel.liveFirebaseUser.value?.uid!!,
                reminderdetailViewModel.observableReminder.value?.uid!!)
            findNavController().navigateUp()
        }
        return root
    }

    private fun render() {
       // fragBinding.editMedicineName.setText("Medicine Name")
       // fragBinding.editFrequency.setText("Medicine Frequency")
       // fragBinding.editTime.setText("Medicine Time")
        fragBinding.editQuantity.setText("Medicine Quantity")
        fragBinding.remindervm = reminderdetailViewModel
        Timber.i("Retrofit fragBinding.remindervm == $fragBinding.remindervm")
    }

    override fun onResume() {
        super.onResume()
        reminderdetailViewModel.getReminder(loggedInViewModel.liveFirebaseUser.value?.uid!!,
            args.reminderid)

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _fragBinding = null
    }
}