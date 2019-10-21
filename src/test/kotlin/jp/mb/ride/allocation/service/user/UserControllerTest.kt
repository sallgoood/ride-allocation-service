package jp.mb.ride.allocation.service.user

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import jp.mb.ride.allocation.service.SecurityEnabledIntegrationTest
import jp.mb.ride.allocation.service.user.Role.ROLE_OPERATOR
import org.hamcrest.Matchers.not
import org.hamcrest.text.IsEmptyString.emptyOrNullString
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultHandlers.print
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

internal class UserControllerTest : SecurityEnabledIntegrationTest() {

    @Autowired
    lateinit var repository: UserRepository

    @Autowired
    lateinit var passwordEncoder: PasswordEncoder

    @Autowired
    lateinit var service: UserService

    @BeforeEach
    fun clean() {
        repository.deleteAll()
    }

    @Test
    fun `when user sign up with valid data then token should be returned and user should be persisted`() {
        val command = UserSignUpCommand("myoungsokang@gmail.com", "MyBridge!2#4", "ROLE_OPERATOR")
        val commandJson = jacksonObjectMapper().writeValueAsString(command)

        mvc.perform(
                post("/users/sign-up")
                        .content(commandJson)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is2xxSuccessful)
                .andExpect(jsonPath("token", not(emptyOrNullString())))

        val user = repository.findById("myoungsokang@gmail.com").get()
        assertEquals("ROLE_OPERATOR", user.roles.first().name)
    }

    @Test
    fun `when user sign in with invalid username then 404`() {
        val command = UserSignInCommand("myoungsokang@gmail.com", "MyBridge!2#4")
        val commandJson = jacksonObjectMapper().writeValueAsString(command)

        mvc.perform(
                post("/users/sign-in")
                        .content(commandJson)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound)
    }

    @Test
    fun `when user sign in with valid credential then token should be returned`() {
        val (username, _, _) = repository.save(User("myoungsokang@gmail.com", passwordEncoder.encode("MyBridge!2#4"), mutableListOf(ROLE_OPERATOR)))

        val command = UserSignInCommand(username, "MyBridge!2#4")
        val commandJson = jacksonObjectMapper().writeValueAsString(command)

        mvc.perform(
                post("/users/sign-in")
                        .content(commandJson)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is2xxSuccessful)
                .andExpect(jsonPath("token", not(emptyOrNullString())))
    }

    @Test
    fun `when user use services with insufficient authentication then return 401`() {
        //no header
        mvc.perform(
                get("/operators/find-any-ride-requests"))
                .andDo(print())
                .andExpect(status().isUnauthorized)

        //no bearer token
        mvc.perform(
                get("/operators/find-any-ride-requests")
                        .header("Authorization", "InvalidValue"))
                .andDo(print())
                .andExpect(status().isUnauthorized)
    }

    @Test
    fun `when user use services with insufficient authority then return 403`() {
        val driverToken = service.signUpUser(UserSignUpCommand("driver@gmail.com", "MyBridge!2#4", "ROLE_DRIVER")).token

        mvc.perform(
                get("/operators/find-any-ride-requests")
                        .header("Authorization", "Bearer $driverToken"))
                .andDo(print())
                .andExpect(status().isForbidden)
    }

    @Test
    fun `when user sing up with already existing username then return 422`() {
        service.signUpUser(UserSignUpCommand("driver@gmail.com", "MyBridge!2#4", "ROLE_DRIVER"))

        val command = UserSignUpCommand("driver@gmail.com", "MyBridge!2#4", "ROLE_OPERATOR")
        val commandJson = jacksonObjectMapper().writeValueAsString(command)

        mvc.perform(
                post("/users/sign-up")
                        .content(commandJson)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity)
    }
}
