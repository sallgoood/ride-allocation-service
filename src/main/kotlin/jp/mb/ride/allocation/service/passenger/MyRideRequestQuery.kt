package jp.mb.ride.allocation.service.passenger

import javax.validation.constraints.NotNull

data class MyRideRequestQuery(
        @field:NotNull
        val requestId: Long?
)
