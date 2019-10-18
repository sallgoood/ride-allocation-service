package jp.mb.ride.allocation.service.driver

import org.springframework.data.domain.Page

data class OpenedRideRequestQueryResult(
        val openedRideRequests: Page<OpenedRideRequest>
)
