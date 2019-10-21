package jp.mb.ride.allocation.service.passenger

import javax.validation.constraints.Size

data class RideRequestCommand(
        @field:Size(min = 1, max = 200)
        val address: String
)
