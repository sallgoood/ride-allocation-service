package jp.mb.ride.allocation.service.operator

import java.time.LocalDateTime

data class AnyRideRequest(
        val id: Long,
        val passengerName: String,
        val address: String,
        val requestedAt: LocalDateTime,
        var driverName: String?,
        var respondedAt: LocalDateTime?,
        val isAllocated: Boolean
)
