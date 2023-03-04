package ie.wit.carerpatient.ui.appointment

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
import ie.wit.carerpatient.databinding.FragmentAppointmentBinding
import ie.wit.carerpatient.models.AppointmentModel
import ie.wit.carerpatient.ui.auth.LoggedInViewModel


class AppointmentFragment : Fragment() {

    private var _fragBinding: FragmentAppointmentBinding? = null
    private lateinit var appointmentViewModel: AppointmentViewModel
    private val fragBinding get() = _fragBinding!!


    private val loggedInViewModel: LoggedInViewModel by activityViewModels()
    var appointment = AppointmentModel()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _fragBinding = FragmentAppointmentBinding.inflate(inflater, container, false)
        val root: View = fragBinding.root
        setupMenu()
        appointmentViewModel = ViewModelProvider(this).get(AppointmentViewModel::class.java)
        appointmentViewModel.observableStatus.observe(viewLifecycleOwner, Observer {
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
            false -> Toast.makeText(context,getString(R.string.appointmentError),Toast.LENGTH_LONG).show()
        }
    }


    fun setButtonListener(layout: FragmentAppointmentBinding) {
        layout.appointmentButton.setOnClickListener {
            //val appointmentname = layout.appointmentName.text.toString()
            val quantity=  layout.quantity.text.toString().toInt()
            val appointmentname= layout.name.text.toString()
            val frequency= layout.frequency.text.toString()
            val time2= layout.time.text.toString()


            appointmentViewModel.addAppointment(
                loggedInViewModel.liveFirebaseUser,

                AppointmentModel( quantity = quantity,appointmentname = appointmentname, frequency = frequency, time2 = time2,
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
                menuInflater.inflate(R.menu.menu_appointment, menu)
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
