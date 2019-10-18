package jp.mb.ride.allocation.service.ride

import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository

interface RideRequestRepository : JpaRepository<RideRequest, Long> {

    fun findAllByPassengerId(id: Long): List<RideRequest>

    fun findAllByDriverIdIsNull(pageable: Pageable): List<RideRequest>

    fun findAllByOrderByRequestedAtDesc(pageable: Pageable): List<RideRequest>
}
