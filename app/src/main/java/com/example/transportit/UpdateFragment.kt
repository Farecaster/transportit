package com.example.transportit


import android.R
import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import com.example.transportit.databinding.FragmentUpdateBinding
import java.io.ByteArrayOutputStream


class UpdateFragment : Fragment() {
    private lateinit var binding: FragmentUpdateBinding
    private lateinit var navController: NavController
    val args: UpdateFragmentArgs by navArgs()
    private var selectedImageBitmap: Bitmap? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentUpdateBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val pickImageLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data = result.data
                if (data != null) {
                    val selectedImageUri = data.data
                    if (selectedImageUri != null) {
                        val inputStream = context?.contentResolver?.openInputStream(selectedImageUri)
                        selectedImageBitmap = BitmapFactory.decodeStream(inputStream)
                        binding.imgAddUpdate.setImageBitmap(selectedImageBitmap)
                    }
                }
            }
        }
        navController = Navigation.findNavController(view)

        binding.apply {
            etTruckNameUpdate.editText?.setText(args.truckName)
            etDimensionsUpdate.editText?.setText(args.dimension)
            etPayloadUpdate.editText?.setText(args.payload)
            etTypeUpdate.setText(args.type)
            etPriceUpdate.editText?.setText(args.price)
            val imageDataString = args.imageData
            val imageData = Base64.decode(imageDataString, Base64.DEFAULT)
            val bitmap = BitmapFactory.decodeByteArray(imageData, 0, imageData.size)
            imgAddUpdate.setImageBitmap(bitmap)

            // Get the user's ID from your authentication system
            val userId = args.ownerId

            val helper = context?.let { MyHelper(it) }
            val db = helper?.writableDatabase
            val truckId = args.id

            val truckTypes = arrayOf("Open Body", "Close Body", "Other Types") // Define your truck types
            binding.etTypeUpdate.setOnClickListener {
                binding.etTypeUpdate.showDropDown()
            }
            val adapter = ArrayAdapter(requireContext(), R.layout.simple_dropdown_item_1line, truckTypes)
            binding.etTypeUpdate.setAdapter(adapter)


            btnSave2Update.setOnClickListener {
                updateData(db, truckId.toInt(), userId.toInt())
                requireActivity().onBackPressed()
            }

            imgAddUpdate.setOnClickListener {
                val intent = Intent(Intent.ACTION_GET_CONTENT)
                intent.type = "image/*"
                pickImageLauncher.launch(intent)
            }
        }
    }

    private fun updateData(db: SQLiteDatabase?, truckId: Int, userId: Int) {
        val truckName = binding.etTruckNameUpdate.editText?.text.toString()
        val dimension = binding.etDimensionsUpdate.editText?.text.toString()
        val payload = binding.etPayloadUpdate.editText?.text.toString()
        val type = binding.etTypeUpdate.text.toString()
        val price = binding.etPriceUpdate.editText?.text.toString()

        val cv = ContentValues()
        cv.put("truckName", truckName)
        cv.put("dimension", dimension)
        cv.put("payload", payload)
        cv.put("type", type)
        cv.put("price", price)
        if (selectedImageBitmap != null) {
            val stream = ByteArrayOutputStream()
            selectedImageBitmap!!.compress(Bitmap.CompressFormat.JPEG, 70, stream)
            val byteArray = stream.toByteArray()
            cv.put("imageData", byteArray)
        }

        // Update the data for the specific truck owned by the current user
        val rowsAffected = db?.update("TRUCKS", cv, "_id = ? AND ownerId = ?", arrayOf(truckId.toString(), userId.toString()))

        if (rowsAffected != null) {
            if (rowsAffected > 0) {
                Toast.makeText(context, "Updated Successfully", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context, "Failed to Update", Toast.LENGTH_SHORT).show()
            }
        }
    }
}






