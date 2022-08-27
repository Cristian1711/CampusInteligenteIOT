package com.example.campusinteligenteiot.ui.home

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.nfc.NdefMessage
import android.nfc.NfcAdapter
import android.os.Bundle
import android.text.Html
import android.text.Spanned
import androidx.activity.viewModels
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.bumptech.glide.Glide
import com.example.campusinteligenteiot.R
import com.example.campusinteligenteiot.api.model.event.EventCall
import com.example.campusinteligenteiot.api.model.event.EventResponse
import com.example.campusinteligenteiot.api.model.user.UserProvider
import com.example.campusinteligenteiot.api.model.user.UsersResponse
import com.example.campusinteligenteiot.databinding.ActivityHomeBinding
import com.example.campusinteligenteiot.ui.home.main.MainHomeViewModel
import com.google.android.material.navigation.NavigationView
import com.google.firebase.storage.FirebaseStorage
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.nav_header_drawer.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding
    private lateinit var navController: NavController
    private lateinit var user: UsersResponse
    private val viewModel by viewModels<MainHomeViewModel>()
    private var nfcAdapter : NfcAdapter? = null
    private lateinit var event: EventResponse
    private lateinit var eventCall: EventCall

    // Pending intent for NFC intent foreground dispatch.
    // Used to read all NDEF tags while the app is running in the foreground.
    private var nfcPendingIntent: PendingIntent? = null
    // Optional: filter NDEF tags this app receives through the pending intent.
    //private var nfcIntentFilters: Array<IntentFilter>? = null


    private val KEY_LOG_TEXT = "logText"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = findViewById(R.id.nav_view)
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navViewDrawer: NavigationView = findViewById(R.id.navigation_view)

        //val navController = findNavController(R.id.fragmentContainerView2)
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment_activity_home) as NavHostFragment
        navController = navHostFragment.navController
        val appBarConfiguration = AppBarConfiguration(
            setOf(R.id.navigation_car,R.id.navigation_shop,R.id.navigation_map, R.id.navigation_events,R.id.navigation_schedule)
        )

        navView.setupWithNavController(navController)
        navViewDrawer.setupWithNavController(navController)

        viewModel.onCreate()
        viewModel.resultUsers.observe(this, Observer {

            UserProvider.users = viewModel.resultUsers.value!!
            println("Lista de usuarios")
            println(UserProvider.users)
            GlobalScope.launch(Dispatchers.Main) {
                user = viewModel.getUserFromLocal(viewModel.getId())
                println("DATOS USUARIO DE BASE DE DATOS LOCAL")
                println(user.name)
                println(user.collegeDegree)
                println(user.surname)
                val media = user.profileImage
                val storageReference = FirebaseStorage.getInstance()
                val gsReference = storageReference.getReferenceFromUrl(media!!)
                gsReference.downloadUrl.addOnSuccessListener {
                    Glide.with(this@HomeActivity).load(it).into(binding.navigationView.getHeaderView(0).imageView)
                    Glide.with(this@HomeActivity).load(it).into(binding.DrawerButton)
                    binding.navigationView.getHeaderView(0).textView2.setText(user.name)
                }

                val sharedPreferences = baseContext.getSharedPreferences("MY_PREF", Context.MODE_PRIVATE)
                val editor = sharedPreferences.edit()
                val gson = Gson()
                val json = gson.toJson(user)
                editor.putString("current_user", json)
                editor.commit()
            }


        })

        binding.DrawerButton.setOnClickListener {
            val sharedPreferences = baseContext.getSharedPreferences("MY_PREF", Context.MODE_PRIVATE)
            val gson = Gson()
            val json = sharedPreferences.getString("current_user", "")
            user = gson.fromJson(json, UsersResponse::class.java)
            val media = user.profileImage
            val storageReference = FirebaseStorage.getInstance()
            val gsReference = storageReference.getReferenceFromUrl(media!!)
            gsReference.downloadUrl.addOnSuccessListener {
                Glide.with(this@HomeActivity).load(it).into(binding.navigationView.getHeaderView(0).imageView)
                binding.navigationView.getHeaderView(0).textView2.setText(user.name)
            }
            drawerLayout.openDrawer(GravityCompat.START)
        }

        nfcAdapter = NfcAdapter.getDefaultAdapter(this)

        // Read all tags when app is running and in the foreground
        // Create a generic PendingIntent that will be deliver to this activity. The NFC stack
        // will fill in the intent with the details of the discovered tag before delivering to
        // this activity.
        nfcPendingIntent = PendingIntent.getActivity(this, 0,
            Intent(this, javaClass).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0)

        // Optional: Setup an intent filter from code for a specific NDEF intent
        // Use this code if you are only interested in a specific intent and don't want to
        // interfere with other NFC tags.
        // In this example, the code is commented out so that we get all NDEF messages,
        // in order to analyze different NDEF-formatted NFC tag contents.
        //val ndef = IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED)
        //ndef.addCategory(Intent.CATEGORY_DEFAULT)
        //ndef.addDataScheme("https")
        //ndef.addDataAuthority("*.andreasjakl.com", null)
        //ndef.addDataPath("/", PatternMatcher.PATTERN_PREFIX)
        // More information: https://stackoverflow.com/questions/30642465/nfc-tag-is-not-discovered-for-action-ndef-discovered-action-even-if-it-contains
        //nfcIntentFilters = arrayOf(ndef)

        if (intent != null) {
            processIntent(intent)
        }
        //setupActionBarWithNavController(navController, appBarConfiguration)


    }

    private fun processIntent(checkIntent: Intent) {
        // Check if intent has the action of a discovered NFC tag
        // with NDEF formatted contents
        if (checkIntent.action == NfcAdapter.ACTION_NDEF_DISCOVERED) {

            // Retrieve the raw NDEF message from the tag
            val rawMessages = checkIntent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES)

            // Complete variant: parse NDEF messages
            if (rawMessages != null) {
                val messages = arrayOfNulls<NdefMessage?>(rawMessages.size)// Array<NdefMessage>(rawMessages.size, {})
                for (i in rawMessages.indices) {
                    messages[i] = rawMessages[i] as NdefMessage;
                }
                // Process the messages array.
                processNdefMessages(messages)
            }

            // Simple variant: assume we have 1x URI record
            //if (rawMessages != null && rawMessages.isNotEmpty()) {
            //    val ndefMsg = rawMessages[0] as NdefMessage
            //    if (ndefMsg.records != null && ndefMsg.records.isNotEmpty()) {
            //        val ndefRecord = ndefMsg.records[0]
            //        if (ndefRecord.toUri() != null) {
            //            logMessage("URI detected", ndefRecord.toUri().toString())
            //        } else {
            //            // Other NFC Tags
            //            logMessage("Payload", ndefRecord.payload.contentToString())
            //        }
            //    }
            //}

        }
    }

    private fun processNdefMessages(ndefMessages: Array<NdefMessage?>) {
        // Go through all NDEF messages found on the NFC tag
        for (curMsg in ndefMessages) {
            if (curMsg != null) {
                val msg = String(curMsg.records[0].payload)
                // Loop through all the records contained in the message
                        // URI NDEF Tag
                        val eventId = msg.substring(3,msg.length)
                        if (eventId != null){
                            GlobalScope.launch(Dispatchers.Main) {
                                event = viewModel.getEvent(eventId)
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

                                val bundle = bundleOf(
                                    "eventId" to eventId
                                )
                                println("HE RECOGIDO EL ID $eventId")

                                navController.navigate(R.id.eventInformationFragment, bundle)
                            }
                        }

            }
        }
    }

    fun toSimpleString(date: Date?) = with(date ?: Date()) {
        SimpleDateFormat("yyyy-MM-dd").format(this)
    }

    override fun onResume() {
        super.onResume()
        // Get all NDEF discovered intents
        // Makes sure the app gets all discovered NDEF messages as long as it's in the foreground.
        nfcAdapter?.enableForegroundDispatch(this, nfcPendingIntent, null, null);
        // Alternative: only get specific HTTP NDEF intent
        //nfcAdapter?.enableForegroundDispatch(this, nfcPendingIntent, nfcIntentFilters, null);
    }

    override fun onPause() {
        super.onPause()
        // Disable foreground dispatch, as this activity is no longer in the foreground
        nfcAdapter?.disableForegroundDispatch(this);
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        // If we got an intent while the app is running, also check if it's a new NDEF message
        // that was discovered
        if (intent != null) processIntent(intent)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        //outState?.putCharSequence(KEY_LOG_TEXT, tv_messages.text)
        if (outState != null) {
            super.onSaveInstanceState(outState)
        }
    }

    private fun fromHtml(html: String): Spanned {
        return if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            Html.fromHtml(html, Html.FROM_HTML_MODE_LEGACY)
        } else {
            @Suppress("DEPRECATION")
            Html.fromHtml(html)
        }
    }


}