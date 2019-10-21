package jp.mb.ride.allocation.service.driver

import io.swagger.annotations.ApiImplicitParam
import io.swagger.annotations.ApiImplicitParams
import io.swagger.annotations.ApiResponse
import io.swagger.annotations.ApiResponses
import org.springframework.data.domain.Pageable
import org.springframework.security.core.Authentication
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import javax.validation.Valid

@RestController
class DriverController(
        val service: DriverService) {

    @GetMapping("/drivers/find-opened-ride-requests")
    @ApiImplicitParams(
            ApiImplicitParam(name = "page", dataType = "integer", paramType = "query",
                    value = "results page you want to retrieve (0..N)", defaultValue = "0"),
            ApiImplicitParam(name = "size", dataType = "integer", paramType = "query",
                    value = "number of records per page.", defaultValue = "20"),
            ApiImplicitParam(name = "sort", allowMultiple = true, dataType = "string", paramType = "query",
                    value = "sorting criteria in the format: property(,asc|desc). default sort order is ascending. " +
                            "multiple sort criteria are supported.", example = "requestedAt.desc"))
    @ApiResponses(
            ApiResponse(code = 200, message = "when successful"),
            ApiResponse(code = 400, message = "when request with invalid data"),
            ApiResponse(code = 401, message = "when authentication is insufficient"),
            ApiResponse(code = 403, message = "when authority is insufficient"))
    fun findOpenedRequests(pageable: Pageable): OpenedRideRequestQueryResult {
        return service.findOpenedRequests(pageable)
    }

    @PostMapping("/drivers/respond-ride")
    @ApiResponses(value = [
        ApiResponse(code = 200, message = "when successful"),
        ApiResponse(code = 400, message = "when request with invalid data"),
        ApiResponse(code = 401, message = "when authentication is insufficient"),
        ApiResponse(code = 403, message = "when authority is insufficient"),
        ApiResponse(code = 409, message = "when ride is already allocated by another driver"),
        ApiResponse(code = 422, message = "when ride is already allocated by another driver")
    ])
    fun respondRideRequest(@Valid @RequestBody command: RideResponseCommand, authentication: Authentication) {
        val driver = authentication.principal as UserDetails
        service.respondRideRequest(driver, command)
    }
}
