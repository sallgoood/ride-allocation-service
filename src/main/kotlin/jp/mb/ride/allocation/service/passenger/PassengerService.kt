package jp.mb.ride.allocation.service.passenger

import jp.mb.ride.allocation.service.ride.RideRequest.Companion.requestedBy
import jp.mb.ride.allocation.service.ride.RideRequestRepository
import org.springframework.stereotype.Service
import java.time.LocalDateTime.now
import javax.persistence.EntityNotFoundException

@Service
class PassengerService(
        val repository: RideRequestRepository) {

    fun requestRide(command: RideRequestCommand): Long {
        val (passengerId, address) = command
        val request = requestedBy(passengerId = passengerId, address = address, requestedAt = now())
        return repository.save(request).id!!
    }

    fun findMyRequest(requestId: Long): MyRideRequestQueryResult {
        val myRequest = repository.findById(requestId).orElseThrow { throw EntityNotFoundException("$requestId not found") }
        return MyRideRequestQueryResult(myRequest.id!!, myRequest.address, myRequest.requestedAt, myRequest.respondedAt, myRequest.driverName)
    }
}
