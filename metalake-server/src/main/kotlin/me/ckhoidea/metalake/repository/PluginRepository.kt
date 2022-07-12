package me.ckhoidea.metalake.repository

import me.ckhoidea.metalake.domain.PluginEntity
import org.springframework.data.jpa.repository.JpaRepository

interface PluginRepository : JpaRepository<PluginEntity, Long> {
}