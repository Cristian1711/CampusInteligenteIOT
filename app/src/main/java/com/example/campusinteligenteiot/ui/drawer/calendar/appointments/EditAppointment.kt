package com.example.campusinteligenteiot.ui.drawer.calendar.appointments

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
import com.example.campusinteligenteiot.databinding.CalendarFragmentBinding
import com.example.campusinteligenteiot.databinding.EditAppointmentFragmentBinding
import com.example.campusinteligenteiot.databinding.EditProfileFragmentBinding
import com.example.campusinteligenteiot.model.Appointment
import com.example.campusinteligenteiot.ui.drawer.calendar.utils.CalendarUtils
import com.example.campusinteligenteiot.ui.drawer.calendar.utils.TimePickerFragment
import java.time.LocalTime

class EditAppointment : Fragment() {

    private var _binding: EditAppointmentFragmentBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<EditAppointmentViewModel>()
    lateinit var appointmentTime: String

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = EditAppointmentFragmentBinding.inflate(inflater,container,false)

        binding.eventDateTV.text = "Fecha: " + CalendarUtils.formattedDate(CalendarUtils.selectedDate!!)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.saveButton.setOnClickListener{
            saveAppointmentAction()
        }

        binding.eventTimeTV.setOnClickListener {
            showTimePickerDialog()
        }
    }

    private fun showTimePickerDialog() {
        val timePicker = TimePickerFragment{onTimeSelected(it)}
        timePicker.show(parentFragmentManager, "time")
    }

    private fun onTimeSelected(time: String){
        binding.eventTimeTV.setText("Has seleccionado las $time")
        appointmentTime = time
    }

    private fun saveAppointmentAction() {
        val appointmentName = binding.eventNameET.text.toString()
        println(appointmentName)
        val appointment = Appointment(appointmentName, CalendarUtils.selectedDate!!, appointmentTime)
        Appointment.appointmentList.add(appointment)
        findNavController().navigate(
            R.id.action_editAppointment_to_calendarFragment
        )
    }

}