package me.ckhoidea.metalake.repository

import me.ckhoidea.metalake.domain.LakeBindingEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.transaction.annotation.Transactional

interface LakeBindingRepository : JpaRepository<LakeBindingEntity, Long> {
    fun findByDataSourceName(name: String): LakeBindingEntity?

    fun findByDataSource(name: String): LakeBindingEntity?

    fun findByPluginUID(uid: String): List<LakeBindingEntity?>

    @Transactional
    fun deleteByDataSourceName(name: String): Long
}