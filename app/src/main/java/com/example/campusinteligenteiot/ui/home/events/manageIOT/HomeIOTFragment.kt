package com.example.campusinteligenteiot.ui.home.events.manageIOT

import android.app.AlertDialog
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.nfc.NdefMessage
import android.nfc.NfcAdapter
import android.nfc.Tag
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.os.Parcelable
import android.provider.Settings
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import com.example.campusinteligenteiot.R
import com.example.campusinteligenteiot.databinding.EventsFragmentBinding
import com.example.campusinteligenteiot.databinding.HomeIOTFragmentBinding
import com.example.campusinteligenteiot.ui.home.events.EventsViewModel
import com.google.zxing.integration.android.IntentIntegrator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.UnsupportedEncodingException
import java.nio.charset.Charset
import kotlin.experimental.and
import android.nfc.NdefRecord

import android.nfc.tech.Ndef
import com.example.campusinteligenteiot.api.model.event.EventCall
import com.example.campusinteligenteiot.api.model.event.EventResponse
import com.example.campusinteligenteiot.api.model.user.UsersResponse
import com.google.gson.Gson
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*


class HomeIOTFragment : Fragment() {
    private  var _binding: HomeIOTFragmentBinding? = null
    private val binding get() = _binding!!
    private lateinit var event: EventResponse
    private lateinit var eventCall: EventCall
    private lateinit var user: UsersResponse
    private val viewModel by viewModels<HomeIOTViewModel>()
    private val nfcAdapter: NfcAdapter? by lazy {
        NfcAdapter.getDefaultAdapter(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = HomeIOTFragmentBinding.inflate(inflater,container,false)

        val sharedPreferences = requireContext().getSharedPreferences("MY_PREF", Context.MODE_PRIVATE)
        val gson = Gson()
        val json = sharedPreferences.getString("current_user", "")
        user = gson.fromJson(json, UsersResponse::class.java)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.nfcButton.setOnClickListener {
            showDefaultDialog()
        }
    }

    private fun showDefaultDialog() {
        val alertDialog = AlertDialog.Builder(context)

        alertDialog.apply {
            setTitle(getString(R.string.connect_event))
            setMessage(getString(R.string.connect_method))
            setPositiveButton("QR") { _, _ ->
                initScanner()
            }
            setNegativeButton("NFC") { _, _ ->
                if (nfcAdapter == null) {
                    Toast.makeText(context, R.string.not_nfc_supported, Toast.LENGTH_SHORT).show()
                } else if (!nfcAdapter!!.isEnabled) {
                    Toast.makeText(context, R.string.nfc_disabled, Toast.LENGTH_SHORT).show()
                    startActivity(Intent(Settings.ACTION_NFC_SETTINGS))
                } else {
                    Toast.makeText(context, R.string.success_nfc, Toast.LENGTH_SHORT).show()
                }
            }
        }.create().show()
    }

    private fun initScanner() {
        val integrator = IntentIntegrator.forSupportFragment(this)
        integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE)
        integrator.setPrompt(getString(R.string.scan_qr))
        integrator.setCameraId(0)
        integrator.setBeepEnabled(false)
        integrator.setBarcodeImageEnabled(false)
        integrator.initiateScan()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)

        if (result != null) {
            if (result.contents == null) {
                Toast.makeText(context, getString(R.string.canceled), Toast.LENGTH_SHORT).show()
            } else {
                    val idEvent = result.contents.toString()

                    GlobalScope.launch(Dispatchers.Main) {
                        event = viewModel.getEvent(idEvent)
                        if(!event.assistants.contains(user.id)){
                            event.assistants.add(user.id)
                            eventCall = EventCall(
                                event.assistants,
                                event.attendances,
                                toSimpleString(event.eventDate),
                                event.description,
                                event.eventImage,
                                event.eventTitle,
                                event.eventPlace,
                                event.suggested
                            )

                            viewModel.saveEvent(event.id, eventCall)
                        }
                    }
                    val bundle = bundleOf(
                        "eventId" to idEvent
                    )
                    val navController = Navigation.findNavController(requireView())
                    navController.navigate(R.id.action_homeIOTFragment_to_eventInformationFragment, bundle)
                }
            } else {
                super.onActivityResult(requestCode, resultCode, data)
            }
    }

    fun toSimpleString(date: Date?) = with(date ?: Date()) {
        SimpleDateFormat("yyyy-MM-dd").format(this)
    }

}