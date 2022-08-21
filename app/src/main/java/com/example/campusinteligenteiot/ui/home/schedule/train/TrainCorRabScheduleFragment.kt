package com.example.campusinteligenteiot.ui.home.schedule.train

import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.campusinteligenteiot.R
import com.example.campusinteligenteiot.databinding.TrainCorRabScheduleFragmentBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.*
import kotlin.collections.ArrayList

class TrainCorRabScheduleFragment : Fragment() {
    private  var _binding: TrainCorRabScheduleFragmentBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<TrainScheduleViewModel>()
    private val SCHEDULE_ID = "CordobaRabanales"
    private var departuresInSeconds: Array<Long> = Array(18) {1}

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
            departuresInSeconds = transformDeparturesToSecond(fullSchedule[0])
            val remainingTime = calculateNextDeparture(departuresInSeconds)
            println("Quedan: $remainingTime segundos para la proxima salida")
            playChrono(remainingTime)
            fillTitles()
            fillTableLayout(fullSchedule)
        }
    }

    private fun playChrono(remainingTime: Long) {
        if(remainingTime == 0L){
            binding.chronometerText.text = "No hay próximas salidas"
            binding.chronometer.text = "Espere al día siguiente"
        }
        else {

            var remainingTimeMiliSeconds = remainingTime * 1000
            object : CountDownTimer(remainingTimeMiliSeconds, 1000) {
                override fun onTick(millisUntilFinished: Long) {
                    var timeInSeconds = (millisUntilFinished / 1000).toInt() + 1
                    val hours = timeInSeconds / 3600
                    timeInSeconds = timeInSeconds % 3600
                    val minutes = timeInSeconds / 60
                    timeInSeconds = timeInSeconds % 60

                    binding.chronometer.text =
                        hours.toString().padStart(2, '0') + ":" + minutes.toString()
                            .padStart(2, '0') + ":" + timeInSeconds.toString().padStart(2, '0')
                }

                override fun onFinish() {
                    calculateNextDeparture(departuresInSeconds)
                }

            }.start()
        }

    }

    private fun transformDeparturesToSecond(departures: java.util.ArrayList<String>?): Array<Long> {
        val departuresInSeconds: Array<Long> = Array(departures!!.size) { 1 }

        for(i in (0 until departures.size)){
            val parts = departures[i].split(":")
            departuresInSeconds[i] = ((parts[0].toLong())*3600 + (parts[1].toLong())*60)
        }

        return departuresInSeconds
    }

    private fun calculateNextDeparture(departures: Array<Long>): Long {
        var remainingTime: Long = 0
        val cal: Calendar = Calendar.getInstance()
        val seconds: Int = cal.get(Calendar.SECOND)
        val minutes: Int = cal.get(Calendar.MINUTE)
        val hour = cal.get(Calendar.HOUR_OF_DAY);
        val timeInSeconds = hour*3600 + minutes*60 + seconds

        for(i in (departures.indices)){
            println("Segundos actuales: $timeInSeconds")
            println("Tiempo en segundos salida: " + departures[i])
            if(timeInSeconds < departures[i]){
                remainingTime = departures[i] - timeInSeconds
                println("Diferencia: $remainingTime")
                break
            }
        }
        return remainingTime
    }

    private fun fillTitles() {
        val titles = LayoutInflater.from(context).inflate(R.layout.row_table_edit_title, null, false)
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