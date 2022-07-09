package me.ckhoidea.metalake.repository

import me.ckhoidea.metalake.domain.SaltEntity
import org.springframework.data.jpa.repository.JpaRepository

interface SaltRepository : JpaRepository<SaltEntity, Long> {
}