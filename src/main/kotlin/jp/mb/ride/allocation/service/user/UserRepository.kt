package jp.mb.ride.allocation.service.user

import org.springframework.data.jpa.repository.JpaRepository


interface UserRepository : JpaRepository<User, String>
