package com.example.transportit

data class BookingDataClass(val _id: Long,
                            val requesterId: String? = null,
                            val requesterUsername: String? = null,
                            val ownerId: String? = null,
                            var status: String? = null,
                            val dateRequested: String? = null,
                            val date: String? = null,
                            val time: String? = null,
                            val fromLocation: String? = null,
                            val toLocation: String? = null,
                            val goodsType: String? = null,
                            val goodsWeight: String? = null,
                            val message: String? = null,
                            val vehicleName: String? = null,
                            val vehicleId: String? = null
                            )
