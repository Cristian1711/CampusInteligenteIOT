package com.example.campusinteligenteiot.ui.home.shop.detail

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import com.airbnb.lottie.LottieAnimationView
import com.bumptech.glide.Glide
import com.example.campusinteligenteiot.R
import com.example.campusinteligenteiot.api.model.product.ProductResponse
import com.example.campusinteligenteiot.api.model.user.UsersResponse
import com.example.campusinteligenteiot.databinding.ProductDetailFragmentBinding
import com.google.firebase.storage.FirebaseStorage
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class ProductDetailFragment : Fragment() {
    private var productId: String? = null
    private  var _binding: ProductDetailFragmentBinding? = null
    private val binding get() = _binding!!
    private lateinit var product: ProductResponse
    private lateinit var user: UsersResponse
    private lateinit var currentUser: UsersResponse
    private val viewModel by viewModels<ProductDetailViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = ProductDetailFragmentBinding.inflate(inflater,container,false)

        productId = arguments?.getString("productId")
        val sharedPreferences = requireContext().getSharedPreferences("MY_PREF", Context.MODE_PRIVATE)
        val gson = Gson()
        val json = sharedPreferences.getString("current_user", "")
        currentUser = gson.fromJson(json, UsersResponse::class.java)

        GlobalScope.launch(Dispatchers.Main) {
            product = viewModel.getSingleProduct(productId!!)!!
            user = viewModel.getSingleUser(product.idOwner)!!
            setProductData(product)
            loadImage(product.productImage!!)
            prepareLikeButton()
        }

        return binding.root
    }

    private fun loadImage(profileImage: String) {
        val storageReference = FirebaseStorage.getInstance()
        val gsReference = storageReference.getReferenceFromUrl(profileImage)
        gsReference.downloadUrl.addOnSuccessListener {
            Glide.with(requireContext()).load(it).into(binding.ProductImage)
        }
    }

    private fun setProductData(product: ProductResponse) {
        binding.ProductOwner.text = "Publicado por " + user.userName
        binding.ProductTitle.text = product.title
        binding.CategoryText.text = product.category
        binding.DescriptionText.text = product.description
        binding.ProductPrice.text = product.price.toString() + " €"
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.ContactButton.setOnClickListener{
            val navController = Navigation.findNavController(view)
            navController.navigate(R.id.action_productDetailFragment_to_channelsFragment)
        }

        binding.ViewProfileButton.setOnClickListener{
            val bundle = bundleOf(
                "userId" to product.idOwner
            )
            val navController = Navigation.findNavController(view)
            navController.navigate(R.id.action_productDetailFragment_to_sellerProfileFragment, bundle)
        }

        binding.backButton.setOnClickListener{
            val navController = Navigation.findNavController(view)
            navController.navigate(R.id.action_productDetailFragment_to_navigation_shop)
        }


    }

    private fun prepareLikeButton() {
        var like: Boolean = currentUser.productLikes.contains(product.id)
        if(like) binding.likeImageView.setImageResource(R.drawable.ic_twitter_like)
        binding.likeImageView.setOnClickListener{
            println("HE PULSADO EL BOTON DE LIKE")
            println(like)
            like = likeAnimation(binding.likeImageView, R.raw.bandai_dokkan, like)
            if(like){
                currentUser.productLikes.add(product.id!!)
                GlobalScope.launch(Dispatchers.Main) {
                    viewModel.saveUser(currentUser, currentUser.id)
                    val sharedPreferences = context?.getSharedPreferences("MY_PREF", Context.MODE_PRIVATE)
                    val editor = sharedPreferences?.edit()
                    val gson = Gson()
                    val json = gson.toJson(currentUser)
                    editor!!.putString("current_user", json)
                    editor.commit()
                }
            } else{
                currentUser.productLikes.remove(product.id)
                GlobalScope.launch(Dispatchers.Main) {
                    viewModel.saveUser(currentUser, currentUser.id)
                    val sharedPreferences = context?.getSharedPreferences("MY_PREF", Context.MODE_PRIVATE)
                    val editor = sharedPreferences?.edit()
                    val gson = Gson()
                    val json = gson.toJson(currentUser)
                    editor!!.putString("current_user", json)
                    editor.commit()
                }
            }
        }

    }

    private fun likeAnimation(imageView: LottieAnimationView, animation: Int, like: Boolean): Boolean {
        if(!like){
            imageView.setAnimation(animation)
            imageView.playAnimation()
        }else{
            imageView.animate()
                .alpha(0f)
                .setDuration(200)
                .setListener(object: AnimatorListenerAdapter(){

                    override fun onAnimationEnd(animation: Animator?) {
                        imageView.setImageResource(R.drawable.twitter_like)
                        imageView.alpha = 1f
                    }

                })

        }
        return !like
    }

}