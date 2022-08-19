package com.example.campusinteligenteiot.ui.home.shop.myProducts

import android.app.AlertDialog
import android.content.Context
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.campusinteligenteiot.R
import com.example.campusinteligenteiot.api.model.product.ProductCall
import com.example.campusinteligenteiot.api.model.user.UsersResponse
import com.example.campusinteligenteiot.databinding.MyProductsFragmentBinding
import com.example.campusinteligenteiot.databinding.ProductDetailFragmentBinding
import com.example.campusinteligenteiot.ui.home.shop.adapter.MyProductAdapter
import com.example.campusinteligenteiot.ui.home.shop.adapter.ProductAdapter
import com.example.campusinteligenteiot.ui.home.shop.detail.ProductDetailViewModel
import com.google.gson.Gson
import kotlinx.android.synthetic.main.delete_dialog_1.view.*
import kotlinx.android.synthetic.main.delete_dialog_1.view.buttonNo
import kotlinx.android.synthetic.main.delete_dialog_1.view.buttonYes
import kotlinx.android.synthetic.main.delete_dialog_2.view.*
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

        binding.fabAddProduct.setOnClickListener{
            val navController = Navigation.findNavController(view)
            navController.navigate(R.id.action_myProductsFragment_to_addNewProductFragment)
        }

        binding.backButton.setOnClickListener{
            findNavController().navigate(R.id.action_myProductsFragment_to_navigation_shop)
        }
    }

    private fun initProductsRecyclerView(view: View) {
        recyclerView = view.findViewById(R.id.productRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        adapter = MyProductAdapter(user, requireContext(),
            {position -> onDeletedItem(position)}
        )
        recyclerView.adapter = adapter
    }

    private fun onDeletedItem(position: Int){
        var product = adapter.getProductList()[position]
        val builder = AlertDialog.Builder(requireContext())

        if(adapter.getProductList()[position].published){
            val myView = layoutInflater.inflate(R.layout.delete_dialog_1, null)

            builder.setView(myView)

            val dialog = builder.create()
            dialog.show()

            myView.buttonYes.setOnClickListener {
                product.published = false
                val editedProduct = ProductCall(product.title, product.description,
                product.price, product.idOwner, product.productImage, product.published, product.category, product.archived)
                GlobalScope.launch(Dispatchers.Main) {
                    viewModel.saveProduct(adapter.getProductList()[position].id, editedProduct)
                    adapter.notifyItemChanged(position)
                    dialog.cancel()
                }
            }

            myView.buttonNo.setOnClickListener {
                dialog.cancel()
            }
        }
        else{
            val myView = layoutInflater.inflate(R.layout.delete_dialog_2, null)

            builder.setView(myView)

            val dialog = builder.create()
            dialog.show()

            myView.buttonYes.setOnClickListener {
                product.archived = true
                val editedProduct = ProductCall(product.title, product.description,
                    product.price, product.idOwner, product.productImage, product.published, product.category, product.archived)
                GlobalScope.launch(Dispatchers.Main) {
                    viewModel.saveProduct(adapter.getProductList()[position].id, editedProduct)
                    adapter.notifyItemChanged(position)
                    dialog.cancel()
                }
            }

            myView.buttonDelete.setOnClickListener {
                GlobalScope.launch(Dispatchers.Main) {
                    val response = viewModel.deleteProduct(product.id)
                    println("RESPUESTA OBTENIDA AL BORRAR $response")
                    adapter.removeItem(position)
                    adapter.notifyItemRemoved(position)
                    Toast.makeText(context, getString(R.string.delete_success), Toast.LENGTH_SHORT).show()
                    dialog.cancel()
                }
            }

        }


    }

}