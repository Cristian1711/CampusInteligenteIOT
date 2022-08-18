package com.example.campusinteligenteiot.ui.home.shop.myProducts.editProduct

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.campusinteligenteiot.R
import com.example.campusinteligenteiot.api.model.product.ProductResponse
import com.example.campusinteligenteiot.databinding.EditProductFragmentBinding
import com.example.campusinteligenteiot.databinding.MyProductsFragmentBinding
import com.example.campusinteligenteiot.ui.home.shop.myProducts.MyProductsViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream
import java.text.SimpleDateFormat
import java.util.*

class EditProductFragment : Fragment() {

    private  var _binding: EditProductFragmentBinding? = null
    private val binding get() = _binding!!
    private lateinit var product: ProductResponse
    private val viewModel by viewModels<EditProductViewModel>()
    private val IMAGE_CHOOSE = 1000
    private var imageUri: Uri? = null
    private lateinit var storage: FirebaseStorage

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = EditProductFragmentBinding.inflate(inflater,container,false)

        product = ProductResponse(
            arguments?.getString("productId")!!,
            arguments?.getString("productTitle")!!,
            arguments?.getString("productDescription")!!,
            arguments?.getFloat("productPrice")!!,
            arguments?.getString("idOwner")!!,
            arguments?.getString("productImage")!!,
            arguments?.getBoolean("published")!!,
            arguments?.getString("category")!!,
            arguments?.getBoolean("archived")!!
            )

        setData(product)
        storage = Firebase.storage

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonGallery.setOnClickListener {
            chooseImageGallery()
        }

        binding.ButtonYes.setOnClickListener {
            binding.cardView1.visibility = VISIBLE
        }

        binding.buttonSave.setOnClickListener{
            binding.progressBar.visibility = View.VISIBLE
            product.title = binding.textTitleProduct.text.toString()
            product.description = binding.textDescription.text.toString()
            product.category = binding.CategoryText.text.toString()
            val price = binding.ProductPrice.text.toString()
            product.price = price.toFloat()
            GlobalScope.launch(Dispatchers.Main) {
                viewModel.saveProductUseCase(product.id, product)
                fileUpload()
            }
        }
    }


    private fun chooseImageGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, IMAGE_CHOOSE)
    }

    private fun setData(product: ProductResponse) {
        binding.CategoryText.setText(product.category)
        binding.textDescription.setText(product.description)
        binding.textTitleProduct.setText(product.title)
        binding.ProductPrice.setText(product.price.toString())
        loadImage()
    }

    private fun loadImage() {
        val storageReference = FirebaseStorage.getInstance()
        val gsReference = storageReference.getReferenceFromUrl(product.productImage)
        gsReference.downloadUrl.addOnSuccessListener {
            Glide.with(requireContext()).load(it).into(binding.productImage)
        }
    }

    private fun fileUpload() {
        val storageRef = storage.reference
        val name = Firebase.auth.currentUser?.uid.toString()
        val sdf = SimpleDateFormat("dd/M/yyyy_hh:mm:ss")
        val currentDate = sdf.format(Date()).toString()
        val pathImage = storageRef.child("profiles/$name/$currentDate.png")

        // Get the data from an ImageView as bytes
        //binding.imageView1.isDrawingCacheEnabled = true
        //binding.imageView1.buildDrawingCache()
        val bitmap = (binding.productImage.drawable as BitmapDrawable).bitmap
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 20, baos)
        val data = baos.toByteArray()

        var uploadTask = pathImage.putBytes(data)
        uploadTask.addOnFailureListener {
            // Handle unsuccessful uploads
            Toast.makeText(context, R.string.register_images_error, Toast.LENGTH_SHORT).show()
        }.addOnSuccessListener { taskSnapshot ->
            // taskSnapshot.metadata contains file metadata such as size, content-type, etc.
            Toast.makeText(context, R.string.register_images_success, Toast.LENGTH_SHORT).show()
        }


        val url = pathImage.downloadUrl.toString()
        uploadDataFirestore(currentDate)

    }

    private fun uploadDataFirestore(currentDate: String ) {
        val userId = Firebase.auth.currentUser?.uid.toString()
        val url = "gs://campusinteligenteiot.appspot.com/profiles/$userId/$currentDate.png"
        val db = Firebase.firestore
        db.collection("Product")
            .document(product.id)
            .update("productImage", url)
            .addOnSuccessListener {
                Toast.makeText(context, R.string.firestore_upload_success, Toast.LENGTH_SHORT)
                    .show()
                findNavController().navigate(R.id.action_editProductFragment_to_myProductsFragment)

            }.addOnFailureListener {

                Toast.makeText(context, R.string.firestore_upload_failure, Toast.LENGTH_SHORT)
                    .show()

            }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == IMAGE_CHOOSE && resultCode == Activity.RESULT_OK) {
            imageUri = data?.data
            binding.productImage.setImageURI(imageUri)
        }
    }

}