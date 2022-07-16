package me.ckhoidea.metalake.service

import me.ckhoidea.metalake.common.ConnectionsCache
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import me.ckhoidea.metalake.domain.LakeBindingEntity
import me.ckhoidea.metalake.repository.LakeBindingRepository
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class DataSourceService(
    @Autowired val lakeBindingRepo: LakeBindingRepository
) {
    private val logger: Logger = LoggerFactory.getLogger(DataSourceService::class.java)
    private fun newDataSource(lakeBinding: LakeBindingEntity, poolSize: Int = 4): HikariDataSource {
        val config = HikariConfig()
        config.jdbcUrl = lakeBinding.dataSource
        if (lakeBinding.username.isNotEmpty() && lakeBinding.password.isNotEmpty()) {
            config.username = lakeBinding.username
            config.password = lakeBinding.password
        }
        config.poolName = "ConnectionPoolOf${lakeBinding.dataSourceName}"
        config.maximumPoolSize = poolSize
        return HikariDataSource(config)
    }

    fun newConnectionPool(dataSourceName: String): Any? {
        val allLakeConfigs = lakeBindingRepo.findAll().toList()
        val allLakeUniqueNames = allLakeConfigs.map { it.dataSourceName }.toList()
        if (dataSourceName !in allLakeUniqueNames) {
            return null
        } else {
            for (config in allLakeConfigs) {
                if (config.dataSourceName == dataSourceName) {
                    val ds = ConnectionsCache.cacheManager.getIfPresent(dataSourceName)
                    return if (ds != null) {
                        logger.info("Get connection pool from cache for `$dataSourceName`")
                        ds
                    } else {
                        logger.info("New connection pool for `$dataSourceName`")
                        ConnectionsCache.cacheManager.get(dataSourceName) { this.newDataSource(config) }
                    }
//
                }
            }
            return null
        }

    }

}