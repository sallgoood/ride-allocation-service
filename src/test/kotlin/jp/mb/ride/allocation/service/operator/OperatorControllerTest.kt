package jp.mb.ride.allocation.service.operator

import jp.mb.ride.allocation.service.IntegrationTestBase
import jp.mb.ride.allocation.service.driver.DriverControllerTest.Companion.ANY_DATE_TIME
import jp.mb.ride.allocation.service.driver.DriverControllerTest.Companion.ANY_DRIVER_ID
import jp.mb.ride.allocation.service.passenger.PassengerControllerTest.Companion.ANY_ADDRESS
import jp.mb.ride.allocation.service.passenger.PassengerControllerTest.Companion.ANY_PASSENGER_ID
import jp.mb.ride.allocation.service.ride.RideRequest.Companion.requestedBy
import jp.mb.ride.allocation.service.ride.RideRequestRepository
import org.hamcrest.Matchers.`is`
import org.hamcrest.Matchers.hasSize
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultHandlers.print
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

internal class OperatorControllerTest : IntegrationTestBase() {

    @Autowired
    lateinit var repository: RideRequestRepository

    @BeforeEach
    fun clean() {
        repository.deleteAll()
    }

    @Test
    fun `when operator query all requests then should return all requests with order by recently requested`() {
        val today = requestedBy(ANY_PASSENGER_ID, ANY_ADDRESS, ANY_DATE_TIME)
        val yesterday = requestedBy(ANY_PASSENGER_ID, ANY_ADDRESS, ANY_DATE_TIME.minusDays(1))
        val twoDaysAgo = requestedBy(ANY_PASSENGER_ID, ANY_ADDRESS, ANY_DATE_TIME.minusDays(2)).allocateDriver(ANY_DRIVER_ID, ANY_DATE_TIME)

        val latest = repository.save(today)
        val mid = repository.save(yesterday)
        val oldest = repository.save(twoDaysAgo)

        mvc.perform(
                get("/operators/find-any-ride-requests"))
                .andDo(print())
                .andExpect(status().is2xxSuccessful)
                .andExpect(jsonPath("anyRideRequests.content", hasSize<AnyRideRequest>(3)))
                .andExpect(jsonPath("anyRideRequests.content[0].id", `is`(latest.id!!.toInt())))
                .andExpect(jsonPath("anyRideRequests.content[1].id", `is`(mid.id!!.toInt())))
                .andExpect(jsonPath("anyRideRequests.content[2].id", `is`(oldest.id!!.toInt())))
    }
}
