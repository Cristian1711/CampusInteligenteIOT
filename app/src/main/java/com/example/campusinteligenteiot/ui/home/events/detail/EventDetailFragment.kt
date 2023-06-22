package com.example.campusinteligenteiot.ui.home.events.detail

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.app.AlertDialog
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.nfc.NdefMessage
import android.nfc.NfcAdapter
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.provider.Settings
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import com.airbnb.lottie.LottieAnimationView
import com.bumptech.glide.Glide
import com.example.campusinteligenteiot.R
import com.example.campusinteligenteiot.api.model.event.EventCall
import com.example.campusinteligenteiot.api.model.event.EventResponse
import com.example.campusinteligenteiot.api.model.product.ProductResponse
import com.example.campusinteligenteiot.api.model.user.UsersResponse
import com.example.campusinteligenteiot.databinding.EventDetailFragmentBinding
import com.example.campusinteligenteiot.databinding.NewEventSuggestFragmentBinding
import com.example.campusinteligenteiot.ui.home.events.suggests.NewEventSuggestViewModel
import com.google.firebase.Timestamp
import com.google.firebase.storage.FirebaseStorage
import com.google.gson.Gson
import com.google.zxing.BarcodeFormat
import com.google.zxing.integration.android.IntentIntegrator
import com.journeyapps.barcodescanner.BarcodeEncoder
import kotlinx.android.synthetic.main.dialog_qr.view.*
import kotlinx.android.synthetic.main.generic_dialog_1_button.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class EventDetailFragment : Fragment() {
    private var eventId: String? = null
    private  var _binding: EventDetailFragmentBinding? = null
    private val binding get() = _binding!!
    private lateinit var event: EventResponse
    private val viewModel by viewModels<EventDetailViewModel>()
    private lateinit var currentUser: UsersResponse
    private var willAssist: Boolean? = false
    private lateinit var dateString: String
    var currentDate = Date()
    private val nfcAdapter: NfcAdapter? by lazy {
        NfcAdapter.getDefaultAdapter(context)
    }
    private var pendingIntent: PendingIntent? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = EventDetailFragmentBinding.inflate(inflater,container,false)

        eventId = arguments?.getString("eventId")
        willAssist = arguments?.getBoolean("willAssist")
        val sharedPreferences = requireContext().getSharedPreferences("MY_PREF", Context.MODE_PRIVATE)
        val gson = Gson()
        val json = sharedPreferences.getString("current_user", "")
        currentUser = gson.fromJson(json, UsersResponse::class.java)

        GlobalScope.launch(Dispatchers.Main) {
            event = viewModel.getSingleEvent(eventId!!)
            dateString = toSimpleStringToText(event.eventDate)
            setEventData(event)
            loadImage(event.eventImage)
            prepareStarButton()
        }

        return binding.root
    }

    private fun prepareStarButton() {
        if(willAssist == true) binding.starImage.setImageResource(R.drawable.ic_star_yellow)
        binding.starImage.setOnClickListener{
            val eventCall = EventCall(
                event.assistants,
                event.attendances,
                toSimpleString(event.eventDate),
                event.description,
                event.eventImage,
                event.eventTitle,
                event.eventPlace,
                event.suggested
            )
            willAssist = starAnimation(binding.starImage, R.raw.star_animation, willAssist!!)
            if(willAssist == true){
                binding.StarText.text =
                    getString(R.string.star_text_2) + ' ' + dateString + "!"
                eventCall.attendances?.add(currentUser.id)
                GlobalScope.launch(Dispatchers.Main) {
                    viewModel.updateEvent(event.id, eventCall)
                }
            } else{
                binding.StarText.text = getString(R.string.star_text_1)
                eventCall.attendances?.remove(currentUser.id)
                GlobalScope.launch(Dispatchers.Main) {
                    viewModel.updateEvent(event.id, eventCall)
                }
            }
        }
    }

    private fun starAnimation(imageView: LottieAnimationView, animation: Int, willAssist: Boolean): Boolean {
        if(!willAssist){
            imageView.setAnimation(animation)
            imageView.playAnimation()
        }else{
            imageView.animate()
                .alpha(0f)
                .setDuration(200)
                .setListener(object: AnimatorListenerAdapter(){

                    override fun onAnimationEnd(animation: Animator?) {
                        imageView.setImageResource(R.drawable.grey_star)
                        imageView.alpha = 1f
                    }

                })

        }
        return !willAssist
    }

    private fun loadImage(eventImage: String) {
        val storageReference = FirebaseStorage.getInstance()
        val gsReference = storageReference.getReferenceFromUrl(eventImage)
        gsReference.downloadUrl.addOnSuccessListener {
            Glide.with(requireContext()).load(it).into(binding.EventImage)
        }
    }

    private fun setEventData(event: EventResponse) {
        binding.attendancesText.text = getString(R.string.assist_text) + " ${event.attendances.size} " + getString(R.string.person_text)
        binding.eventTitle.text = event.eventTitle
        binding.PlaceAndDateText.text = event.eventPlace + " - " + dateString
        binding.DescriptionText.text = event.description
        if(willAssist == true){
            binding.StarText.text =
                getString(R.string.star_text_2) + ' ' + dateString + "!"
        }
        else{
            binding.StarText.text = getString(R.string.star_text_1)
        }
    }

    fun toSimpleString(date: Date?) = with(date ?: Date()) {
        SimpleDateFormat("yyyy-MM-dd").format(this)
    }

    fun toSimpleStringToText(date: Date?) = with(date ?: Date()) {
        SimpleDateFormat("dd-MM-yyyy").format(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        readFromIntent(requireActivity().intent)
        pendingIntent = PendingIntent.getActivity(
            context, 0, Intent(context, javaClass).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0
        )
        val tagDetected = IntentFilter(NfcAdapter.ACTION_TAG_DISCOVERED)
        tagDetected.addCategory(Intent.CATEGORY_DEFAULT)

        binding.backButton.setOnClickListener{
            val navController = Navigation.findNavController(view)
            navController.navigate(R.id.action_eventDetail_to_navigation_events)
        }

        binding.AccessButton.setOnClickListener {
            if(getZeroTimeDate(event.eventDate).compareTo(getZeroTimeDate(currentDate)) > 0){
                val builder = AlertDialog.Builder(requireContext())
                val myView = layoutInflater.inflate(R.layout.generic_dialog_1_button, null)
                builder.setView(myView)
                val dialog = builder.create()
                dialog.show()

                myView.cancelButton.setOnClickListener{
                    dialog.cancel()
                }
            }
            else {
                showDefaultDialog()
            }
        }
    }

    private fun readFromIntent(intent: Intent) {
        val action = intent.action
        if (NfcAdapter.ACTION_TAG_DISCOVERED == action
            || NfcAdapter.ACTION_TECH_DISCOVERED == action || NfcAdapter.ACTION_NDEF_DISCOVERED == action
        ) {

            val parcelables = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES)
            with(parcelables) {
                val inNdefMessage = this?.get(0) as NdefMessage
                val inNdefRecords = inNdefMessage.records
                val ndefRecord_0 = inNdefRecords[0]

                val inMessage = String(ndefRecord_0.payload)
            }
        }
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
                navController.navigate(R.id.action_eventDetail_to_eventInformationFragment, bundle)
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
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

    fun getZeroTimeDate(date:Date): Date {
        var res: Date = date
        val calendar = Calendar.getInstance()

        calendar.time = date
        calendar[Calendar.HOUR_OF_DAY] = 0
        calendar[Calendar.MINUTE] = 0
        calendar[Calendar.SECOND] = 0
        calendar[Calendar.MILLISECOND] = 0

        res = calendar.time

        return res
    }

}