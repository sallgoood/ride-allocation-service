package jp.mb.ride.allocation.service.passenger

import javax.validation.constraints.Min
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

data class RideRequestCommand(

        @field:NotNull
        @field:Min(0)
        val passengerId: Long,

        @field:Size(min = 1, max = 200)
        val address: String
)
