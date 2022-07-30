package com.example.campusinteligenteiot.ui.drawer.friends

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.campusinteligenteiot.R
import com.example.campusinteligenteiot.api.model.user.UsersResponse
import com.example.campusinteligenteiot.databinding.FriendsFragmentBinding
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class FriendsFragment : Fragment() {

    private  var _binding: FriendsFragmentBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<FriendsViewModel>()
    private lateinit var adapter: FriendsAdapter
    private lateinit var user: UsersResponse

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FriendsFragmentBinding.inflate(inflater,container,false)

        val sharedPreferences = requireContext().getSharedPreferences("MY_PREF", Context.MODE_PRIVATE)
        val gson = Gson()
        val json = sharedPreferences.getString("current_user", "")
        user = gson.fromJson(json, UsersResponse::class.java)

        GlobalScope.launch(Dispatchers.Main){
            observeData(user)
            binding.friendsNumber.text = user.friends.size.toString()
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initFriendsRecyclerView(view)
    }

    private fun initFriendsRecyclerView(view: View) {
        val recyclerView = view.findViewById<RecyclerView>(R.id.friendsRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        adapter = FriendsAdapter(requireContext())
        recyclerView.adapter = adapter
    }

    suspend fun observeData(user: UsersResponse){
        viewModel.getFriendsFromUser(user).observe(viewLifecycleOwner, Observer{
            adapter.setFriendList(it)
            adapter.notifyDataSetChanged()
        })
    }

}