package ie.wit.carerpatient.ui.medicine

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import ie.wit.carerpatient.R
import ie.wit.carerpatient.databinding.FragmentMedicineBinding
import ie.wit.carerpatient.models.CarerPatientModel
import ie.wit.carerpatient.ui.auth.LoggedInViewModel
import ie.wit.carerpatient.ui.report.ReportViewModel


class MedicineFragment : Fragment() {

    private var _fragBinding: FragmentMedicineBinding? = null
    private lateinit var medicineViewModel: MedicineViewModel
    private val fragBinding get() = _fragBinding!!
    private val reportViewModel: ReportViewModel by activityViewModels()

    private val loggedInViewModel: LoggedInViewModel by activityViewModels()
    var medicine = CarerPatientModel()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _fragBinding = FragmentMedicineBinding.inflate(inflater, container, false)
        val root: View = fragBinding.root
        setupMenu()
        medicineViewModel = ViewModelProvider(this).get(MedicineViewModel::class.java)
        medicineViewModel.observableStatus.observe(viewLifecycleOwner, Observer {
                status -> status?.let { render(status) }
        })

    setButtonListener(fragBinding)

    return root;
}

    private fun render(status: Boolean) {
        when (status) {
            true -> {
                view?.let {
                    //Uncomment this if you want to immediately return to Report
                    //findNavController().popBackStack()
                }
            }
            false -> Toast.makeText(context,getString(R.string.medicineError),Toast.LENGTH_LONG).show()
        }
    }


    fun setButtonListener(layout: FragmentMedicineBinding) {
        layout.medicineButton.setOnClickListener {
            //val medicinename = layout.medicineName.text.toString()
            val quantity=  layout.quantity.text.toString().toInt()
            val medicinename= layout.name.text.toString()
            val frequency= layout.frequency.text.toString()
            val time2= layout.time.text.toString()
            //medicine.quantity =layout.quantity.text.toString().toInt()
           // medicine.time2=layout.time.text.toString()
            //if (medicine.medicinename.isEmpty()) {
              //  Snackbar.make(it, R.string.medicineName, Snackbar.LENGTH_LONG)
                //    .show()


            //  medicine.medicinename = layout.medicinename.text.toString().trim()
            //medicine.quantity = layout.quantity.text.toString().toInt()
            //medicine.frequency = layout.frequency.text.toString().toInt()
            //  medicine.name = layout.name.text.toString().trim()
            // val amount = if (layout.quantity.text.isNotEmpty())
            //        layout.quantity.text.toString().toInt() else layout.amountPicker.value
            // if(totalMedicine >= layout.progressBar.max)
            //       Toast.makeText(context,"Medicine Amount Exceeded!", Toast.LENGTH_LONG).show()
            // else {
            // val quantity = if(layout.quantity2.checkedRadioButtonId == R.id.Direct) "Direct" else "Paypal"
            // totalMedicine += amount
            // layout.totalSoFar.text = String.format(getString(R.string.totalSoFar),totalMedicine)
            //layout.progressBar.progress = totalMedicine

            medicineViewModel.addMedicine(
                loggedInViewModel.liveFirebaseUser,

                CarerPatientModel( quantity = quantity,medicinename = medicinename, frequency = frequency, time2 = time2,
                    email = loggedInViewModel.liveFirebaseUser.value?.email!!
                )
            )
        }

        // findNavController().popBackStack()
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


    override fun onDestroyView() {
        super.onDestroyView()
        _fragBinding = null
    }

    override fun onResume() {
        super.onResume()

            }


}

