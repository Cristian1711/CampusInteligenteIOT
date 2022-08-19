package com.example.campusinteligenteiot.ui.home.shop.myProducts.addProduct

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.campusinteligenteiot.R
import com.example.campusinteligenteiot.api.model.product.ProductCall
import com.example.campusinteligenteiot.api.model.product.ProductResponse
import com.example.campusinteligenteiot.api.model.user.UsersResponse
import com.example.campusinteligenteiot.databinding.AddNewProductFragmentBinding
import com.example.campusinteligenteiot.model.Product
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import com.google.gson.Gson
import kotlinx.android.synthetic.main.dialog.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream
import java.text.SimpleDateFormat
import java.util.*

class AddNewProductFragment : Fragment() {

    private  var _binding: AddNewProductFragmentBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<AddNewProductViewModel>()
    private lateinit var user: UsersResponse
    private lateinit var product: ProductCall
    private val IMAGE_CHOOSE = 1000
    private var imageUri: Uri? = null
    private lateinit var storage: FirebaseStorage
    private lateinit var selectedItem: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = AddNewProductFragmentBinding.inflate(inflater,container,false)

        val sharedPreferences = requireContext().getSharedPreferences("MY_PREF", Context.MODE_PRIVATE)
        val gson = Gson()
        val json = sharedPreferences.getString("current_user", "")
        user = gson.fromJson(json, UsersResponse::class.java)
        storage = Firebase.storage
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        val categories = resources.getStringArray(R.array.categories_new_product)
        val arrayAdapter = ArrayAdapter(requireContext(), R.layout.dropdown_item, categories)
        binding.autoCompleteTextView.setAdapter(arrayAdapter)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.autoCompleteTextView.onItemClickListener = AdapterView.OnItemClickListener{
                parent, _, position, _ ->
            selectedItem = parent.getItemAtPosition(position).toString()
        }

        binding.buttonGallery.setOnClickListener {
            chooseImageGallery()
        }

        binding.backButton.setOnClickListener{
            findNavController().navigate(R.id.action_addNewProductFragment_to_myProductsFragment)
        }

        binding.buttonSave.setOnClickListener{

            product = ProductCall(binding.textTitleProduct.text.toString(),
            binding.textDescription.text.toString(), binding.textProductPrice.text.toString().toFloat(),
            user.id, null, false, selectedItem, false)

            val builder = AlertDialog.Builder(requireContext())
            val myView = layoutInflater.inflate(R.layout.dialog, null)

            builder.setView(myView)

            val dialog = builder.create()
            dialog.show()

            myView.buttonYes.setOnClickListener {
                product.published = true
                GlobalScope.launch(Dispatchers.Main) {
                    val response = viewModel.saveProduct("null", product)
                    fileUpload(response.body()!!)
                    dialog.cancel()
                }
            }

            myView.buttonNo.setOnClickListener {
                product.published = false
                GlobalScope.launch(Dispatchers.Main) {
                    val response = viewModel.saveProduct("null", product)
                    fileUpload(response.body()!!)
                    dialog.cancel()
                }
            }
        }
    }

    private fun fileUpload(id: String) {
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
        uploadDataFirestore(currentDate, id)
    }

    private fun uploadDataFirestore(currentDate: String, id: String) {
        val userId = Firebase.auth.currentUser?.uid.toString()
        val url = "gs://campusinteligenteiot.appspot.com/profiles/$userId/$currentDate.png"
        product.productImage = url
        val db = Firebase.firestore
        db.collection("Product")
            .document(id)
            .update("productImage", url)
            .addOnSuccessListener {
                Toast.makeText(context, R.string.firestore_upload_success, Toast.LENGTH_SHORT)
                    .show()
                Handler().postDelayed({
                    findNavController().navigate(R.id.action_addNewProductFragment_to_myProductsFragment)
                }, 3000)

            }.addOnFailureListener {

                Toast.makeText(context, R.string.firestore_upload_failure, Toast.LENGTH_SHORT)
                    .show()

            }
    }

    private fun chooseImageGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, IMAGE_CHOOSE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == IMAGE_CHOOSE && resultCode == Activity.RESULT_OK) {
            imageUri = data?.data
            binding.productImage.setImageURI(imageUri)
        }
    }

}