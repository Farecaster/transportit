package com.example.transportit

import android.graphics.BitmapFactory
import android.provider.ContactsContract.Profile
import android.util.Base64
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.example.transportit.databinding.TruckListBinding

class ViewProfileAdapter(val truckList : MutableList<AvailableTruckDataClass>, val id: String,val username: String): RecyclerView.Adapter<ViewProfileAdapter.ViewHolder>(){

    inner class ViewHolder(val binding: TruckListBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewProfileAdapter.ViewHolder {
        val view = TruckListBinding.inflate(LayoutInflater.from(parent.context),parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewProfileAdapter.ViewHolder, position: Int) {
        val currentItem = truckList[position]
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
                val id = id
                rvTruck.setOnClickListener {
                    val action = ProfileFragmentDirections.actionProfileFragmentToViewFragment(
                        currentItem._id,
                        currentItem.truckName.toString(),
                        currentItem.dimension.toString(),
                        currentItem.payload.toString(),
                        currentItem.type.toString(),
                        currentItem.price.toString(),
                        currentItem.ownerId.toString(),
                        imageDataString,
                        id,
                        username
                    )
                    Navigation.findNavController(itemView).navigate(action)
                }

            }
        }
    }

    override fun getItemCount(): Int {
        return truckList.size
    }

}
