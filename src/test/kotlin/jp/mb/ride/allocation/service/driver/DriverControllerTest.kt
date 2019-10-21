package jp.mb.ride.allocation.service.driver

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import jp.mb.ride.allocation.service.SecurityDisabledIntegrationTest
import jp.mb.ride.allocation.service.passenger.PassengerControllerTest.Companion.ANY_ADDRESS
import jp.mb.ride.allocation.service.passenger.PassengerControllerTest.Companion.ANY_PASSENGER_ID
import jp.mb.ride.allocation.service.ride.RideRequest.Companion.requestedBy
import jp.mb.ride.allocation.service.ride.RideRequestRepository
import org.hamcrest.Matchers.hasSize
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultHandlers.print
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.time.LocalDateTime.now

internal class DriverControllerTest : SecurityDisabledIntegrationTest() {

    @Autowired
    lateinit var repository: RideRequestRepository

    @BeforeEach
    fun clean() {
        repository.deleteAll()
    }

    @Test
    fun `when driver query requests to respond then only opened requests should be returned`() {
        repository.saveAll(listOf(
                requestedBy(ANY_PASSENGER_ID, ANY_ADDRESS, ANY_DATE_TIME),
                requestedBy(ANY_PASSENGER_ID, ANY_ADDRESS, ANY_DATE_TIME),
                requestedBy(ANY_PASSENGER_ID, ANY_ADDRESS, ANY_DATE_TIME).allocateDriver(ANY_DRIVER_ID, ANY_DRIVER_NAME, ANY_DATE_TIME)))

        mvc.perform(
                get("/drivers/find-opened-ride-requests"))
                .andDo(print())
                .andExpect(status().is2xxSuccessful)
                .andExpect(jsonPath("openedRideRequests.content", hasSize<OpenedRideRequest>(2)))
    }

    @Test
    fun `when driver response to a request then response should be persisted`() {
        val requested = repository.save(requestedBy(ANY_PASSENGER_ID, ANY_ADDRESS, ANY_DATE_TIME))

        val command = RideResponseCommand(requested.id!!, ANY_DRIVER_ID)
        val commandJson = jacksonObjectMapper().writeValueAsString(command)

        mvc.perform(
                post("/drivers/respond-ride")
                        .content(commandJson)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is2xxSuccessful)

        val responded = repository.findById(requested.id!!).get()
        assertTrue(responded.isAllocated())
        assertNotNull(ANY_DRIVER_NAME)
    }

    @Test
    fun `when driver response to a allocated request then should return 422`() {
        val allocated = repository.save(requestedBy(ANY_PASSENGER_ID, ANY_ADDRESS, ANY_DATE_TIME)
                .allocateDriver(ANY_DRIVER_ID, ANY_DRIVER_NAME, ANY_DATE_TIME))

        val command = RideResponseCommand(allocated.id!!, ANY_DRIVER_ID)
        val commandJson = jacksonObjectMapper().writeValueAsString(command)

        mvc.perform(
                post("/drivers/respond-ride")
                        .content(commandJson)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity)

        val responded = repository.findById(allocated.id!!).get()
        assertTrue(responded.isAllocated())
    }

    @Test
    fun `when driver request with invalid data then should return 400`() {
        val command = RideResponseCommand(ANY_INVALID_REQUEST_ID, ANY_INVALID_DRIVER_ID)
        val commandJson = jacksonObjectMapper().writeValueAsString(command)

        mvc.perform(
                post("/drivers/respond-ride")
                        .content(commandJson)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest)
    }

    companion object {
        val ANY_DATE_TIME = now()
        const val ANY_DRIVER_ID = 0L
        const val ANY_DRIVER_NAME = "anyDriver@driver.com"
        const val ANY_INVALID_REQUEST_ID = -1L
        const val ANY_INVALID_DRIVER_ID = -1L
    }
}
