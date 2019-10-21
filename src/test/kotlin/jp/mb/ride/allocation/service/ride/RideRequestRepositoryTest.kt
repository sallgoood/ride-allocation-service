package jp.mb.ride.allocation.service.ride

import jp.mb.ride.allocation.service.driver.DriverControllerTest.Companion.ANY_DRIVER_NAME
import jp.mb.ride.allocation.service.ride.RideRequest.Companion.requestedBy
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.orm.ObjectOptimisticLockingFailureException
import java.time.LocalDateTime.now


@SpringBootTest
internal class RideRequestRepositoryTest {

    @Autowired
    lateinit var repository: RideRequestRepository

    @Test
    fun `ride request with optimistic locking`() {
        val request = repository.save(requestedBy("AnyName", "AnyAddress", now()))

        val request01 = repository.findById(request.id!!).get()
        val request02 = repository.findById(request.id!!).get()

        assertEquals(request01.id, request02.id)

        repository.save(request01.allocateDriver(ANY_DRIVER_NAME, now()))

        assertThrows(ObjectOptimisticLockingFailureException::class.java)
        { repository.save(request02.allocateDriver(ANY_DRIVER_NAME, now())) }
    }
}
