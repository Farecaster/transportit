package com.example.transportit


import android.graphics.BitmapFactory
import android.util.Base64
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.Navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

import com.example.transportit.databinding.TruckListBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.squareup.picasso.Picasso
import java.io.File


class SellerAdapter(val trucklist: MutableList<AvailableTruckDataClass>): RecyclerView.Adapter<SellerAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: TruckListBinding):RecyclerView.ViewHolder(binding.root)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SellerAdapter.ViewHolder {
        val view = TruckListBinding.inflate(LayoutInflater.from(parent.context), parent , false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: SellerAdapter.ViewHolder, position: Int) {
        val currentItem = trucklist[position]

        with(holder){
            binding.apply {
                tvName.text = currentItem.truckName
                tvDimension.text = currentItem.dimension
                tvPayload.text = currentItem.payload
                tvType.text = currentItem.type
                tvPrice.text = currentItem.price


                // Convert the ByteArray to a Bitmap
                val byteArray = currentItem.imageData
                val bitmap = byteArray?.let { BitmapFactory.decodeByteArray(byteArray, 0, it.size) }

                // Set the Bitmap in the ImageView
                imageView.setImageBitmap(bitmap)

                val imageDataString = Base64.encodeToString(currentItem.imageData, Base64.DEFAULT)
                rvTruck.setOnClickListener{
                    val action = sellerFragmentDirections.actionSellerFragmentToUpdateFragment(
                        currentItem._id,
                        currentItem.truckName.toString(),
                        currentItem.dimension.toString(),
                        currentItem.payload.toString(),
                        currentItem.type.toString(),
                        currentItem.price.toString(),
                        currentItem.ownerId.toString(),
                        imageDataString
                    )
                    findNavController(itemView).navigate(action)
                }

                rvTruck.setOnLongClickListener {
                    MaterialAlertDialogBuilder(holder.itemView.context)
                        .setTitle("Delete Item permanently")
                        .setMessage("Are you sure you want to delete this item?")
                        .setPositiveButton("Yes"){_,_ ->
                            val itemPosition = holder.adapterPosition

                            if (itemPosition != RecyclerView.NO_POSITION) {
                                val currentItem = trucklist[itemPosition] // Get the item from your dataset
                                val context = itemView.context
                                val helper = MyHelper(context)
                                val db = helper.writableDatabase

                                val idToDelete = currentItem._id // Assuming you can access the ID of the current item

                                if (idToDelete != null) {
                                    val deletedRows = db.delete("TRUCKS", "_id = ?", arrayOf(idToDelete.toString()))

                                    if (deletedRows > 0) {
                                        // Item deleted successfully from the database
                                        // Remove the item from the dataset and notify the adapter
                                        trucklist.removeAt(itemPosition)
                                        notifyItemRemoved(itemPosition)
                                    }
                                }
                            }
                        }
                        .setNegativeButton("No"){_,_ ->
                            Toast.makeText(itemView.context, "Canceled", Toast.LENGTH_SHORT).show()
                        }.show()
                    return@setOnLongClickListener true
                }

            }
        }
    }

    override fun getItemCount(): Int {
        return trucklist.size
    }
}