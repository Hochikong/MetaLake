package me.ckhoidea.metalake.repository

import me.ckhoidea.metalake.domain.AuthHashEntity
import org.springframework.data.jpa.repository.JpaRepository

interface AuthHashRepository : JpaRepository<AuthHashEntity, Long> {}