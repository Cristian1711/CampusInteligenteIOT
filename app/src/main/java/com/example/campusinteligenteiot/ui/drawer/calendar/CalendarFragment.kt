package com.example.campusinteligenteiot.ui.drawer.calendar

import android.content.Context
import android.os.Build
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.campusinteligenteiot.R
import com.example.campusinteligenteiot.api.model.user.UsersResponse
import com.example.campusinteligenteiot.databinding.CalendarFragmentBinding
import com.example.campusinteligenteiot.model.Appointment
import com.example.campusinteligenteiot.ui.drawer.calendar.appointments.AppointmentAdapter
import com.example.campusinteligenteiot.ui.drawer.calendar.utils.CalendarAdapter
import com.example.campusinteligenteiot.ui.drawer.calendar.utils.CalendarUtils
import com.example.campusinteligenteiot.ui.drawer.calendar.utils.CalendarUtils.daysInWeekArray
import com.example.campusinteligenteiot.ui.drawer.calendar.utils.CalendarUtils.monthYearFromDate
import com.google.gson.Gson
import java.time.LocalDate
import java.util.ArrayList

class CalendarFragment : Fragment(), CalendarAdapter.OnItemListener{

    private var _binding:CalendarFragmentBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<CalendarViewModel>()
    private var dailyAppointments:List<Appointment?>? = null
    private lateinit var currentUser: UsersResponse

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = CalendarFragmentBinding.inflate(inflater,container,false)

        val sharedPreferences = requireContext().getSharedPreferences("MY_PREF", Context.MODE_PRIVATE)
        val gson = Gson()
        val json = sharedPreferences.getString("current_user", "")
        currentUser = gson.fromJson(json, UsersResponse::class.java)


        CalendarUtils.selectedDate = LocalDate.now()
        dailyAppointments = Appointment.appointmentsForDate(CalendarUtils.selectedDate)
        setWeekView()

        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initAppointmentsRecyclerView(requireView())

        binding.nextWeekButton.setOnClickListener{
            nextWeekAction(view)
        }

        binding.previousWeekButton.setOnClickListener{
            previousWeekAction(view)
        }

        binding.newEventButton.setOnClickListener{
            gotoEditAppointment()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun previousWeekAction(view:View){
        CalendarUtils.selectedDate = CalendarUtils.selectedDate?.minusWeeks(1)
        dailyAppointments = Appointment.appointmentsForDate(CalendarUtils.selectedDate)
        setWeekView()
        initAppointmentsRecyclerView(requireView())
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun nextWeekAction(view: View){
        CalendarUtils.selectedDate = CalendarUtils.selectedDate?.plusWeeks(1)
        dailyAppointments = Appointment.appointmentsForDate(CalendarUtils.selectedDate)
        setWeekView()
        initAppointmentsRecyclerView(requireView())
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onItemClick(position: Int, date: LocalDate?) {
        CalendarUtils.selectedDate = date
        dailyAppointments = Appointment.appointmentsForDate(CalendarUtils.selectedDate)
        setWeekView()
        initAppointmentsRecyclerView(requireView())
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun setWeekView(){
        println("SELECTED DATE")
        binding.monthYearTV.text = monthYearFromDate(CalendarUtils.selectedDate!!)
        println(CalendarUtils.selectedDate)
        val days:ArrayList<LocalDate> = daysInWeekArray(CalendarUtils.selectedDate!!)
        println("EVENTOS PARA ESE DIA")
        println(dailyAppointments)
        val calendarAdapter = CalendarAdapter(days, this)
        val layoutManager = GridLayoutManager(context, 7)
        binding.calendarRecyclerView.layoutManager = layoutManager
        binding.calendarRecyclerView.adapter = calendarAdapter
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun initAppointmentsRecyclerView(view: View){
        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerAppointments)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        println("LOS EVENTOS EN EL RECYCLER VIEW DE EVENTOS ES")
        println(dailyAppointments)
        recyclerView.adapter = AppointmentAdapter(dailyAppointments)
    }

    fun gotoEditAppointment(){
        val bundle = bundleOf(
            "userId" to currentUser.id
        )
        findNavController().navigate(R.id.action_calendarFragment_to_editAppointment, bundle)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onResume() {
        super.onResume()
        initAppointmentsRecyclerView(requireView())
    }

}