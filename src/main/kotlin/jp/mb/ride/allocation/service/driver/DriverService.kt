package jp.mb.ride.allocation.service.driver

import jp.mb.ride.allocation.service.ride.RideRequestRepository
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Service
import java.time.LocalDateTime.now
import javax.persistence.EntityNotFoundException

@Service
class DriverService(
        val repository: RideRequestRepository) {

    fun findOpenedRequests(pageable: Pageable): OpenedRideRequestQueryResult {
        //TODO find requests near driver's location(lat, lon)
        val openedRequests = repository.findAllByDriverNameIsNull(pageable)
                .map { OpenedRideRequest(id = it.id!!, address = it.address, requestedAt = it.requestedAt) }

        return OpenedRideRequestQueryResult(PageImpl
        (openedRequests, pageable, openedRequests.size.toLong()))
    }

    fun respondRideRequest(driver: UserDetails, command: RideResponseCommand) {
        val (requestId) = command
        val requested = repository.findById(requestId).orElseThrow { throw EntityNotFoundException("$requestId not found") }

        check(!requested.isAllocated()) { "$requestId already allocated" }

        val allocated = requested.allocateDriver(driver.username, now())
        repository.save(allocated)
    }
}
