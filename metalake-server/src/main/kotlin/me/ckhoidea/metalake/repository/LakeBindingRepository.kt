package me.ckhoidea.metalake.repository

import me.ckhoidea.metalake.domain.LakeBindingEntity
import org.springframework.data.jpa.repository.JpaRepository

interface LakeBindingRepository : JpaRepository<LakeBindingEntity, Long> {
    fun findByDataSourceName(name: String): LakeBindingEntity?

    fun findByDataSource(name: String): LakeBindingEntity?

    fun findByPluginUID(uid: String): List<LakeBindingEntity?>
}