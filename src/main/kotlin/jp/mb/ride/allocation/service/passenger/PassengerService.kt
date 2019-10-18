package jp.mb.ride.allocation.service.passenger

import jp.mb.ride.allocation.service.ride.RideRequest.Companion.requestedBy
import jp.mb.ride.allocation.service.ride.RideRequestRepository
import org.springframework.stereotype.Service
import java.time.LocalDateTime.now

@Service
class PassengerService(
        val repository: RideRequestRepository) {

    fun requestRide(command: RideRequestCommand) {
        val (passengerId, address) = command
        val request = requestedBy(passengerId = passengerId, address = address, requestedAt = now())
        repository.save(request)
    }
}
