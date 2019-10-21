package jp.mb.ride.allocation.service.passenger

import java.time.LocalDateTime

data class MyRideRequestQueryResult(
        val id: Long,
        val address: String,
        val requestedAt: LocalDateTime,
        val respondedAt: LocalDateTime?,
        val driverName: String?
)
