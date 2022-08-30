package com.example.campusinteligenteiot.ui.drawer.chats

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.fragment.app.viewModels
import com.example.campusinteligenteiot.R
import com.example.campusinteligenteiot.api.model.user.UsersResponse
import com.example.campusinteligenteiot.databinding.ChannelsFragmentBinding
import com.example.campusinteligenteiot.ui.home.main.MainHomeViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson
import io.getstream.chat.android.client.ChatClient
import io.getstream.chat.android.client.models.Filters
import io.getstream.chat.android.client.models.User
import io.getstream.chat.android.client.models.image
import io.getstream.chat.android.client.models.name
import io.getstream.chat.android.ui.channel.list.header.viewmodel.ChannelListHeaderViewModel
import io.getstream.chat.android.ui.channel.list.header.viewmodel.bindView
import io.getstream.chat.android.ui.channel.list.viewmodel.ChannelListViewModel
import io.getstream.chat.android.ui.channel.list.viewmodel.bindView
import io.getstream.chat.android.ui.channel.list.viewmodel.factory.ChannelListViewModelFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class ChannelsFragment : Fragment() {

    private  var _binding: ChannelsFragmentBinding? = null
    private val binding get() = _binding!!
    val idUser = Firebase.auth.currentUser?.uid.toString()
    private val client = ChatClient.instance()

    private val viewModel by viewModels<ChannelsViewModel>()
    private lateinit var currentUser: UsersResponse

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = ChannelsFragmentBinding.inflate(inflater,container,false)

        val sharedPreferences = requireContext().getSharedPreferences("MY_PREF", Context.MODE_PRIVATE)
        val gson = Gson()
        val json = sharedPreferences.getString("current_user", "")
        currentUser = gson.fromJson(json, UsersResponse::class.java)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        GlobalScope.launch(Dispatchers.Main) {
            //observeData()

            val user = User(id = currentUser.userName).apply {
                name = currentUser.userName
            }
            val token = client.devToken(user.id)
            println("id = ${currentUser.userName}\n token = $token")

            client.connectUser(
                user = user,
                token = token
            ).enqueue()

            // Step 3 - Set the channel list filter and order
            // This can be read as requiring only channels whose "type" is "messaging" AND
            // whose "members" include our "user.id"
            val filter = Filters.and(
                Filters.eq("type", "messaging"),
                Filters.`in`("members", listOf(user.id))
            )
            val viewModelFactory = ChannelListViewModelFactory(filter, ChannelListViewModel.DEFAULT_SORT)
            val viewModel: ChannelListViewModel by viewModels { viewModelFactory }

            // Step 4 - Connect the ChannelListViewModel to the ChannelListView, loose
            //          coupling makes it easy to customize
            viewModel.bindView(binding.channelListView, viewLifecycleOwner)
            binding.channelListView.setChannelItemClickListener { channel ->
                startActivity(ChatActivity.newIntent(requireContext(), channel))
            }
        }


        /*// Step 1 - Set up the client for API calls and the domain for offline storage
        val client = ChatClient.Builder("b67pax5b2wdq", applicationContext)
            .logLevel(ChatLogLevel.ALL) // Set to NOTHING in prod
            .build()
        ChatDomain.Builder(client, applicationContext).build()        // Step 2 - Authenticate and connect the user
        val user = User(id = "tutorial-droid").apply {
            name = "Tutorial Droid"
            image = "https://bit.ly/2TIt8NR"
        }
        */
    }

}