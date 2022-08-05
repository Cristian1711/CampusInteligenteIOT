package com.example.campusinteligenteiot.ui.home.shop

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.campusinteligenteiot.R
import com.example.campusinteligenteiot.api.model.product.ProductResponse
import com.example.campusinteligenteiot.databinding.ScheduleFragmentBinding
import com.example.campusinteligenteiot.databinding.ShopFragmentBinding
import com.example.campusinteligenteiot.repository.ProductRepository
import com.example.campusinteligenteiot.ui.home.schedule.ScheduleViewModel
import com.example.campusinteligenteiot.ui.home.shop.adapter.ProductAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class ShopFragment : Fragment() {

    private  var _binding: ShopFragmentBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<ShopViewModel>()
    private lateinit var arrProducts: List<ProductResponse>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = ShopFragmentBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        GlobalScope.launch(Dispatchers.Main) {
            arrProducts = viewModel.getAllProducts()!!
            initProductRecyclerView()
        }

    }

    private fun initProductRecyclerView() {
        //val manager = GridLayoutManager(requireContext(), 2)
        //val decoration = DividerItemDecoration(requireContext(), manager.orientation)
        val recyclerView = binding.productRecyclerView
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = ProductAdapter(arrProducts, requireContext()) { product ->
            onItemSelected(
                product
            )
        }
        //binding.productRecyclerView.addItemDecoration(decoration)
    }

    fun onItemSelected(product: ProductResponse){
        //se abre una ventana con el producto
    }

}