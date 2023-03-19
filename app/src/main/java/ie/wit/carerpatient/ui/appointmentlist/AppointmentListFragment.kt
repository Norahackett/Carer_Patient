package ie.wit.carerpatient.ui.appointmentlist


import android.app.AlertDialog
import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SwitchCompat
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ie.wit.carerpatient.R
import ie.wit.carerpatient.adapters.AppointmentAdaptor
import ie.wit.carerpatient.adapters.AppointmentClickListener
import ie.wit.carerpatient.databinding.FragmentAppointmentlistBinding
import ie.wit.carerpatient.models.AppointmentModel
import ie.wit.carerpatient.ui.auth.LoggedInViewModel
import ie.wit.carerpatient.utils.*


class AppointmentListFragment : Fragment(), AppointmentClickListener {

    private var _fragBinding: FragmentAppointmentlistBinding? = null
    private val fragBinding get() = _fragBinding!!
    lateinit var loader : AlertDialog
    private val appointmentlistViewModel: AppointmentListViewModel by activityViewModels()
    private val loggedInViewModel : LoggedInViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?
    ): View? {
        _fragBinding = FragmentAppointmentlistBinding.inflate(inflater, container, false)
        val root = fragBinding.root
        setupMenu()
        loader  = createLoaderApp(requireActivity())


        fragBinding.recyclerView.layoutManager = LinearLayoutManager(activity)
        fragBinding.fab.setOnClickListener {
            val action = AppointmentListFragmentDirections.actionAppointmentlistFragmentToAppointmentFragment()
            findNavController().navigate(action)
        }
        showLoader(loader,"Downloading Appointments")
        appointmentlistViewModel.observableAppointmentList.observe(viewLifecycleOwner, Observer {
                appointments ->
            appointments?.let {
                render(appointments as ArrayList<AppointmentModel>)
                hideLoaderApp(loader)
                checkSwipeRefresh()
            }
        })

        setSwipeRefresh()

        val swipeDeleteHandler = object : AppointmentSwipeToDeleteCallback(requireContext()) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                showLoader(loader,"Deleting Appointments")
                val adapter = fragBinding.recyclerView.adapter as AppointmentAdaptor
                adapter.removeAt(viewHolder.adapterPosition)
                appointmentlistViewModel.deleteAppointment(appointmentlistViewModel.liveFirebaseUser.value?.uid!!,
                    (viewHolder.itemView.tag as AppointmentModel).uid!!)

                hideLoaderApp(loader)
            }
        }
        val itemTouchDeleteHelper = ItemTouchHelper(swipeDeleteHandler)
        itemTouchDeleteHelper.attachToRecyclerView(fragBinding.recyclerView)

        val swipeEditHandler = object : AppointmentSwipeToEditCallback(requireContext()) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                onAppointmentClick(viewHolder.itemView.tag as AppointmentModel)
            }
        }
        val itemTouchEditHelper = ItemTouchHelper(swipeEditHandler)
        itemTouchEditHelper.attachToRecyclerView(fragBinding.recyclerView)

        return root
    }


    private fun setupMenu() {
        (requireActivity() as MenuHost).addMenuProvider(object : MenuProvider {
            override fun onPrepareMenu(menu: Menu) {
                // Handle for example visibility of menu items
            }

            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.menu_appointment_list, menu)

                val item = menu.findItem(R.id.toggleMedicine) as MenuItem
                item.setActionView(R.layout.togglebutton_layout)
                val toggleMedicines: SwitchCompat = item.actionView!!.findViewById(R.id.toggleButton)
                toggleMedicines.isChecked = false

                toggleMedicines.setOnCheckedChangeListener { _, isChecked ->
                    if (isChecked) appointmentlistViewModel.loadAll()
                    else appointmentlistViewModel.load()
                }
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                // Validate and handle the selected menu item
                return NavigationUI.onNavDestinationSelected(menuItem,
                    requireView().findNavController())
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    private fun render(appointmentsList: ArrayList<AppointmentModel>) {
        fragBinding.recyclerView.adapter = AppointmentAdaptor(appointmentsList,this,
            appointmentlistViewModel.readOnly.value!!)
        if (appointmentsList.isEmpty()) {
            fragBinding.recyclerView.visibility = View.GONE
            fragBinding.appointmentsNotFound.visibility = View.VISIBLE
        } else {
            fragBinding.recyclerView.visibility = View.VISIBLE
            fragBinding.appointmentsNotFound.visibility = View.GONE
        }
    }

   override fun onAppointmentClick(appointment: AppointmentModel) {
        val action = AppointmentListFragmentDirections.actionAppointmentlistFragmentToAppointmentdetailFragment(appointment.uid!!)
        if(!appointmentlistViewModel.readOnly.value!!)
            findNavController().navigate(action)
    }



    private fun setSwipeRefresh() {
        fragBinding.swiperefresh.setOnRefreshListener {
            fragBinding.swiperefresh.isRefreshing = true
            showLoaderApp(loader,"Downloading Appointments")
            if(appointmentlistViewModel.readOnly.value!!)
                appointmentlistViewModel.loadAll()
            else
                appointmentlistViewModel.load()
        }
    }

    private fun checkSwipeRefresh() {
        if (fragBinding.swiperefresh.isRefreshing)
            fragBinding.swiperefresh.isRefreshing = false
    }

    override fun onResume() {
        super.onResume()
        showLoaderApp(loader,"Downloading Appointments")
        loggedInViewModel.liveFirebaseUser.observe(viewLifecycleOwner, Observer { firebaseUser ->
            if (firebaseUser != null) {
                appointmentlistViewModel.liveFirebaseUser.value = firebaseUser
                appointmentlistViewModel.load()
            }
        })
        //hideLoader(loader)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _fragBinding = null
    }
}



