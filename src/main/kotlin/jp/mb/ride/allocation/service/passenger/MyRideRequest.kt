package jp.mb.ride.allocation.service.passenger

import java.time.LocalDateTime

data class MyRideRequest(
        val id: Long,
        val address: String,
        val requestedAt: LocalDateTime,
        val respondedAt: LocalDateTime?
)
