package com.example.transportit

import android.app.DatePickerDialog
import android.content.ContentValues
import android.os.Bundle
import android.text.format.DateFormat
import android.text.format.DateFormat.is24HourFormat
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.Toast
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import com.example.transportit.databinding.FragmentRequestBinding
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale


class requestFragment : Fragment() {
    private lateinit var binding: FragmentRequestBinding
    val args: requestFragmentArgs by navArgs()
    private lateinit var navController: NavController
    private lateinit var calendar: Calendar
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRequestBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init(view)
        val date = DateFormat.getDateFormat(context).format(Date())
        binding.apply {
            test.text = args.username

            etDatePicker.setOnClickListener{
                showDatePicker()
            }
            etTimePicker.setOnClickListener{
                openTimepicker()
            }

            btnReq.setOnClickListener {
                binding.apply {
                    val from = etFrom.text.toString().trim()
                    val to = etTo.text.toString().trim()
                    val dateRequest = etDatePicker.text.toString()
                    val time = etTimePicker.text.toString()
                    val goodsType = etGoodsType.text.toString().trim()
                    val goodsWeight = etGoodsWeight.text.toString()
                    val message = etMessage.text.toString()
                    val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                    val currentDate = sdf.format(Date())

                    if (from.isNotEmpty() && to.isNotEmpty() && goodsType.isNotEmpty() && message.isNotEmpty()) {
                        if (from.length <= 30 && to.length <= 30 && goodsType.length <= 30) {
                            val cv = ContentValues()
                            cv.put("ownerId", args.ownerId)
                            cv.put("requesterId", args.requesterId)
                            cv.put("requesterUsername", args.username)
                            cv.put("fromLocation", from)
                            cv.put("toLocation", to)
                            cv.put("dateRequested", dateRequest)
                            cv.put("time", time)
                            cv.put("goodsType", goodsType)
                            cv.put("goodsWeight", goodsWeight)
                            cv.put("message", message)
                            cv.put("date", currentDate)
                            cv.put("vehicleName", args.truckName)
                            cv.put("vehicleId", args.id)
                            val pending = "Pending"
                            cv.put("status", pending)

                            val helper = context?.let { MyHelper(it) }
                            val db = helper?.writableDatabase
                            val newRowId = db?.insert("BOOKING", null, cv)

                            if (newRowId != -1L) {
                                Toast.makeText(context, "Booked successfully", Toast.LENGTH_SHORT).show()
                                val navController = Navigation.findNavController(view)
                                navController.popBackStack(R.id.homeFragment, false)
                            } else {
                                Toast.makeText(
                                    context,
                                    "An error occurred while booking.",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        } else {
                            Toast.makeText(context, "Location fields and Goods Type must be 30 characters or less.", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(context, "Please fill in all fields.", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }



    private fun openTimepicker() {
        val isSystem24Hour = is24HourFormat(requireContext())
        val clockFormat = if(isSystem24Hour) TimeFormat.CLOCK_24H else TimeFormat.CLOCK_12H

        val picker  = MaterialTimePicker.Builder()
            .setTimeFormat(clockFormat)
            .setHour(12)
            .setMinute(0)
            .setTitleText("Pick Time")
            .build()
        picker.show(childFragmentManager, "TAG")
        picker.addOnPositiveButtonClickListener{
            val formattedHour = String.format("%02d", picker.hour)
            val formattedMinute = String.format("%02d", picker.minute)

            val amPm = if (picker.hour >= 12) "PM" else "AM"
            val displayHour = if (picker.hour > 12) picker.hour - 12 else picker.hour
            val time = "$displayHour:$formattedMinute $amPm"
            binding.etTimePicker.setText(time)
        }

    }

    private fun showDatePicker() {
        val currentDate = Calendar.getInstance()
        val datePickerDialog =
            context?.let {
               val datePicker = DatePickerDialog(
                    it,{ DatePicker, year: Int, monthOfYear: Int, dayOfMonth: Int ->
                        val selectedDate = Calendar.getInstance()
                        selectedDate.set(year, monthOfYear, dayOfMonth)
                        val dateFormat= SimpleDateFormat("MM/dd/yy", Locale.getDefault())
                        val formattedDate = dateFormat.format(selectedDate.time)
                        binding.etDatePicker.setText(formattedDate)
                    },
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)
                )
                datePicker.datePicker.minDate = currentDate.timeInMillis
                datePicker
            }
        datePickerDialog?.show()
    }

    private fun init(view: View) {
        navController = Navigation.findNavController(view)
        calendar = Calendar.getInstance()
    }

}