package com.example.transportit

import android.graphics.Bitmap

data class AvailableTruckDataClass(
    val _id: Long,
    val truckName: String? = null,
    val dimension: String? = null,
    val payload: String? = null,
    val type: String? = null,
    val price: String? = null,
    val ownerId: String? = null,
    val imageData: ByteArray?= null
                                   ) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as AvailableTruckDataClass

        if (_id != other._id) return false
        if (truckName != other.truckName) return false
        if (dimension != other.dimension) return false
        if (payload != other.payload) return false
        if (type != other.type) return false
        if (price != other.price) return false
        if (ownerId != other.ownerId) return false
        if (imageData != null) {
            if (other.imageData == null) return false
            if (!imageData.contentEquals(other.imageData)) return false
        } else if (other.imageData != null) return false

        return true
    }

    override fun hashCode(): Int {
        var result = _id.hashCode()
        result = 31 * result + (truckName?.hashCode() ?: 0)
        result = 31 * result + (dimension?.hashCode() ?: 0)
        result = 31 * result + (payload?.hashCode() ?: 0)
        result = 31 * result + (type?.hashCode() ?: 0)
        result = 31 * result + (price?.hashCode() ?: 0)
        result = 31 * result + (ownerId?.hashCode() ?: 0)
        result = 31 * result + (imageData?.contentHashCode() ?: 0)
        return result
    }
}
