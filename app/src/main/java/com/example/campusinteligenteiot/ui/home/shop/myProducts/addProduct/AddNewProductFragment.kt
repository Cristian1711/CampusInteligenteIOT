package com.example.campusinteligenteiot.ui.home.shop.myProducts.addProduct

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.campusinteligenteiot.R

class AddNewProductFragment : Fragment() {

    companion object {
        fun newInstance() = AddNewProductFragment()
    }

    private lateinit var viewModel: AddNewProductViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.add_new_product_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(AddNewProductViewModel::class.java)
        // TODO: Use the ViewModel
    }

}