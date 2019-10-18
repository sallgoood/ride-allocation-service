package jp.mb.ride.allocation.service.ride

import java.time.LocalDateTime
import javax.persistence.*
import javax.validation.constraints.Size

@Entity
@Table(name = "RIDE_REQUEST")
class RideRequest(

        @Id
        @GeneratedValue
        var id: Long?,

        @Column(nullable = false)
        val passengerId: Long,

        @Size(min = 1, max = 200)
        @Column(nullable = false)
        val address: String,

        @Column(nullable = false)
        val requestedAt: LocalDateTime,

        @Column
        var driverId: Long?,

        @Column
        var respondedAt: LocalDateTime?,

        @Column
        @Version
        var version: Long?) {

    companion object {
        fun requestedBy(passengerId: Long, address: String, requestedAt: LocalDateTime): RideRequest {
            return RideRequest(passengerId, address, requestedAt)
        }
    }

    fun allocateDriver(driverId: Long, respondedAt: LocalDateTime): RideRequest {
        this.driverId = driverId
        this.respondedAt = respondedAt
        return this
    }

    fun isAllocated(): Boolean {
        return driverId != null
    }

    private constructor(passengerId: Long, address: String, requestedAt: LocalDateTime) : this(
            null, passengerId, address, requestedAt, null, null, null
    )
}
