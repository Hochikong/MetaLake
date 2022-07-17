package me.ckhoidea.metalake.controller


import com.zaxxer.hikari.HikariDataSource
import me.ckhoidea.metalake.domain.dataclasses.QueryPost
import me.ckhoidea.metalake.repository.LakeBindingRepository
import me.ckhoidea.metalake.repository.PluginRepository
import me.ckhoidea.metalake.service.DataSourceService
import me.ckhoidea.metalake.service.PluginCentreService
import me.ckhoidea.metalake.share.LakePluginInterface
import me.ckhoidea.metalake.utils.exceptionToString
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.web.bind.annotation.*


@RestController
class SimpleAPI(
    @Autowired val dataSourceService: DataSourceService,
    @Autowired val pluginCentreService: PluginCentreService,
    @Autowired val lakeBindingRepo: LakeBindingRepository,
    @Autowired val pluginRepo: PluginRepository,
) {
    private val logger: Logger = LoggerFactory.getLogger(SimpleAPI::class.java)
    private val notAllowSQLSegments = listOf(
        "update", "delete",
        "truncate", "insert", "create", "alter", "drop", "comment", "rename", "merge", "call", "explain"
    )

    @Value("\${web.enable_debug}")
    private var enableDebug = false

    @PostMapping("/query/simple", produces = ["application/json"])
    fun simpleHandler(@RequestBody qp: QueryPost): Map<String, Any> {
        logger.info("Endpoint: /query/simple, Lake: ${qp.lakeName}")
        val lake = lakeBindingRepo.findByDataSourceName(qp.lakeName)
        if (lake != null) {
            val plugin = pluginRepo.findByPluginUID(lake.pluginUID) ?: return mapOf(
                "code" to 404,
                "msg" to "Lake's plugin not found"
            )

            val driver = pluginCentreService.getInstance(plugin.nameClass)
            if (driver != null) {
                val ds = dataSourceService.newConnectionPool(qp.lakeName) as HikariDataSource
                val jdbcTemplate = JdbcTemplate(ds)

                val realDriverClass = driver as Class<*>
                val driverInstance = realDriverClass.getDeclaredConstructor().newInstance() as LakePluginInterface
                val sql = driverInstance.translateRequests(qp.queryBody)
                for (nAL in notAllowSQLSegments) {
                    if (nAL in sql.lowercase()) {
                        return mapOf(
                            "code" to 403,
                            "error" to "Only query allowed"
                        )
                    }
                }

                return try {
                    if (sql != "ERROR") {
                        val queryResult = jdbcTemplate.queryForList(sql)
                        mapOf(
                            "code" to 200,
                            "result" to driverInstance.castResponse(queryResult, mapOf("Key" to "TEST"))
                        )
                    } else {
                        mapOf(
                            "code" to 200,
                            "result" to "NO_DATA"
                        )
                    }
                } catch (e: Exception) {
                    val (sw, pw) = exceptionToString()
                    e.printStackTrace(pw)
                    mapOf(
                        "code" to 500,
                        "error" to if (enableDebug) sw.buffer.toString() else "Internal error"
                    )
                }
            }

            return mapOf(
                "status" to 500,
                "msg" to "Driver null"
            )
        }

        return mapOf(
            "status" to 403,
            "error" to if (enableDebug) "No such lake" else "Denied"
        )
    }
}