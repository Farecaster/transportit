package com.example.transportit


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.transportit.databinding.RequestedbookingListBinding

class BookingAdapter(val bookingListt : MutableList<BookingDataClass>): RecyclerView.Adapter<BookingAdapter.ViewHolder>(){

    inner class ViewHolder(val binding: RequestedbookingListBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookingAdapter.ViewHolder {
        val view =RequestedbookingListBinding.inflate(LayoutInflater.from(parent.context),parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: BookingAdapter.ViewHolder, position: Int) {
        val currentItem = bookingListt[position]
        with(holder){
            binding.apply {
                tvToLocationRequestedBooking.text = currentItem.fromLocation
                tvToLocationRequested.text = currentItem.toLocation
                tvDatePostedBooking.text = currentItem.dateRequested
                tvDateRequestedBooking.text = currentItem.date
                tvVehicleName.text = currentItem.vehicleName
                tvVehicleId.text = currentItem.vehicleId
                when (currentItem.status) {
                    "Accepted" -> {
                        binding.tvStatus.setTextColor(ContextCompat.getColor(holder.itemView.context, R.color.green))
                        binding.tvStatus.text = "Accepted"
                    }
                    "Rejected" -> {
                        binding.tvStatus.setTextColor(ContextCompat.getColor(holder.itemView.context, R.color.red))
                        binding.tvStatus.text = "Rejected"
                    }
                    "Pending" -> {
                        binding.tvStatus.setTextColor(ContextCompat.getColor(holder.itemView.context, R.color.yellow))
                        binding.tvStatus.text = "Pending"
                    }
                }

            }
        }
    }

    override fun getItemCount(): Int {
        return bookingListt.size
    }
}