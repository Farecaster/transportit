package com.example.transportit

import android.app.AlertDialog
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.transportit.databinding.FragmentSellerBinding


class sellerFragment : Fragment() {
    private lateinit var binding: FragmentSellerBinding
    private lateinit var navController: NavController
    private lateinit var truckList :MutableList<AvailableTruckDataClass>
    private lateinit var SellerAdapter: SellerAdapter
    private val READ_EXTERNAL_STORAGE_REQUEST_CODE = 123
    private var isExitRequested = false
    val args: sellerFragmentArgs by navArgs()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSellerBinding.inflate(inflater, container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Check for permission and request it if needed
        if (ContextCompat.checkSelfPermission(requireContext(), android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            // Permission is granted. You can access the external storage here.
            // Load your image from storage.
        } else {
            // Permission is not granted. You can request the permission from the user.
            ActivityCompat.requestPermissions(requireActivity(), arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE), READ_EXTERNAL_STORAGE_REQUEST_CODE)
        }

        init(view)


        binding.btnPending.setOnClickListener {
            val action = sellerFragmentDirections.actionSellerFragmentToBookingSellerFragment(args.Id)
            findNavController().navigate(action)
        }

        binding.btnAddTruck.setOnClickListener {
            val action = sellerFragmentDirections.actionSellerFragmentToAddFragment(args.Id)
            findNavController().navigate(action)

        }
        binding.btnProfile2.setOnClickListener {
            val action = sellerFragmentDirections.actionSellerFragmentToMainProfileFragment(args.Id)
            findNavController().navigate(action)
        }


// Initialize an empty list to store the user's trucks

// Fetch the user's trucks from the SQLite database
        val helper = context?.let { MyHelper(it) }
        val db = helper?.readableDatabase
        val selection = "ownerId = ?"
        val selectionArgs = arrayOf(args.Id)
        val cursor = db?.query("TRUCKS", null,  selection,
            selectionArgs, null, null, null)

        val idColumnIndex = cursor?.getColumnIndex("_id")
        val truckNameColumnIndex = cursor?.getColumnIndex("truckName")
        val dimensionColumnIndex = cursor?.getColumnIndex("dimension")
        val payloadColumnIndex = cursor?.getColumnIndex("payload")
        val typeColumnIndex = cursor?.getColumnIndex("type")
        val priceColumnIndex = cursor?.getColumnIndex("price")
        val imageDataColumnIndex = cursor?.getColumnIndex("imageData")

        if (idColumnIndex != -1 &&
            truckNameColumnIndex != -1 &&
            dimensionColumnIndex != -1 &&
            payloadColumnIndex != -1 &&
            typeColumnIndex != -1 &&
            priceColumnIndex != -1 &&
            imageDataColumnIndex != -1) {
            truckList.clear()
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    val id = idColumnIndex?.let { cursor.getLong(it) }
                    val truckName = truckNameColumnIndex?.let { cursor.getString(it) }
                    val dimension = dimensionColumnIndex?.let { cursor.getString(it) }
                    val payload = payloadColumnIndex?.let { cursor.getString(it) }
                    val type = typeColumnIndex?.let { cursor.getString(it) }
                    val price = priceColumnIndex?.let { cursor.getString(it) }
                    val imageData = imageDataColumnIndex?.let { cursor.getBlob(it) } // Retrieve image data as a ByteArray

                    if (imageData != null) {
                        val truckItem = id?.let {
                            AvailableTruckDataClass(it, truckName, dimension, payload, type, price, args.Id, imageData)
                        }
                        truckList.add(truckItem!!)
                    }
                }
            }
            // Notify your adapter here
            SellerAdapter.notifyDataSetChanged()
        }



        requireActivity().onBackPressedDispatcher.addCallback(this) {
            if (isExitRequested) {
                // Exit immediately if the exit request is already in progress
                requireActivity().onBackPressed()
            } else {
                isExitRequested = true
                showExitConfirmationDialog()
            }
        }
    }

    private fun init(view: View) {
        navController = Navigation.findNavController(view)


        val recyclerView =binding.rvTruckSeller
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.setItemViewCacheSize(20)
        truckList = mutableListOf()
        SellerAdapter = SellerAdapter(truckList)
        recyclerView.adapter = SellerAdapter


    }

    private fun showExitConfirmationDialog() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Exit Confirmation")
        builder.setMessage("Are you sure you want to exit the app?")
        builder.setPositiveButton("Yes") { _, _ ->
            requireActivity().finish()
        }
        builder.setNegativeButton("No") { dialog, _ ->
            dialog.dismiss()
            isExitRequested = false
        }
        builder.setOnCancelListener {
            isExitRequested = false
        }

        val dialog = builder.create()
        dialog.show()
    }


}



