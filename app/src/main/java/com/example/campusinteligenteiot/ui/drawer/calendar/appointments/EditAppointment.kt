package com.example.campusinteligenteiot.ui.drawer.calendar.appointments

import android.app.AlertDialog
import android.os.Build
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.campusinteligenteiot.R
import com.example.campusinteligenteiot.api.model.appointments.AppointmentsCall
import com.example.campusinteligenteiot.api.model.user.UsersResponse
import com.example.campusinteligenteiot.databinding.CalendarFragmentBinding
import com.example.campusinteligenteiot.databinding.EditAppointmentFragmentBinding
import com.example.campusinteligenteiot.databinding.EditProfileFragmentBinding
import com.example.campusinteligenteiot.model.Appointment
import com.example.campusinteligenteiot.ui.drawer.calendar.utils.CalendarUtils
import com.example.campusinteligenteiot.ui.drawer.calendar.utils.TimePickerFragment
import kotlinx.android.synthetic.main.generic_dialog_1_button.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter

class EditAppointment : Fragment() {

    private var _binding: EditAppointmentFragmentBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<EditAppointmentViewModel>()
    lateinit var appointmentTime: String
    private lateinit var userId: String
    private lateinit var currentUser: UsersResponse

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = EditAppointmentFragmentBinding.inflate(inflater,container,false)

        userId = arguments?.getString("userId")!!
        GlobalScope.launch(Dispatchers.Main){
            currentUser = viewModel.getUser(userId)
        }

        binding.eventDateTV.text =
            "Fecha: ${CalendarUtils.formattedDate(CalendarUtils.selectedDate!!)}"
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.saveButton.setOnClickListener{
            if(binding.eventNameET.text.isEmpty()){
                val builder = AlertDialog.Builder(context)
                val myView = LayoutInflater.from(context).inflate(R.layout.generic_dialog_1_button, null)
                builder.setView(myView)
                val dialog = builder.create()

                myView.Question.text = getString(R.string.not_possible_to_add)
                myView.Question2.text = getString(R.string.must_add_name)

                dialog.show()

                myView.cancelButton.setOnClickListener {
                    dialog.cancel()
                }
            }
            else if(binding.eventTimeTV.text.isEmpty()){
                val builder = AlertDialog.Builder(context)
                val myView = LayoutInflater.from(context).inflate(R.layout.generic_dialog_1_button, null)
                builder.setView(myView)
                val dialog = builder.create()

                myView.Question.text = getString(R.string.not_possible_to_add)
                myView.Question2.text = getString(R.string.must_add_hour)

                dialog.show()

                myView.cancelButton.setOnClickListener {
                    dialog.cancel()
                }
            }
            else{
                saveAppointmentAction()
            }
        }

        binding.eventTimeTV.setOnClickListener {
            showTimePickerDialog()
        }

        binding.backButton.setOnClickListener {
            findNavController().navigate(R.id.action_editAppointment_to_calendarFragment)
        }
    }

    private fun showTimePickerDialog() {
        val timePicker = TimePickerFragment{onTimeSelected(it)}
        timePicker.show(parentFragmentManager, "time")
    }

    private fun onTimeSelected(time: String){
        binding.eventTimeTV.setText(getString(R.string.you_have_Selected) + ' ' + time)
        appointmentTime = time
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun saveAppointmentAction() {
        val appointmentName = binding.eventNameET.text.toString()
        println(appointmentName)
        val appointment = Appointment(appointmentName, CalendarUtils.selectedDate!!, appointmentTime, userId)
        Appointment.appointmentList.add(appointment)
        if(currentUser.appointmentsDates == null){
            currentUser.appointmentsDates = arrayListOf(toString(CalendarUtils.selectedDate!!))
            currentUser.appointmentsHours = arrayListOf(appointmentTime)
            currentUser.appointmentsTitles = arrayListOf(appointmentName)
        }
        else{
            currentUser.appointmentsDates.add(toString(CalendarUtils.selectedDate!!))
            currentUser.appointmentsHours.add(appointmentTime)
            currentUser.appointmentsTitles.add(appointmentName)
        }

        val appointments = AppointmentsCall(currentUser.appointmentsDates, currentUser.appointmentsHours,
        currentUser.appointmentsTitles)

        GlobalScope.launch(Dispatchers.Main){
            viewModel.saveAppointments(appointments, currentUser.id)
            findNavController().navigate(
                R.id.action_editAppointment_to_calendarFragment
            )
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun toString(date: LocalDate): String{
        val formatters: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        return date.format(formatters)
    }

}