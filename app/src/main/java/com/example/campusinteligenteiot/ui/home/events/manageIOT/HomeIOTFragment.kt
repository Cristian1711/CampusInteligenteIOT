package com.example.campusinteligenteiot.ui.home.events.manageIOT

import android.app.AlertDialog
import android.app.PendingIntent
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
import java.lang.Exception


class HomeIOTFragment : Fragment() {
    private  var _binding: HomeIOTFragmentBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<HomeIOTViewModel>()
    private val nfcAdapter: NfcAdapter? by lazy {
        NfcAdapter.getDefaultAdapter(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = HomeIOTFragmentBinding.inflate(inflater,container,false)

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

                    val bundle = bundleOf(
                        "eventId" to idEvent
                    )
                    val navController = Navigation.findNavController(requireView())
                    navController.navigate(R.id.action_generateQRFragment_to_friendsProfileFragment2, bundle)
                }
            } else {
                super.onActivityResult(requestCode, resultCode, data)
            }
    }

}