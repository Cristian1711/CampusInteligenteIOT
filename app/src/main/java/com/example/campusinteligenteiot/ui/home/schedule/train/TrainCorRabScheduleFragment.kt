package com.example.campusinteligenteiot.ui.home.schedule.train

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.viewModels
import com.example.campusinteligenteiot.R
import com.example.campusinteligenteiot.databinding.TrainCorRabScheduleFragmentBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class TrainCorRabScheduleFragment : Fragment() {
    private  var _binding: TrainCorRabScheduleFragmentBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<TrainScheduleViewModel>()
    private val SCHEDULE_ID = "CordobaRabanales"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = TrainCorRabScheduleFragmentBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.tlTabla?.removeAllViews()

        GlobalScope.launch(Dispatchers.Main) {
            val schedule = viewModel.getSingleSchedule(SCHEDULE_ID)
            println("YA HE RECUPERADO EL HORARIO DE CORDOBA A RABANALES")
            println(schedule)
            val fullSchedule = arrayOf(
                schedule?.departures,
                schedule?.arrivals
            )
            fillTitles()
            fillTableLayout(fullSchedule)
        }
    }

    private fun fillTitles() {
        val titles = LayoutInflater.from(context).inflate(R.layout.row_table_edited, null, false)
        val departures = titles.findViewById<View>(R.id.departures) as TextView
        val arrivals = titles.findViewById<View>(R.id.arrivals) as TextView
        departures.text = getString(R.string.departures)
        arrivals.text = getString(R.string.arrivals)
        binding.tlTabla.addView(titles)
    }

    private fun fillTableLayout(fullSchedule: Array<ArrayList<String>?>){
        for(i in (0 until fullSchedule[0]!!.size)){
            val registro = LayoutInflater.from(context).inflate(R.layout.row_table_edited, null, false)
            val departures = registro.findViewById<View>(R.id.departures) as TextView
            val arrivals = registro.findViewById<View>(R.id.arrivals) as TextView
            departures.text = fullSchedule[0]?.get(i) ?: "Datos no disponibles"
            arrivals.text = fullSchedule[1]?.get(i) ?: "Datos no disponibles"
            binding.tlTabla.addView(registro)
        }
    }

}