package com.example.transportit

import android.graphics.BitmapFactory
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.transportit.databinding.FragmentViewBinding
import android.util.Base64

class viewFragment : Fragment() {
    private lateinit var binding: FragmentViewBinding
    val args: viewFragmentArgs by navArgs()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentViewBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            tvNameViewFragment.text = args.truckName
            tvDimensionViewFragment.text = args.dimension
            tvPayloadViewFragment.text = "${args.payload}KG"
            tvTypeViewFragment.text = args.type
            tvPriceViewFragment.text = args.price

            //fetch the image
            val imageDataString = args.imageData
            val imageData = Base64.decode(imageDataString, Base64.DEFAULT)
            val bitmap = BitmapFactory.decodeByteArray(imageData, 0, imageData.size)
            imageViewFragment.setImageBitmap(bitmap)

            btnBook.setOnClickListener{
                val action = viewFragmentDirections.actionViewFragmentToRequestFragment(
                    args.id,
                    args.truckName,
                    args.dimension,
                    args.payload,
                    args.type,
                    args.price,
                    args.ownerId,
                    args.imageData,
                    args.requesterId,
                    args.username
                )
                findNavController().navigate(action)
            }
            btnProfileView.setOnClickListener {
                val action = viewFragmentDirections.actionViewFragmentToProfileFragment(args.ownerId, args.requesterId, args.username)
                findNavController().navigate(action)
            }

            imageViewFragment.setOnClickListener {
                val action = viewFragmentDirections.actionViewFragmentToImageDialogFragment(args.imageData)
                findNavController().navigate(action)
            }
        }
    }


}