package com.example.transportit


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.transportit.databinding.FragmentBookingSellerBinding

class BookingSellerFragment : Fragment() {
    private lateinit var binding: FragmentBookingSellerBinding
    private lateinit var navController: NavController
    private lateinit var bookingList :MutableList<BookingDataClass>
    private lateinit var BookingSellerAdapter: BookingSellerAdapter
    val args: BookingSellerFragmentArgs by navArgs()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBookingSellerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init(view)

        val helper = context?.let { MyHelper(it) }
        val selection = "ownerId = ? AND status = ?"
        val selectionArgs = arrayOf(args.id, "Pending")
        val db = helper?.readableDatabase

        val cursor = db?.query("BOOKING", null, selection, selectionArgs, null, null, null)

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
            messageColumnIndex != -1) {
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
                            message
                        )
                    }

                    bookingList.add(bookingItem!!)
                }
                // Notify your adapter here
                BookingSellerAdapter.notifyDataSetChanged()
            }
        }

    }

    private fun init(view: View){
        navController = Navigation.findNavController(view)

        val usernameMap = mutableMapOf<String, String>()
        val recyclerView = binding.rvBookingSeller
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.setItemViewCacheSize(20)
        bookingList = mutableListOf()
        BookingSellerAdapter = BookingSellerAdapter(bookingList)
        recyclerView.adapter = BookingSellerAdapter
    }

}