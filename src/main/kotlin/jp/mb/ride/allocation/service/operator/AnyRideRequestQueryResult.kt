package jp.mb.ride.allocation.service.operator

import org.springframework.data.domain.Page

data class AnyRideRequestQueryResult(
        val anyRideRequests: Page<AnyRideRequest>
)
