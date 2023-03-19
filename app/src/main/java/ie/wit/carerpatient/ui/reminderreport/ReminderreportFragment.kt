package ie.wit.carerpatient.ui.reminderreport

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
import ie.wit.carerpatient.adapters.ReminderAdapter
import ie.wit.carerpatient.adapters.ReminderClickListener
import ie.wit.carerpatient.databinding.FragmentReminderreportBinding
import ie.wit.carerpatient.models.ReminderModel
import ie.wit.carerpatient.ui.auth.LoggedInViewModel

import ie.wit.carerpatient.utils.*



class ReminderreportFragment : Fragment(), ReminderClickListener {

    private var _fragBinding: FragmentReminderreportBinding? = null
    private val fragBinding get() = _fragBinding!!
    lateinit var loader : AlertDialog
    private val reminderreportViewModel: ReminderreportViewModel by activityViewModels()
    private val loggedInViewModel : LoggedInViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?
    ): View? {
        _fragBinding = FragmentReminderreportBinding.inflate(inflater, container, false)
        val root = fragBinding.root
        setupMenu()
        loader = createLoader(requireActivity())

        fragBinding.recyclerView.layoutManager = LinearLayoutManager(activity)
        fragBinding.fab.setOnClickListener {
            val action = ReminderreportFragmentDirections.actionReminderreportFragmentToReminderFragment()
            findNavController().navigate(action)
        }
        showLoader(loader,"Downloading Reminder")
        reminderreportViewModel.observableRemindersList.observe(viewLifecycleOwner, Observer {
                reminders ->
            reminders?.let {
                render(reminders as ArrayList<ReminderModel>)
                hideLoader(loader)
                checkSwipeRefresh()
            }
        })

        setSwipeRefresh()

        val swipeDeleteHandler = object : SwipeToDeleteCallback(requireContext()) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                showLoader(loader,"Deleting Reminder")
                val adapter = fragBinding.recyclerView.adapter as ReminderAdapter
                adapter.removeAt(viewHolder.adapterPosition)
                reminderreportViewModel.delete(reminderreportViewModel.liveFirebaseUser.value?.uid!!,
                    (viewHolder.itemView.tag as ReminderModel).uid!!)

                hideLoader(loader)
            }
        }
        val itemTouchDeleteHelper = ItemTouchHelper(swipeDeleteHandler)
        itemTouchDeleteHelper.attachToRecyclerView(fragBinding.recyclerView)

        val swipeEditHandler = object : SwipeToEditCallback(requireContext()) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                onReminderClick(viewHolder.itemView.tag as ReminderModel)
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
                menuInflater.inflate(R.menu.menu_reminderreport, menu)

                val item = menu.findItem(R.id.toggleMedicine) as MenuItem
                item.setActionView(R.layout.togglebutton_layout)
                val toggleMedicines: SwitchCompat = item.actionView!!.findViewById(R.id.toggleButton)
                toggleMedicines.isChecked = false

                toggleMedicines.setOnCheckedChangeListener { _, isChecked ->
                    if (isChecked) reminderreportViewModel.loadAll()
                    else reminderreportViewModel.load()
                }
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                // Validate and handle the selected menu item
                return NavigationUI.onNavDestinationSelected(menuItem,
                    requireView().findNavController())
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    private fun render(remindersList: ArrayList<ReminderModel>) {
        fragBinding.recyclerView.adapter = ReminderAdapter(remindersList,this,
            reminderreportViewModel.readOnly.value!!)
        if (remindersList.isEmpty()) {
            fragBinding.recyclerView.visibility = View.GONE
            fragBinding.remindersNotFound.visibility = View.VISIBLE
        } else {
            fragBinding.recyclerView.visibility = View.VISIBLE
            fragBinding.remindersNotFound.visibility = View.GONE
        }
    }

    override fun onReminderClick(reminder: ReminderModel) {
        val action = ReminderreportFragmentDirections.actionReminderreportFragmentToReminderdetailFragment(reminder.uid!!)
        if(!reminderreportViewModel.readOnly.value!!)
            findNavController().navigate(action)
    }

    private fun setSwipeRefresh() {
        fragBinding.swiperefresh.setOnRefreshListener {
            fragBinding.swiperefresh.isRefreshing = true
            showLoader(loader,"Downloading Reminders")
            if(reminderreportViewModel.readOnly.value!!)
                reminderreportViewModel.loadAll()
            else
                reminderreportViewModel.load()
        }
    }

    private fun checkSwipeRefresh() {
        if (fragBinding.swiperefresh.isRefreshing)
            fragBinding.swiperefresh.isRefreshing = false
    }

    override fun onResume() {
        super.onResume()
        showLoader(loader,"Downloading Reminders")
        loggedInViewModel.liveFirebaseUser.observe(viewLifecycleOwner, Observer { firebaseUser ->
            if (firebaseUser != null) {
                reminderreportViewModel.liveFirebaseUser.value = firebaseUser
                reminderreportViewModel.load()
            }
        })
        //hideLoader(loader)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _fragBinding = null
    }
}

