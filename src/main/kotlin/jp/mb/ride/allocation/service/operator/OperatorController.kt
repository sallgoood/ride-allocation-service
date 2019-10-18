package jp.mb.ride.allocation.service.operator

import io.swagger.annotations.*
import org.springframework.data.domain.Pageable
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@Api(value = "api for operators")
@RestController
class OperatorController(
        val service: OperatorService) {

    @GetMapping("/operators/find-any-ride-requests")
    @ApiImplicitParams(
            ApiImplicitParam(name = "page", dataType = "integer", paramType = "query",
                    value = "results page you want to retrieve (0..N)", defaultValue = "0"),
            ApiImplicitParam(name = "size", dataType = "integer", paramType = "query",
                    value = "number of records per page.", defaultValue = "20"),
            ApiImplicitParam(name = "sort", allowMultiple = true, dataType = "string", paramType = "query",
                    value = "sorting criteria in the format: property(,asc|desc). default sort order is ascending. " +
                            "multiple sort criteria are supported.", example = "requestedAt.desc"))
    @ApiResponses(value = [
        ApiResponse(code = 200, message = "when successful"),
        ApiResponse(code = 401, message = "when authentication is insufficient"),
        ApiResponse(code = 403, message = "when authority is insufficient")
    ])
    fun findAllRideRequests(pageable: Pageable): AnyRideRequestQueryResult {
        return service.findAllRideRequests(pageable)
    }
}
