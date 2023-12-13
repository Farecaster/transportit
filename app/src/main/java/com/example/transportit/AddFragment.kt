package com.example.transportit

import android.app.Activity
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.transportit.databinding.FragmentAddBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.io.ByteArrayOutputStream

class AddFragment : Fragment() {
    private lateinit var binding: FragmentAddBinding
    private lateinit var navController: NavController
    val args: AddFragmentArgs by navArgs()

    private val PICK_IMAGE_REQUEST_CODE = 123
    private var selectedImageBitmap: Bitmap? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.window?.decorView?.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        init(view)
        var helper = context?.let { MyHelper(it) }
        var db = helper?.readableDatabase
        val rs = db?.rawQuery("SELECT * FROM TRUCKS",null)

        binding.btnPickImage.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(intent, PICK_IMAGE_REQUEST_CODE)

        }

        val truckTypes = arrayOf("Open Body", "Close Body", "Other Types") // Define your truck types
        binding.etType.setOnClickListener {
           binding.etType.showDropDown()
        }
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, truckTypes)
        binding.etType.setAdapter(adapter)


        binding.btnSave2.setOnClickListener {
            val truckName = binding.etTruckName.editText?.text.toString()
            val dimension = binding.etDimensions.editText?.text.toString()
            val payload = binding.etPayload.editText?.text.toString()
            val type = binding.etType.text.toString()
            val price = binding.etPrice.editText?.text.toString()

            if (truckName.isNotEmpty() && dimension.isNotEmpty() && payload.isNotEmpty() && type.isNotEmpty() && price.isNotEmpty() && selectedImageBitmap != null) {
                val cv = ContentValues()
                cv.put("truckName", truckName)
                cv.put("dimension", dimension)
                cv.put("payload", payload)
                cv.put("type", type)
                cv.put("price", price)
                cv.put("ownerId",args.id)
                val stream = ByteArrayOutputStream()
                selectedImageBitmap!!.compress(Bitmap.CompressFormat.JPEG, 70, stream)
                val byteArray = stream.toByteArray()
                cv.put("imageData", byteArray)


                val helper = context?.let { MyHelper(it) }
                val db = helper?.writableDatabase
                db?.insert("TRUCKS", null, cv)
                Toast.makeText(context, "Added successfully", Toast.LENGTH_SHORT).show()
                // Safely navigate to seller fragment
                requireActivity().onBackPressed()
            } else {
                Toast.makeText(
                    context,
                    "Please fill all fields",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }


    }


    private fun init(view: View) {
        navController = Navigation.findNavController(view)
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_IMAGE_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            val selectedImageUri = data?.data

            if (selectedImageUri != null) {
                // Convert the selected image to a Bitmap
                selectedImageBitmap = MediaStore.Images.Media.getBitmap(requireContext().contentResolver, selectedImageUri)

                // Resize the image if it exceeds the maximum dimensions
                val maxWidth = 2048
                val maxHeight = 2048
                if (selectedImageBitmap!!.width > maxWidth || selectedImageBitmap!!.height > maxHeight) {
                    val aspectRatio: Float = selectedImageBitmap!!.width.toFloat() / selectedImageBitmap!!.height.toFloat()
                    if (selectedImageBitmap!!.width > selectedImageBitmap!!.height) {
                        selectedImageBitmap = Bitmap.createScaledBitmap(selectedImageBitmap!!, maxWidth, (maxWidth / aspectRatio).toInt(), false)
                    } else {
                        selectedImageBitmap = Bitmap.createScaledBitmap(selectedImageBitmap!!, (maxHeight * aspectRatio).toInt(), maxHeight, false)
                    }
                }

                // Update the UI to display the resized image
                binding.imgAdd.setImageBitmap(selectedImageBitmap)
            }
        }
    }

}