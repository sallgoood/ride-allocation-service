package jp.mb.ride.allocation.service.user

import io.swagger.annotations.ApiResponse
import io.swagger.annotations.ApiResponses
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import javax.validation.Valid

@RestController
class UserController(
        val service: UserService) {

    @PostMapping("/users/sign-up")
    @ApiResponses(value = [
        ApiResponse(code = 400, message = "when request with invalid data"),
        ApiResponse(code = 422, message = "when username already exists")
    ])
    fun signUpUser(@Valid @RequestBody command: UserSignUpCommand): TokenResponse {
        return service.signUpUser(command)
    }

    @PostMapping("/users/sign-in")
    @ApiResponses(value = [
        ApiResponse(code = 400, message = "when request with invalid data"),
        ApiResponse(code = 401, message = "when authentication is insufficient")
    ])
    fun signInUser(@Valid @RequestBody command: UserSignInCommand): TokenResponse {
        return service.signInUser(command)
    }
}
