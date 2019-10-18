package jp.mb.ride.allocation.service.passenger

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import jp.mb.ride.allocation.service.IntegrationTestBase
import jp.mb.ride.allocation.service.ride.RideRequestRepository
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultHandlers.print
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

internal class PassengerControllerTest : IntegrationTestBase() {

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
                MockMvcRequestBuilders.post("/passengers/request-ride")
                        .content(commandJson)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is2xxSuccessful)

        val request = repository.findAllByPassengerId(ANY_PASSENGER_ID).first()
        assertEquals(ANY_ADDRESS, request.address)
    }

    @Test
    fun `when passenger request with invalid data then should return 400`() {
        val command = RideRequestCommand(ANY_INVALID_PASSENGER_ID, ANY_INVALID_ADDRESS)
        val commandJson = jacksonObjectMapper().writeValueAsString(command)

        mvc.perform(
                MockMvcRequestBuilders.post("/passengers/request-ride")
                        .content(commandJson)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest)
    }

    companion object {
        const val ANY_PASSENGER_ID = 0L
        const val ANY_ADDRESS = "anyAddress"
        const val ANY_INVALID_PASSENGER_ID = -1L
        const val ANY_INVALID_ADDRESS = ""
    }
}
