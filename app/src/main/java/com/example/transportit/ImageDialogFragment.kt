package com.example.transportit

import android.graphics.BitmapFactory
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.example.transportit.databinding.FragmentImageDialogBinding
import android.util.Base64


class ImageDialogFragment : Fragment() {
    private lateinit var binding: FragmentImageDialogBinding
    val args: ImageDialogFragmentArgs by navArgs()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentImageDialogBinding.inflate(inflater, container,false )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val imageDataString = args.imageData
        val imageData = Base64.decode(imageDataString, Base64.DEFAULT)

        // Load and display the image in the ImageView
        val bitmap = BitmapFactory.decodeByteArray(imageData, 0, imageData.size)
        binding.imageViewDialog.setImageBitmap(bitmap)

    }


}