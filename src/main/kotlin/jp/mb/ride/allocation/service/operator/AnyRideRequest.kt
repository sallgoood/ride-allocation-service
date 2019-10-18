package jp.mb.ride.allocation.service.operator

import java.time.LocalDateTime

data class AnyRideRequest(
        val id: Long,
        val passengerId: Long,
        val address: String,
        val requestedAt: LocalDateTime,
        var driverId: Long?,
        var respondedAt: LocalDateTime?,
        val isAllocated: Boolean
)
