package com.example.transportit

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.SearchView
import androidx.activity.addCallback
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.transportit.databinding.FragmentHomeBinding

class homeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var navController: NavController
    private lateinit var truckList: MutableList<AvailableTruckDataClass>
    private lateinit var homeAdapter: HomeAdapter
    private var searchQuery: String? = null
    private var isExitRequested = false
    val args: homeFragmentArgs by navArgs()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init(view)

        binding.btnRequestedTrips.setOnClickListener {
            val action = homeFragmentDirections.actionHomeFragmentToBookingFragment(args.id)
            findNavController().navigate(action)
        }
        binding.btnProfile.setOnClickListener {
            val action = homeFragmentDirections.actionHomeFragmentToMainProfileFragment(args.id)
            findNavController().navigate(action)
        }


        binding.btnSearch.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                val imm = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(binding.btnSearch.windowToken, 0)

                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText != null) {
                    // Filter your list based on the newText
                    val filteredList = truckList.filter { truckItem ->
                        (truckItem.truckName?.contains(newText, ignoreCase = true) == true) ||
                                (truckItem.type?.contains(newText, ignoreCase = true) == true)
                    }.toMutableList()

                    // Update your adapter with the filtered results
                    homeAdapter.updateData(filteredList)

                    // Notify the adapter that the dataset has changed
                    homeAdapter.notifyDataSetChanged()
                }
                return true
            }

        })

        

        val helper = context?.let { MyHelper(it) }
        val db = helper?.readableDatabase
        val cursor = db?.query("TRUCKS", null,  "ownerId != ?",
            arrayOf(args.id), null, null, null)

        val idColumnIndex = cursor?.getColumnIndex("_id")
        val truckNameColumnIndex = cursor?.getColumnIndex("truckName")
        val dimensionColumnIndex = cursor?.getColumnIndex("dimension")
        val payloadColumnIndex = cursor?.getColumnIndex("payload")
        val typeColumnIndex = cursor?.getColumnIndex("type")
        val priceColumnIndex = cursor?.getColumnIndex("price")
        val ownerColumnIndex = cursor?.getColumnIndex("ownerId")
        val imageDataColumnIndex = cursor?.getColumnIndex("imageData")

        if (idColumnIndex != -1 &&
            truckNameColumnIndex != -1 &&
            dimensionColumnIndex != -1 &&
            payloadColumnIndex != -1 &&
            typeColumnIndex != -1 &&
            priceColumnIndex != -1 &&
            ownerColumnIndex != -1 &&
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
                    val ownerId = ownerColumnIndex?.let { cursor.getString(it) }
                    val imageData = imageDataColumnIndex?.let { cursor.getBlob(it) }

                    val truckItem = id?.let { AvailableTruckDataClass(it, truckName, dimension, payload, type, price,ownerId,imageData ) }
                    truckList.add(truckItem!!)
                }
            }
            // Notify your adapter here
            homeAdapter.notifyDataSetChanged()
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

        val recyclerView = binding.rvTruckList
        val LinearLayoutManager = LinearLayoutManager(context)
        recyclerView.setItemViewCacheSize(20)
        LinearLayoutManager.reverseLayout = true
        LinearLayoutManager.stackFromEnd = true
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager
        truckList = mutableListOf()
        homeAdapter = HomeAdapter(truckList, args.id, args.username)

        recyclerView.adapter = homeAdapter


    }
    private fun showExitConfirmationDialog() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Exit Confirmation")
        builder.setMessage("Are you sure you want to exit the app?")
        builder.setPositiveButton("Yes") { _, _ ->
            // User confirmed exit, finish the current activity to exit the app
            requireActivity().finish()
        }
        builder.setNegativeButton("No") { dialog, _ ->
            // User canceled exit, dismiss the dialog
            dialog.dismiss()
            isExitRequested = false
        }
        builder.setOnCancelListener {
            // Handle the case when the user cancels the dialog with the back button
            isExitRequested = false
        }

        val dialog = builder.create()
        dialog.show()
    }
}
