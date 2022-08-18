package com.example.campusinteligenteiot.ui.home.shop.myProducts

import android.content.Context
import androidx.lifecycle.ViewModelProvider
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
import com.example.campusinteligenteiot.databinding.MyProductsFragmentBinding
import com.example.campusinteligenteiot.databinding.ProductDetailFragmentBinding
import com.example.campusinteligenteiot.ui.home.shop.adapter.MyProductAdapter
import com.example.campusinteligenteiot.ui.home.shop.adapter.ProductAdapter
import com.example.campusinteligenteiot.ui.home.shop.detail.ProductDetailViewModel
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MyProductsFragment : Fragment() {

    private  var _binding: MyProductsFragmentBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<MyProductsViewModel>()
    private lateinit var user: UsersResponse
    private lateinit var adapter: MyProductAdapter
    private lateinit var recyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = MyProductsFragmentBinding.inflate(inflater,container,false)

        val sharedPreferences = requireContext().getSharedPreferences("MY_PREF", Context.MODE_PRIVATE)
        val gson = Gson()
        val json = sharedPreferences.getString("current_user", "")
        user = gson.fromJson(json, UsersResponse::class.java)

        GlobalScope.launch(Dispatchers.Main){
            observeData()
        }

        return binding.root
    }

    private suspend fun observeData() {
        viewModel.getAllProducts().observe(viewLifecycleOwner, Observer{
            adapter.setProductList(it)
            adapter.filterProductListByOwner(user.id)
            adapter.notifyDataSetChanged()
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initProductsRecyclerView(view)
    }

    private fun initProductsRecyclerView(view: View) {
        recyclerView = view.findViewById(R.id.productRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        adapter = MyProductAdapter(user, requireContext())
        recyclerView.adapter = adapter
    }

}