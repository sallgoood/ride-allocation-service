package jp.mb.ride.allocation.service.driver

import javax.validation.constraints.Min
import javax.validation.constraints.NotNull

data class RideResponseCommand(
        @field:NotNull
        @field:Min(0)
        val requestId: Long
)
