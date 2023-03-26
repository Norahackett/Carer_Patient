package ie.wit.carerpatient.ui.auth

import android.app.AlarmManager
import android.app.PendingIntent

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*


import android.widget.Toast

import android.app.*

import android.widget.*
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import ie.wit.carerpatient.R
import ie.wit.carerpatient.databinding.FragmentMedicineBinding
import ie.wit.carerpatient.models.CarerPatientModel

import ie.wit.carerpatient.utils.AlarmReceiver

import java.util.*

import ie.wit.carerpatient.databinding.FragmentRegisterBinding


import ie.wit.carerpatient.models.User



import timber.log.Timber
import java.util.*

private var _fragBinding: FragmentRegisterBinding? = null
private lateinit var registerViewModel: RegisterViewModel
private val fragBinding get() = _fragBinding!!
//private val reportViewModel: ReportViewModel by activityViewModels()

private lateinit var loggedInViewModel: LoggedInViewModel
var user = User()



class Register : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _fragBinding = FragmentRegisterBinding.inflate(inflater, container, false)
        val root: View = fragBinding.root
        setupMenu()
        registerViewModel = ViewModelProvider(this).get(RegisterViewModel::class.java)
        registerViewModel.observableStatus.observe(viewLifecycleOwner, Observer { status ->
            status?.let { render(status) }

        })
        setButtonListener(fragBinding)



        return root;
    }

    fun setButtonListener(layout: FragmentRegisterBinding) {


        //val time = getTime(hour, minute)
        layout.registerButton.setOnClickListener {


            val firstname =
                if (layout.registerFirstNameInput.text.isNullOrEmpty()) "Medicine" else layout.registerFirstNameInput.text.toString()
            val lastname = layout.registerLastNameInput.text.toString()


            registerViewModel.addUser(
                loggedInViewModel.liveFirebaseUser,

                User(
                    firstName = firstname,
                    lastName = lastname

                   //email = loggedInViewModel.liveFirebaseUser.value?.email!!
                )
            )
        }




    }


    private fun render(status: Boolean) {
        when (status) {
            true -> {
                view?.let {
                    //Uncomment this if you want to immediately return to Report
                    //findNavController().popBackStack()
                }
            }
            false -> Toast.makeText(context, getString(R.string.medicineError), Toast.LENGTH_LONG)
                .show()
        }
    }


   // private fun createAccount(email: String, password: String) {
  //      Timber.d("createAccount:$email")
    //    if (!validateForm()) {
    //        return
    //    }
    //    loginRegisterViewModel.register(email, password)
    //}




    private fun setupMenu() {
        (requireActivity() as MenuHost).addMenuProvider(object : MenuProvider {
            override fun onPrepareMenu(menu: Menu) {
                // Handle for example visibility of menu items
            }

            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.menu_register, menu)
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