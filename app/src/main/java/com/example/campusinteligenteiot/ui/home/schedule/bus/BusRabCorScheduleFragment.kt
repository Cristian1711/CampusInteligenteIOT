package com.example.campusinteligenteiot.ui.home.schedule.bus

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.campusinteligenteiot.R
import com.example.campusinteligenteiot.databinding.FragmentBusRabCorScheduleBinding
import java.text.SimpleDateFormat
import java.util.*

class BusRabCorScheduleFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private  var _binding: FragmentBusRabCorScheduleBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<BusScheduleViewModel>()
    private lateinit var uri: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentBusRabCorScheduleBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val dateFormat = SimpleDateFormat("yyyy-MM-ddHH:mm:ssZ", Locale.getDefault())
        val date = Date()

        val fecha: String = dateFormat.format(date)

        try {

            // Assume that Moovit app exists. If not, exception will occur
            val pm = requireContext().packageManager
            pm.getPackageInfo("com.tranzmate", PackageManager.GET_ACTIVITIES)

        } catch (e: PackageManager.NameNotFoundException) {

            // Moovit not installed - send to store
            val url = "https://moovit.onelink.me/3986059930?pid=Developers&c=CampusInteligenteIOT"
            val i = Intent(Intent.ACTION_VIEW)
            i.data = Uri.parse(url)
            startActivity(i)
        }

        binding.btnNearbyLines.setOnClickListener{
            uri = "moovit://nearby?lat=&lon=&partner_id=CampusInteligenteIOT"
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(uri)
            startActivity(intent)
        }
        binding.btnGetRoutes.setOnClickListener{
            uri = "moovit://directions?dest_lat=37.894266&dest_lon=-4.782051&dest_name=Cordoba&orig_lat=&orig_lon=&orig_name=Posicion%20Actual&auto_run=true&date=$fecha&partner_id=CampusInteligenteIOT"
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(uri)
            startActivity(intent)
        }



    }
}