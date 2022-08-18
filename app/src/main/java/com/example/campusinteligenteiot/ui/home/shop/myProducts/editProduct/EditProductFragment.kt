package com.example.campusinteligenteiot.ui.home.shop.myProducts.editProduct

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.campusinteligenteiot.R

class EditProductFragment : Fragment() {

    companion object {
        fun newInstance() = EditProductFragment()
    }

    private lateinit var viewModel: EditProductViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.edit_product_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(EditProductViewModel::class.java)
        // TODO: Use the ViewModel
    }

}