package me.ckhoidea.metalake.repository

import me.ckhoidea.metalake.domain.PluginEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.transaction.annotation.Transactional

interface PluginRepository : JpaRepository<PluginEntity, Long> {
    fun findByPluginUID(uid: String): PluginEntity?

    @Transactional
    fun deleteByPluginUID(uid: String): Long
}