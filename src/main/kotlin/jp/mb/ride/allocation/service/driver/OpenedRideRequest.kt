package jp.mb.ride.allocation.service.driver

import java.time.LocalDateTime

data class OpenedRideRequest(
        val id: Long,
        val address: String,
        val requestedAt: LocalDateTime
)
