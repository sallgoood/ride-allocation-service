package jp.mb.ride.allocation.service.user

import jp.mb.ride.allocation.service.exception.UsernameExistsException
import jp.mb.ride.allocation.service.security.JwtTokenManager
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import javax.security.auth.login.AccountNotFoundException

@Service
class UserService(
        val repository: UserRepository,
        val passwordEncoder: PasswordEncoder,
        val authenticationManager: AuthenticationManager,
        val tokenManager: JwtTokenManager) {

    fun signUpUser(command: UserSignUpCommand): TokenResponse {
        val (username, password, role) = command
        repository.findById(username).ifPresent { throw UsernameExistsException("$username already exists") }

        repository.save(User(username = username, password = passwordEncoder.encode(password), roles = mutableListOf(Role.valueOf(role))))
        return TokenResponse(tokenManager.createToken(username, listOf(Role.valueOf(role))))
    }

    fun signInUser(command: UserSignInCommand): TokenResponse {
        val (username, password) = command
        val user = repository.findById(username).orElseThrow { throw AccountNotFoundException("$username not found") }

        authenticationManager.authenticate(UsernamePasswordAuthenticationToken(username, password))
        return TokenResponse(tokenManager.createToken(username, user.roles))
    }
}
