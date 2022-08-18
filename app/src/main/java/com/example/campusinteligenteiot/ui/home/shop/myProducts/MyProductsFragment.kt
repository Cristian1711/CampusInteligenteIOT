package com.example.campusinteligenteiot.ui.home.shop.myProducts

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.campusinteligenteiot.R
import com.example.campusinteligenteiot.databinding.MyProductsFragmentBinding
import com.example.campusinteligenteiot.databinding.ProductDetailFragmentBinding
import com.example.campusinteligenteiot.ui.home.shop.detail.ProductDetailViewModel

class MyProductsFragment : Fragment() {

    private  var _binding: MyProductsFragmentBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<MyProductsViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = MyProductsFragmentBinding.inflate(inflater,container,false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

}