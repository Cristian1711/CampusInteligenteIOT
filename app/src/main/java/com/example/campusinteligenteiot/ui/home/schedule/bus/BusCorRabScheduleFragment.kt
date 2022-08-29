package com.example.campusinteligenteiot.ui.home.schedule.bus

import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import com.example.campusinteligenteiot.R
import com.example.campusinteligenteiot.databinding.BusScheduleFragmentBinding
import com.example.campusinteligenteiot.databinding.FragmentBusCorRabScheduleBinding
import kotlinx.android.synthetic.main.generic_dialog_1_button.view.*
import java.text.SimpleDateFormat
import java.util.*

class BusCorRabScheduleFragment : Fragment() {
    private  var _binding: FragmentBusCorRabScheduleBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<BusScheduleViewModel>()
    private lateinit var uri: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentBusCorRabScheduleBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val dateFormat = SimpleDateFormat("yyyy-MM-ddHH:mm:ssZ", Locale.getDefault())
        val date = Date()

        val fecha: String = dateFormat.format(date)

        binding.btnNearbyLines.setOnClickListener{
            uri = "moovit://nearby?lat=&lon=&partner_id=<CampusInteligente>"
            checkMoovit()
        }
        binding.btnGetRoutes.setOnClickListener{
            uri = "moovit://directions?dest_lat=37.912759&dest_lon=-4.720911&dest_name=Rabanales&orig_lat=&orig_lon=&orig_name=Posicion&auto_run=true&partner_id=<CampusInteligenteIOT>"
            checkMoovit()
        }


    }

    private fun checkMoovit() {
        try {

            // Assume that Moovit app exists. If not, exception will occur


            // Assume that Moovit app exists. If not, exception will occur
            val pm = requireContext().packageManager

            pm.getPackageInfo("com.tranzmate", PackageManager.GET_ACTIVITIES)


            val intent = Intent(Intent.ACTION_VIEW)

            intent.data = Uri.parse(uri)

            startActivity(intent)

        } catch (e: PackageManager.NameNotFoundException) {

            // Moovit not installed - send to store
            val builder = AlertDialog.Builder(context)
            val myView = LayoutInflater.from(context).inflate(R.layout.generic_dialog_1_button, null)
            builder.setView(myView)
            val dialog = builder.create()

            myView.Question.text = getString(R.string.not_possible_moovit)
            myView.Question2.text = getString(R.string.no_moovit_installed)

            dialog.show()

            myView.cancelButton.setOnClickListener {
                dialog.cancel()
            }

            Thread.sleep(4000)
            val url = "https://moovit.onelink.me/3986059930?pid=Developers&c=CampusInteligenteIOT"
            val i = Intent(Intent.ACTION_VIEW)
            i.data = Uri.parse(url)
            startActivity(i)
        }
    }
}