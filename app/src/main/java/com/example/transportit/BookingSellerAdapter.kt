package com.example.transportit



import android.content.ContentValues
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.example.transportit.databinding.BookingListBinding


class   BookingSellerAdapter(val bookingList : MutableList<BookingDataClass>): RecyclerView.Adapter<BookingSellerAdapter.ViewHolder>(){

    inner class ViewHolder(val binding: BookingListBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookingSellerAdapter.ViewHolder {
        val view = BookingListBinding.inflate(LayoutInflater.from(parent.context),parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: BookingSellerAdapter.ViewHolder, position: Int) {
        val currentItem = bookingList[position]
        with(holder){
            binding.apply {
                tvUsername.text = "${currentItem.requesterUsername} requested your contact to pickup you goods from ${currentItem.fromLocation} to ${currentItem.toLocation}"
                tvDateTime.text = "${currentItem.dateRequested} ${currentItem.time}"
                tvDatePosted.text = currentItem.date

                btnAccept.setOnClickListener {
                    currentItem.status = "Accepted"

                    val dbHelper = MyHelper(itemView.context)
                    val db = dbHelper.writableDatabase

                    val contentValues = ContentValues()
                    contentValues.put("status", "Accepted")

                    db.update("BOOKING", contentValues, "_id = ?", arrayOf(currentItem._id.toString()))
                    Toast.makeText(itemView.context, "Booking Accepted", Toast.LENGTH_SHORT).show()
                    removeItem(position)
                    notifyDataSetChanged()
                }
                btnReject.setOnClickListener {
                    currentItem.status = "Rejected"

                    val dbHelper = MyHelper(itemView.context)
                    val db = dbHelper.writableDatabase

                    val contentValues = ContentValues()
                    contentValues.put("status", "Rejected")

                    db.update("BOOKING", contentValues, "_id = ?", arrayOf(currentItem._id.toString()))
                    Toast.makeText(itemView.context, "Booking Rejected", Toast.LENGTH_SHORT).show()
                    removeItem(position)
                    notifyDataSetChanged()

                }

                btnViewDetails.setOnClickListener {
                    val action = BookingSellerFragmentDirections.actionBookingSellerFragmentToViewDetailsFragment2(
                        currentItem._id,
                        currentItem.requesterId.toString(),
                        currentItem.requesterUsername.toString(),
                        currentItem.ownerId.toString(),
                        currentItem.status.toString(),
                        currentItem.dateRequested.toString(),
                        currentItem.date.toString(),
                        currentItem.time.toString(),
                        currentItem.fromLocation.toString(),
                        currentItem.toLocation.toString(),
                        currentItem.goodsType.toString(),
                        currentItem.goodsWeight.toString(),
                        currentItem.message.toString()
                    )

                    Navigation.findNavController(itemView).navigate(action)
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return bookingList.size
    }

    fun removeItem(position: Int) {
        bookingList.removeAt(position)
        notifyItemRemoved(position)
    }

}