package com.example.transportit

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.transportit.databinding.FragmentBookingBinding

class BookingFragment : Fragment() {
    private lateinit var binding: FragmentBookingBinding
    private lateinit var bookingList :MutableList<BookingDataClass>
    private lateinit var BookingAdapter: BookingAdapter
    val args: BookingFragmentArgs by navArgs()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBookingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()

        val helper = context?.let { MyHelper(it) }

        val db = helper?.readableDatabase

        val cursor = db?.query("BOOKING", null, "requesterId = ?", arrayOf(args.id), null, null, null)


        val idColumnIndex = cursor?.getColumnIndex("_id")
        val requesterIdColumnIndex = cursor?.getColumnIndex("requesterId")
        val requesterUsernameColumnIndex = cursor?.getColumnIndex("requesterUsername")
        val ownerIdColumnIndex = cursor?.getColumnIndex("ownerId")
        val statusColumnIndex = cursor?.getColumnIndex("status")
        val dateRequestedColumnIndex = cursor?.getColumnIndex("dateRequested")
        val dateColumnIndex = cursor?.getColumnIndex("date")
        val timeColumnIndex = cursor?.getColumnIndex("time")
        val fromLocationColumnIndex = cursor?.getColumnIndex("fromLocation")
        val toLocationColumnIndex = cursor?.getColumnIndex("toLocation")
        val goodsTypeColumnIndex = cursor?.getColumnIndex("goodsType")
        val goodsWeightColumnIndex = cursor?.getColumnIndex("goodsWeight")
        val messageColumnIndex = cursor?.getColumnIndex("message")
        val vehicleNameColumnIndex = cursor?.getColumnIndex("vehicleName")
        val vehicleIdColumnIndex = cursor?.getColumnIndex("vehicleId")

        if (idColumnIndex != -1 &&
            requesterIdColumnIndex != -1 &&
            requesterUsernameColumnIndex != -1 &&
            ownerIdColumnIndex != -1 &&
            statusColumnIndex != -1 &&
            dateRequestedColumnIndex != -1 &&
            dateColumnIndex != -1 &&
            timeColumnIndex != -1 &&
            fromLocationColumnIndex != -1 &&
            toLocationColumnIndex != -1 &&
            goodsTypeColumnIndex != -1 &&
            goodsWeightColumnIndex != -1 &&
            messageColumnIndex != -1 &&
            vehicleNameColumnIndex != -1 &&
            vehicleIdColumnIndex != -1) {
            bookingList.clear()
            cursor?.let {
                while (it.moveToNext()) {
                    val id = idColumnIndex?.let { it1 -> it.getLong(it1) }
                    val requesterId = requesterIdColumnIndex?.let { it1 -> it.getString(it1) }
                    val requesterUsername = requesterUsernameColumnIndex?.let { it1 -> it.getString(it1) }
                    val ownerId = ownerIdColumnIndex?.let { it1 -> it.getString(it1) }
                    val status = statusColumnIndex?.let { it1 -> it.getString(it1) }
                    val dateRequested = dateRequestedColumnIndex?.let { it1 -> it.getString(it1) }
                    val date = dateColumnIndex?.let { it1 -> it.getString(it1) }
                    val time = timeColumnIndex?.let { it1 -> it.getString(it1) }
                    val fromLocation = fromLocationColumnIndex?.let { it1 -> it.getString(it1) }
                    val toLocation = toLocationColumnIndex?.let { it1 -> it.getString(it1) }
                    val goodsType = goodsTypeColumnIndex?.let { it1 -> it.getString(it1) }
                    val goodsWeight = goodsWeightColumnIndex?.let { it1 -> it.getString(it1) }
                    val message = messageColumnIndex?.let { it1 -> it.getString(it1) }
                    val vehicleName = vehicleNameColumnIndex?.let { it1 -> it.getString(it1) }
                    val vehicleId = vehicleIdColumnIndex?.let { it1 -> it.getString(it1) }


                    val bookingItem = id?.let { it1 ->
                        BookingDataClass(
                            it1,
                            requesterId,
                            requesterUsername,
                            ownerId,
                            status,
                            dateRequested,
                            date,
                            time,
                            fromLocation,
                            toLocation,
                            goodsType,
                            goodsWeight,
                            message,
                            vehicleName,
                            vehicleId,
                        )
                    }

                    bookingList.add(bookingItem!!)
                }
                // Notify your adapter here
                BookingAdapter.notifyDataSetChanged()
            }
        }



    }
    private fun init(){

        val recyclerView = binding.rvBooking
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.setItemViewCacheSize(20)
        bookingList = mutableListOf()
        BookingAdapter = BookingAdapter(bookingList)
        recyclerView.adapter = BookingAdapter
    }
}