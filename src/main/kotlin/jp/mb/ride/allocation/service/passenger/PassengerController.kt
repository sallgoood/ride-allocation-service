package jp.mb.ride.allocation.service.passenger

import io.swagger.annotations.ApiResponse
import io.swagger.annotations.ApiResponses
import org.springframework.security.core.Authentication
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.web.bind.annotation.*
import javax.validation.Valid


@RestController
class PassengerController(val passengerService: PassengerService) {

    @PostMapping("/passengers/request-ride")
    @ApiResponses(value = [
        ApiResponse(code = 200, message = "when successful"),
        ApiResponse(code = 400, message = "when request with invalid data"),
        ApiResponse(code = 401, message = "when authentication is insufficient"),
        ApiResponse(code = 403, message = "when authority is insufficient")
    ])
    fun requestRide(@Valid @RequestBody command: RideRequestCommand, authentication: Authentication): Long {
        val passenger = authentication.principal as UserDetails
        return passengerService.requestRide(passenger, command)
    }

    @GetMapping("/passengers/ride-requests/{requestId}")
    fun findMyRequest(@PathVariable requestId: Long): MyRideRequestQueryResult {
        return passengerService.findMyRequest(requestId)
    }
}
