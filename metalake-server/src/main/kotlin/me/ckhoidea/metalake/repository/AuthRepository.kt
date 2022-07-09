package me.ckhoidea.metalake.repository

import me.ckhoidea.metalake.domain.AuthEntity
import org.springframework.data.jpa.repository.JpaRepository

interface AuthRepository : JpaRepository<AuthEntity, Long> {
}