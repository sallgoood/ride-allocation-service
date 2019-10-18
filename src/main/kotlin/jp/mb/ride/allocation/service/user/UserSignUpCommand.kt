package jp.mb.ride.allocation.service.user

import javax.validation.constraints.Email
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Pattern

data class UserSignUpCommand(
        @field:Email
        val username: String,

        @field:NotBlank
        val password: String,

        @field:Pattern(regexp = "^$|ROLE_PASSENGER|ROLE_DRIVER|ROLE_OPERATOR", message = "allowed roles are, ROLE_PASSENGER, ROLE_DRIVER, ROLE_OPERATOR")
        val role: String
)
