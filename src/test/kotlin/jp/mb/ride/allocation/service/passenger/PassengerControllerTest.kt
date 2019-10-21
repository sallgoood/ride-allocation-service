package jp.mb.ride.allocation.service.passenger

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import jp.mb.ride.allocation.service.SecurityDisabledIntegrationTest
import jp.mb.ride.allocation.service.driver.DriverControllerTest.Companion.ANY_DATE_TIME
import jp.mb.ride.allocation.service.ride.RideRequest.Companion.requestedBy
import jp.mb.ride.allocation.service.ride.RideRequestRepository
import org.hamcrest.Matchers.`is`
import org.hamcrest.Matchers.notNullValue
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultHandlers.print
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

internal class PassengerControllerTest : SecurityDisabledIntegrationTest() {

    @Autowired
    lateinit var repository: RideRequestRepository

    @BeforeEach
    fun clean() {
        repository.deleteAll()
    }

    @Test
    fun `when passenger request a ride then request should be persisted`() {
        val command = RideRequestCommand(ANY_PASSENGER_ID, ANY_ADDRESS)
        val commandJson = jacksonObjectMapper().writeValueAsString(command)

        mvc.perform(
                post("/passengers/request-ride")
                        .content(commandJson)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is2xxSuccessful)
                .andExpect(jsonPath("$", notNullValue()))

        val request = repository.findAllByPassengerId(ANY_PASSENGER_ID).first()
        assertEquals(ANY_ADDRESS, request.address)
    }

    @Test
    fun `when passenger request with invalid data then should return 400`() {
        val command = RideRequestCommand(ANY_INVALID_PASSENGER_ID, ANY_INVALID_ADDRESS)
        val commandJson = jacksonObjectMapper().writeValueAsString(command)

        mvc.perform(
                post("/passengers/request-ride")
                        .content(commandJson)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest)
    }

    @Test
    fun `when passenger request to confirm their ride request then return 200`() {
        val requested = repository.save(requestedBy(ANY_PASSENGER_ID, ANY_ADDRESS, ANY_DATE_TIME))
        mvc.perform(
                get("/passengers/ride-requests/{requestId}", requested.id))
                .andDo(print())
                .andExpect(status().is2xxSuccessful)
                .andExpect(jsonPath("id", `is`(requested.id!!.toInt())))
    }

    companion object {
        const val ANY_PASSENGER_ID = 0L
        const val ANY_ADDRESS = "anyAddress"
        const val ANY_INVALID_PASSENGER_ID = -1L
        const val ANY_INVALID_ADDRESS = ""
    }
}
