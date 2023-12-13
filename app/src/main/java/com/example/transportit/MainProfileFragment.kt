package com.example.transportit

import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import com.example.transportit.databinding.FragmentMainProfileBinding
import java.io.ByteArrayOutputStream

class MainProfileFragment : Fragment() {
    private lateinit var binding: FragmentMainProfileBinding
    private val PICK_IMAGE_REQUEST_CODE = 124
    private var selectedImageBitmap: Bitmap? = null
    val args: MainProfileFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMainProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            imageView2.setOnClickListener {
                val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                startActivityForResult(intent, PICK_IMAGE_REQUEST_CODE)
            }

            btnLogout.setOnClickListener {
                val navController = Navigation.findNavController(view)
                navController.popBackStack(R.id.splashFragment, false)
            }
        }
        val helper = context?.let { MyHelper(it) }
        val db = helper?.readableDatabase
        val imageData = arrayOf("imageData")
        val cursor = db?.query("USER", null,  "_id = ?",
            arrayOf(args.id), null, null, null)

        if (cursor != null && cursor.moveToFirst()) {
            val profilePicData = cursor.getBlob(cursor.getColumnIndex("imageData"))
            val username = cursor.getString(cursor.getColumnIndex("USERNAME"))
            val email = cursor.getString(cursor.getColumnIndex("EMAIL"))

            // Now you have the username and email, and you can display them in your UI
            binding.tvProfileUsername.text = username
            binding.tvProfileEmail.text = email

            if (profilePicData != null) {
                // Convert the profilePicData to a Bitmap and display it in an ImageView or use it as needed
                val profileBitmap = BitmapFactory.decodeByteArray(profilePicData, 0, profilePicData.size)
                // Now you can set the retrieved profile picture in an ImageView, for example:
                binding.imageView2.setImageBitmap(profileBitmap)
            } else {
                // Handle the case where there's no profile picture available
            }

            cursor.close()
        }
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
                val cv = ContentValues()
                val stream = ByteArrayOutputStream()
                selectedImageBitmap!!.compress(Bitmap.CompressFormat.JPEG, 70, stream)
                val byteArray = stream.toByteArray()
                cv.put("imageData", byteArray)

                // Update the image data in the database for the current user (use the appropriate user ID)
                val helper = context?.let { MyHelper(it) }
                val db = helper?.writableDatabase
                db?.update("USER", cv, "_id = ?", arrayOf(args.id))

                // Set the retrieved profile picture in an ImageView
                binding.imageView2.setImageBitmap(selectedImageBitmap)
            }
        }
    }
}