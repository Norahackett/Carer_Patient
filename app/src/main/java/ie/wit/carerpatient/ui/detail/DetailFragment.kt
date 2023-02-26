package ie.wit.carerpatient.ui.detail

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
import ie.wit.carerpatient.R
import ie.wit.carerpatient.databinding.FragmentDetailBinding
import ie.wit.carerpatient.ui.auth.LoggedInViewModel
import ie.wit.carerpatient.ui.report.ReportViewModel
import timber.log.Timber

class DetailFragment : Fragment() {

    private lateinit var detailViewModel: DetailViewModel
    private val args by navArgs<DetailFragmentArgs>()
    private var _fragBinding: FragmentDetailBinding? = null
    private val fragBinding get() = _fragBinding!!
    private val loggedInViewModel : LoggedInViewModel by activityViewModels()
    private val reportViewModel : ReportViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?
    ): View? {
        _fragBinding = FragmentDetailBinding.inflate(inflater, container, false)
        val root = fragBinding.root

        detailViewModel = ViewModelProvider(this).get(DetailViewModel::class.java)
        detailViewModel.observableMedicine.observe(viewLifecycleOwner, Observer { render() })

        fragBinding.editMedicineButton.setOnClickListener {
            detailViewModel.updateMedicine(loggedInViewModel.liveFirebaseUser.value?.uid!!,
                args.medicineid, fragBinding.medicinevm?.observableMedicine!!.value!!)
            findNavController().navigateUp()
        }

        fragBinding.deleteMedicineButton.setOnClickListener {
            reportViewModel.delete(loggedInViewModel.liveFirebaseUser.value?.email!!,
                detailViewModel.observableMedicine.value?.uid!!)
            findNavController().navigateUp()
        }
        return root
    }

    private fun render() {
       fragBinding.editMedicineName.setText("Medicine Name")
        fragBinding.editFrequency.setText("Medicine Frequency")
        fragBinding.editTime.setText("Medicine Time")
        fragBinding.medicinevm = detailViewModel
        Timber.i("Retrofit fragBinding.medicinevm == $fragBinding.medicinevm")
    }

    override fun onResume() {
        super.onResume()
        detailViewModel.getMedicine(loggedInViewModel.liveFirebaseUser.value?.uid!!,
            args.medicineid)

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _fragBinding = null
    }
}