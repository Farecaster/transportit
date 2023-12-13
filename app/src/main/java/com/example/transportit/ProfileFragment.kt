package com.example.transportit

import android.graphics.BitmapFactory
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.transportit.databinding.FragmentProfileBinding

class ProfileFragment : Fragment() {
    private lateinit var binding: FragmentProfileBinding
    private lateinit var navController: NavController
    private lateinit var truckList: MutableList<AvailableTruckDataClass>
    private lateinit var ViewProfileAdapter: ViewProfileAdapter
    val args: ProfileFragmentArgs by navArgs()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init(view)

        val helper = context?.let { MyHelper(it) }
        val db = helper?.readableDatabase


        val cursorUser = db?.query("USER",  null,  "_id = ?",
            arrayOf(args.id), null, null, null)
        if (cursorUser != null && cursorUser.moveToFirst()) {
            val usernameColumnIndex = cursorUser.getColumnIndex("USERNAME")
            val emailColumnIndex = cursorUser.getColumnIndex("EMAIL")
            val profilePicData =  cursorUser.getBlob(cursorUser.getColumnIndex("imageData"))
            val username = cursorUser.getString(usernameColumnIndex)
            val email = cursorUser.getString(emailColumnIndex)
            binding.username.text = username
            binding.tvEmail.text = email
            if (profilePicData != null) {
                // Convert the profilePicData to a Bitmap and display it in an ImageView or use it as needed
                val profileBitmap = BitmapFactory.decodeByteArray(profilePicData, 0, profilePicData.size)
                // Now you can set the retrieved profile picture in an ImageView, for example:
                binding.imgProfile.setImageBitmap(profileBitmap)
            } else {
                binding.imgProfile.setImageResource(R.drawable.truck)
            }
            cursorUser.close()
        }

        val selection = "ownerId = ?"
        val selectionArgs = arrayOf(args.id)
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
                    val imageData = imageDataColumnIndex?.let { cursor.getBlob(it) }

                    val truckItem = id?.let { AvailableTruckDataClass(it, truckName, dimension, payload, type, price,args.id,imageData ) }
                    truckList.add(truckItem!!)
                }
            }
            // Notify your adapter here
            ViewProfileAdapter.notifyDataSetChanged()
        }

    }
    private fun init(view: View) {
        navController = Navigation.findNavController(view)

        val recyclerView = binding.rvProfile
        val LinearLayoutManager = LinearLayoutManager(context)
        recyclerView.setItemViewCacheSize(20)
        LinearLayoutManager.reverseLayout = true
        LinearLayoutManager.stackFromEnd = true
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager
        truckList = mutableListOf()
        ViewProfileAdapter = ViewProfileAdapter(truckList,args.requesterId, args.username)

        recyclerView.adapter = ViewProfileAdapter


    }
}