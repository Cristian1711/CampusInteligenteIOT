package com.example.campusinteligenteiot.ui.home.shop

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.*
import android.view.ViewGroup
import android.view.animation.OvershootInterpolator
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.compose.material.FloatingActionButton
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.campusinteligenteiot.R
import com.example.campusinteligenteiot.api.model.product.ProductResponse
import com.example.campusinteligenteiot.api.model.user.UsersResponse
import com.example.campusinteligenteiot.api.network.product.ProductApiClient
import com.example.campusinteligenteiot.databinding.ScheduleFragmentBinding
import com.example.campusinteligenteiot.databinding.ShopFragmentBinding
import com.example.campusinteligenteiot.repository.ProductRepository
import com.example.campusinteligenteiot.ui.drawer.friends.FriendsAdapter
import com.example.campusinteligenteiot.ui.home.schedule.ScheduleViewModel
import com.example.campusinteligenteiot.ui.home.shop.adapter.ProductAdapter
import com.google.gson.Gson
import kotlinx.android.synthetic.main.shop_fragment.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class ShopFragment : Fragment() {

    private  var _binding: ShopFragmentBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<ShopViewModel>()
    private lateinit var user: UsersResponse
    private val translationYaxis = 100f
    private var menuOpen = false
    private val interpolator: OvershootInterpolator = OvershootInterpolator()
    private lateinit var adapter: ProductAdapter
    private lateinit var recyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = ShopFragmentBinding.inflate(inflater,container,false)

        val sharedPreferences = requireContext().getSharedPreferences("MY_PREF", Context.MODE_PRIVATE)
        val gson = Gson()
        val json = sharedPreferences.getString("current_user", "")
        user = gson.fromJson(json, UsersResponse::class.java)

        GlobalScope.launch(Dispatchers.Main){
            observeData()
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.autoCompleteTextView.onItemClickListener = AdapterView.OnItemClickListener{
                parent, _, position, _ ->
            val selectedItem = parent.getItemAtPosition(position).toString()
            if(selectedItem == "Sin filtro"){
                binding.titleProductsPage.text = "Productos"
                binding.comboBox.visibility = INVISIBLE
            }
            else {
                binding.titleProductsPage.text = "Productos" + " - $selectedItem"
            }
            adapter.filterProductList(selectedItem)
            adapter.notifyDataSetChanged()
        }

        initProductsRecyclerView(view)
        showMenu()

    }

    override fun onResume() {
        super.onResume()
        val categories = resources.getStringArray(R.array.categories)
        val arrayAdapter = ArrayAdapter(requireContext(), R.layout.dropdown_item, categories)
        binding.autoCompleteTextView.setAdapter(arrayAdapter)
    }

    private fun showMenu() {
        binding.fabFavouriteProducts.alpha = 0f
        binding.fabFilterProducts.alpha = 0f
        binding.fabProfileProducts.alpha = 0f
        binding.cardView1.alpha = 0f
        binding.cardView2.alpha = 0f
        binding.cardView3.alpha = 0f

        binding.fabFavouriteProducts.translationY = translationYaxis
        binding.fabFilterProducts.translationY = translationYaxis
        binding.fabProfileProducts.translationY = translationYaxis
        binding.cardView1.translationY = translationYaxis
        binding.cardView2.translationY = translationYaxis
        binding.cardView3.translationY = translationYaxis

        binding.fabMain.setOnClickListener{
            if(menuOpen){
                closeMenu()
            }
            else{
                openMenu()
            }
        }

    }

    private fun initProductsRecyclerView(view: View) {
        recyclerView = view.findViewById(R.id.productRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        adapter = ProductAdapter(user, requireContext())
        recyclerView.adapter = adapter
    }

    private fun openMenu() {
        menuOpen = !menuOpen
        binding.fabFavouriteProducts.visibility = VISIBLE
        binding.fabFilterProducts.visibility = VISIBLE
        binding.fabProfileProducts.visibility = VISIBLE
        binding.cardView1.visibility = VISIBLE
        binding.cardView2.visibility = VISIBLE
        binding.cardView3.visibility = VISIBLE

        binding.fabMain.setImageResource(R.drawable.ic_down_arrow)
        binding.fabFavouriteProducts.animate().translationY(0f).alpha(1f).setInterpolator(interpolator).setDuration(300).start()
        binding.fabFilterProducts.animate().translationY(0f).alpha(1f).setInterpolator(interpolator).setDuration(300).start()
        binding.fabProfileProducts.animate().translationY(0f).alpha(1f).setInterpolator(interpolator).setDuration(300).start()
        binding.cardView1.animate().translationY(0f).alpha(1f).setInterpolator(interpolator).setDuration(300).start()
        binding.cardView2.animate().translationY(0f).alpha(1f).setInterpolator(interpolator).setDuration(300).start()
        binding.cardView3.animate().translationY(0f).alpha(1f).setInterpolator(interpolator).setDuration(300).start()

        binding.fabFilterProducts.setOnClickListener{
            binding.comboBox.visibility = VISIBLE
            closeMenu()
        }

        binding.fabFavouriteProducts.setOnClickListener{
            binding.titleProductsPage.text = "Lista productos favoritos"
            binding.comboBox.visibility = INVISIBLE
            adapter.filterProductListByLikes(user)
            adapter.notifyDataSetChanged()
            closeMenu()
        }

        binding.fabProfileProducts.setOnClickListener{
            findNavController().navigate(R.id.action_navigation_shop_to_myProductsFragment)
        }

    }

    private fun closeMenu() {
        menuOpen = !menuOpen
        binding.fabMain.setImageResource(R.drawable.ic_arrow_up)
        binding.fabFavouriteProducts.animate().translationY(translationYaxis).alpha(0f).setInterpolator(interpolator).setDuration(300).start()
        binding.fabFilterProducts.animate().translationY(translationYaxis).alpha(0f).setInterpolator(interpolator).setDuration(300).start()
        binding.fabProfileProducts.animate().translationY(translationYaxis).alpha(0f).setInterpolator(interpolator).setDuration(300).start()
        binding.cardView1.animate().translationY(translationYaxis).alpha(0f).setInterpolator(interpolator).setDuration(300).start()
        binding.cardView2.animate().translationY(translationYaxis).alpha(0f).setInterpolator(interpolator).setDuration(300).start()
        binding.cardView3.animate().translationY(translationYaxis).alpha(0f).setInterpolator(interpolator).setDuration(300).start()

        Handler().postDelayed({
            binding.fabFavouriteProducts.visibility = INVISIBLE
            binding.fabFilterProducts.visibility = INVISIBLE
            binding.fabProfileProducts.visibility = INVISIBLE
            binding.cardView1.visibility = INVISIBLE
            binding.cardView2.visibility = INVISIBLE
            binding.cardView3.visibility = INVISIBLE
        }, 400)
    }

    private suspend fun observeData(){
        viewModel.getAllProducts().observe(viewLifecycleOwner, Observer{
            adapter.setProductList(it)
            adapter.notifyDataSetChanged()
        })
    }

}