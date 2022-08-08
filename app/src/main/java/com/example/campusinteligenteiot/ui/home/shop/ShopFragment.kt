package com.example.campusinteligenteiot.ui.home.shop

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.OvershootInterpolator
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.campusinteligenteiot.R
import com.example.campusinteligenteiot.api.model.product.ProductResponse
import com.example.campusinteligenteiot.api.model.user.UsersResponse
import com.example.campusinteligenteiot.databinding.ScheduleFragmentBinding
import com.example.campusinteligenteiot.databinding.ShopFragmentBinding
import com.example.campusinteligenteiot.repository.ProductRepository
import com.example.campusinteligenteiot.ui.home.schedule.ScheduleViewModel
import com.example.campusinteligenteiot.ui.home.shop.adapter.ProductAdapter
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class ShopFragment : Fragment() {

    private  var _binding: ShopFragmentBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<ShopViewModel>()
    private lateinit var arrProducts: List<ProductResponse>
    private lateinit var user: UsersResponse
    private val translationYaxis = 100f
    private var menuOpen = false
    private val interpolator: OvershootInterpolator = OvershootInterpolator()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = ShopFragmentBinding.inflate(inflater,container,false)

        val sharedPreferences = requireContext().getSharedPreferences("MY_PREF", Context.MODE_PRIVATE)
        val gson = Gson()
        val json = sharedPreferences.getString("current_user", "")
        user = gson.fromJson(json, UsersResponse::class.java)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        GlobalScope.launch(Dispatchers.Main) {
            arrProducts = viewModel.getAllProducts()!!
            initProductRecyclerView(user)
        }

        showMenu()

    }

    private fun showMenu() {
        binding.fabAddProduct.alpha = 0f
        binding.fabFavouriteProducts.alpha = 0f
        binding.fabFilterProducts.alpha = 0f
        binding.fabProfileProducts.alpha = 0f

        binding.fabAddProduct.translationY = translationYaxis
        binding.fabFavouriteProducts.translationY = translationYaxis
        binding.fabFilterProducts.translationY = translationYaxis
        binding.fabProfileProducts.translationY = translationYaxis

        binding.fabMain.setOnClickListener{
            if(menuOpen){
                closeMenu()
            }
            else{
                openMenu()
            }
        }

    }

    private fun openMenu() {
        menuOpen = !menuOpen
        binding.fabMain.setImageResource(R.drawable.ic_down_arrow)
        binding.fabAddProduct.animate().translationY(0f).alpha(1f).setInterpolator(interpolator).setDuration(300).start()
        binding.fabFavouriteProducts.animate().translationY(0f).alpha(1f).setInterpolator(interpolator).setDuration(300).start()
        binding.fabFilterProducts.animate().translationY(0f).alpha(1f).setInterpolator(interpolator).setDuration(300).start()
        binding.fabProfileProducts.animate().translationY(0f).alpha(1f).setInterpolator(interpolator).setDuration(300).start()
    }

    private fun closeMenu() {
        menuOpen = !menuOpen
        binding.fabMain.setImageResource(R.drawable.ic_arrow_up)
        binding.fabAddProduct.animate().translationY(translationYaxis).alpha(0f).setInterpolator(interpolator).setDuration(300).start()
        binding.fabFavouriteProducts.animate().translationY(translationYaxis).alpha(0f).setInterpolator(interpolator).setDuration(300).start()
        binding.fabFilterProducts.animate().translationY(translationYaxis).alpha(0f).setInterpolator(interpolator).setDuration(300).start()
        binding.fabProfileProducts.animate().translationY(translationYaxis).alpha(0f).setInterpolator(interpolator).setDuration(300).start()
    }

    private fun initProductRecyclerView(currentUser: UsersResponse) {
        //val manager = GridLayoutManager(requireContext(), 2)
        //val decoration = DividerItemDecoration(requireContext(), manager.orientation)
        val recyclerView = binding.productRecyclerView
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = ProductAdapter(currentUser, arrProducts, requireContext()) { product ->
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