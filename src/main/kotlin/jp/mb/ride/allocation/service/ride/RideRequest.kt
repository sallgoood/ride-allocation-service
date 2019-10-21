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
        val passengerName: String,

        @Size(min = 1, max = 200)
        @Column(nullable = false)
        val address: String,

        @Column(nullable = false)
        val requestedAt: LocalDateTime,

        @Column
        var driverName: String?,

        @Column
        var respondedAt: LocalDateTime?,

        @Column
        @Version
        var version: Long?) {

    companion object {
        fun requestedBy(passengerName: String, address: String, requestedAt: LocalDateTime): RideRequest {
            return RideRequest(passengerName, address, requestedAt)
        }
    }

    fun allocateDriver(driverName: String, respondedAt: LocalDateTime): RideRequest {
        this.respondedAt = respondedAt
        this.driverName = driverName
        return this
    }

    fun isAllocated(): Boolean {
        return driverName != null
    }

    private constructor(passengerName: String, address: String, requestedAt: LocalDateTime) : this(
            id = null, passengerName = passengerName, address = address, requestedAt = requestedAt,
            driverName = null, respondedAt = null, version = null)
}
