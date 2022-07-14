package me.ckhoidea.metalake.controller


import com.zaxxer.hikari.HikariDataSource
import me.ckhoidea.metalake.domain.dataclasses.QueryPost
import me.ckhoidea.metalake.repository.LakeBindingRepository
import me.ckhoidea.metalake.repository.PluginRepository
import me.ckhoidea.metalake.service.DataSourceService
import me.ckhoidea.metalake.service.PluginCentreService
import me.ckhoidea.metalake.share.LakePluginInterface
import org.apache.tomcat.util.http.fileupload.IOUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.web.bind.annotation.*
import java.io.IOException


@RestController
class SimpleAPI(
    @Autowired val dataSourceService: DataSourceService,
    @Autowired val pluginCentreService: PluginCentreService,
    @Autowired val lakeBindingRepo: LakeBindingRepository,
    @Autowired val pluginRepo: PluginRepository,
) {
    @GetMapping("/api/test", produces = ["application/json"])
    fun test(): Map<String, Any> {
        return mapOf(
            "status" to 200
        )
    }

    @PostMapping("/api/test1", produces = ["application/json"])
    fun testPost(@RequestBody qp: QueryPost): Map<String, Any> {
        val ds = dataSourceService.newConnectionPool(qp.lakeName) as HikariDataSource
        val jdbcTemplate = JdbcTemplate(ds)

        val lake = lakeBindingRepo.findByDataSourceName(qp.lakeName)
        if (lake != null) {
            val plugin = pluginRepo.findByPluginUID(lake.pluginUID) ?: return mapOf(
                "status" to 500
            )

            val driver = pluginCentreService.getInstance(plugin.nameClass)
            if (driver != null) {
                val realDriver = driver as Class<*>
                val instance = realDriver.getDeclaredConstructor().newInstance() as LakePluginInterface
                val sql = instance.translateRequests(qp.queryBody)
                return if (sql != "ERROR") {
                    val queryResult = jdbcTemplate.queryForList(sql)
                    mapOf(
                        "status" to 200,
                        "result" to instance.castResponse(queryResult)
                    )
                } else {
                    mapOf(
                        "status" to 200,
                        "result" to "NO_DATA"
                    )
                }
            }

            return mapOf(
                "status" to 200
            )
        }

        return mapOf(
            "status" to 500
        )
    }
}