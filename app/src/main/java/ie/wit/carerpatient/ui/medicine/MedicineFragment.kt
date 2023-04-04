package ie.wit.carerpatient.ui.medicine

import android.app.*
import android.content.Context
import android.content.Intent

import android.os.Bundle
import android.text.TextUtils
import android.text.format.DateFormat
import android.util.Log
import android.view.*
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
import ie.wit.carerpatient.ui.auth.LoggedInViewModel
import ie.wit.carerpatient.ui.report.ReportViewModel
import ie.wit.carerpatient.utils.AlarmReceiver
import ie.wit.carerpatient.utils.DatePickerHelper
import ie.wit.carerpatient.utils.TimePickerHelper
import java.util.*


class MedicineFragment : Fragment() {


    private var _fragBinding: FragmentMedicineBinding? = null
    private lateinit var medicineViewModel: MedicineViewModel
    private val fragBinding get() = _fragBinding!!
    private val reportViewModel: ReportViewModel by activityViewModels()

    private val loggedInViewModel: LoggedInViewModel by activityViewModels()
    var medicine = CarerPatientModel()

    private lateinit var alarmManager: AlarmManager


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _fragBinding = FragmentMedicineBinding.inflate(inflater, container, false)
        val root: View = fragBinding.root
        setupMenu()
        medicineViewModel = ViewModelProvider(this).get(MedicineViewModel::class.java)
        medicineViewModel.observableStatus.observe(viewLifecycleOwner, Observer { status ->
            status?.let { render(status) }

        })

        alarmManager = requireActivity().getSystemService(Context.ALARM_SERVICE) as AlarmManager
        setButtonListener(fragBinding)
        setupMedicineType()
        setupDateAndTimePicker()
        onSeekbarChanged()

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
            false -> Toast.makeText(context, getString(R.string.medicineError), Toast.LENGTH_LONG)
                .show()
        }
    }


    fun setButtonListener(layout: FragmentMedicineBinding) {


        //val time = getTime(hour, minute)
        layout.saveMedicineButton.setOnClickListener {
            if (validateForm()) {
                //sendNotification()
                //val medicinename = layout.medicineName.text.toString()


                val medicinename =
                    if (layout.medicineNameInput.text.isNullOrEmpty()) "Medicine" else layout.medicineNameInput.text.toString()
                val amount = layout.amountInputField.text.toString().toInt()
                //fragBinding.medicineTypeChooser.setOnItemClickListener { _, _, i, _ ->
                val amount2 = amount.toString()
                val type = medicine.type
                val duration = medicine.duration
                val time = medicine.time
                //val time = medicine.time

                val intent = Intent(requireActivity().applicationContext, AlarmReceiver::class.java)
                //send medicine info to the alarm manager
                intent.apply {
                    putExtra("medicineName", medicinename)
                    putExtra("medicineAmount", amount2)
                    putExtra("medicineType", medicine.type)

                }
                val alarmIntent = intent.let {


                    PendingIntent.getBroadcast(
                        requireActivity().applicationContext,
                        medicine.time.toInt(),
                        it,
                        PendingIntent.FLAG_IMMUTABLE
                    )

                }
                Log.d("OBIEKT", medicine.time.toString())
                alarmManager.set(AlarmManager.RTC_WAKEUP, medicine.time, alarmIntent)

                medicineViewModel.addMedicine(
                    loggedInViewModel.liveFirebaseUser,

                    CarerPatientModel(
                        amount = amount,
                        name = medicinename,
                        type = type,
                        duration = duration,
                        time = time,
                        email = loggedInViewModel.liveFirebaseUser.value?.email!!
                    )
                )
            }

            // findNavController().popBackStack()

        }
    }

    private fun setupMedicineType() {
        fragBinding.medicineTypeChooser.setAdapter(
            ArrayAdapter(
                requireContext(),
                android.R.layout.simple_list_item_1,
                arrayListOf("pills", "ml", "mg")

            )
        )
        val type = fragBinding.medicineTypeChooser.getText().toString()

        fragBinding.medicineTypeChooser.inputType = 0
        fragBinding.medicineTypeChooser.keyListener = null
        fragBinding.medicineTypeChooser.setOnClickListener {
            fragBinding.medicineTypeChooser.showDropDown()
        }
        // val position = medicine.type.
        //set type of medicine object
        //fragBinding.medicineTypeChooser.setOnItemClickListener { _, _, i, _ ->
        //    medicine.type = fragBinding.medicineTypeChooser.adapter.getItem(i).toString()
        //  if .isValid()
        fragBinding.medicineTypeChooser.onItemClickListener =
            AdapterView.OnItemClickListener { _, _, i, _ ->
                medicine.type =
                   fragBinding.medicineTypeChooser.adapter.getItem(i).toString()
                     //   fragBinding.medicineTypeChooser.error = ("select a type")
                    //    fragBinding.medicineTypeChooser.requestFocus()
                //medicine.type.setVA
                    }

    }
              //  if (medicine.type.isEmpty()) {
                 //   fragBinding.medicineTypeChooser.error = ("select a type")
                  //  fragBinding.medicineTypeChooser.requestFocus()
                    // mtextInputLayout.setError(null);
                    //  }
             //   }
          //  }
        // }
    //}

    private fun onSeekbarChanged() {
        fragBinding.durationSeekbar.setOnSeekBarChangeListener(object :
            SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                fragBinding.durationText.text =
                    resources.getQuantityString(R.plurals.numberOfSongsAvailable, p1 + 1, p1 + 1)
                medicine.duration = p1 + 1
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {}
            override fun onStopTrackingTouch(p0: SeekBar?) {}
        })
    }

    private fun setupDateAndTimePicker() {

        //show time picker
        (arrayListOf<View>(fragBinding.chooseTimeButton, fragBinding.timeTextInput)).forEach {
            it.setOnClickListener {
                val timePickerDialog: TimePickerHelper = TimePickerHelper()
                timePickerDialog.show(requireActivity().supportFragmentManager, "time_picker")
            }
        }

        //show date picker
        (arrayListOf<View>(fragBinding.chooseDateButton, fragBinding.dateTextInput)).forEach {
            it.setOnClickListener {
                val datePickerDialog: DatePickerHelper = DatePickerHelper()
                datePickerDialog.show(requireActivity().supportFragmentManager, "date_picker")
            }
        }

        val dateTimePickerViewModel: MedicineViewModel =
            ViewModelProvider(requireActivity()).get(MedicineViewModel::class.java)
        val c = Calendar.getInstance()
        //------set actual time in textviews-------
        dateTimePickerViewModel.setDate(c.timeInMillis)
        dateTimePickerViewModel.setTime(c.timeInMillis)
        //=========================================


        dateTimePickerViewModel.getTime().observe(viewLifecycleOwner, Observer { time ->
            val helper = Calendar.getInstance()
            helper.timeInMillis = time

            //set madicine time
            c.set(Calendar.HOUR, helper.get(Calendar.HOUR))
            c.set(Calendar.MINUTE, helper.get(Calendar.MINUTE))
            c.set(Calendar.SECOND, helper.get(Calendar.SECOND))

            medicine.time = c.timeInMillis
            fragBinding.timeTextInput.text = DateFormat.format("HH:mm", helper).toString()
        })


        dateTimePickerViewModel.getDate().observe(viewLifecycleOwner, Observer { date ->
            val helper = Calendar.getInstance()
            helper.timeInMillis = date

            //set madicine date
            c.set(Calendar.YEAR, helper.get(Calendar.YEAR))
            c.set(Calendar.MONTH, helper.get(Calendar.MONTH))
            c.set(Calendar.DAY_OF_MONTH, helper.get(Calendar.DAY_OF_MONTH))

            medicine.time = c.timeInMillis
            fragBinding.dateTextInput.text = DateFormat.format("dd MMMM yyyy", helper).toString()
        })

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


    private fun validateForm(): Boolean {


        var valid = true
        if (fragBinding.medicineNameInput.text!!.isEmpty()) {
            fragBinding.medicineNameInput.requestFocus()
            fragBinding.medicineNameInput.error = "Required."
            valid = false
        }

        if (fragBinding.amountInputField.text.toString() == "") fragBinding.amountInputField.setText(
            "0"
        )
    //  fragBinding.medicineTypeChooser.keyListener = null


        if (fragBinding.amountInputField.text.toString().toInt() <= 0) {
            fragBinding.amountInputField.requestFocus()
            fragBinding.amountInputField.error = "Please enter an amount great than 0"
            valid = false
        }

        if (fragBinding.medicineTypeChooser.text.toString() == "") fragBinding.medicineTypeChooser.setText(
            "Pick a Type"
        )

      if (fragBinding.medicineTypeChooser.text!!.isEmpty()) {
          fragBinding.medicineTypeChooser.requestFocus()
          fragBinding.medicineTypeChooser.error = "plEASE Pick a type."
          valid = false
      }
        //medicine.type = fragBinding.medicineTypeChooser.adapter.getItem(i).toString()
            return valid
        }


}






