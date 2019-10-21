package jp.mb.ride.allocation.service.operator

import jp.mb.ride.allocation.service.ride.RideRequestRepository
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

@Service
class OperatorService(
        val repository: RideRequestRepository) {

    fun findAllRideRequests(pageable: Pageable): AnyRideRequestQueryResult {
        val anyRideRequests = repository.findAllByOrderByRequestedAtDesc(pageable)
                .map {
                    AnyRideRequest(id = it.id!!, passengerName = it.passengerName, address = it.address,
                            requestedAt = it.requestedAt, driverName = it.driverName, respondedAt = it.respondedAt,
                            isAllocated = it.isAllocated())
                }

        return AnyRideRequestQueryResult(PageImpl
        (anyRideRequests, pageable, anyRideRequests.size.toLong()))
    }
}
