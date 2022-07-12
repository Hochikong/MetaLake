package me.ckhoidea.metalake


import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import me.ckhoidea.metalake.repository.AuthHashRepository
import me.ckhoidea.metalake.service.PluginCentreService
import me.ckhoidea.metalake.share.LakePluginBase
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration
import org.springframework.core.annotation.Order
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.datasource.DriverManagerDataSource
import java.lang.reflect.Method
import java.net.URL
import java.net.URLClassLoader
import java.util.jar.JarFile


// 禁用默认生成密码
@SpringBootApplication(exclude = [UserDetailsServiceAutoConfiguration::class])
@Order(-1)
class MainApp(
    @Autowired val authHashRepo: AuthHashRepository,
    @Autowired val pluginCentreService: PluginCentreService
) : CommandLineRunner {
    private var logger: Logger = LoggerFactory.getLogger(MainApp::class.java)
    override fun run(vararg args: String?) {
        if (authHashRepo.findAll().filter { it.isMainHash }.toList().isEmpty()) {
            println("WARNING: Remember to create a new password hash to protect your data!")
        }

        pluginCentreService.initAllPlugins()
        val driver = pluginCentreService.getInstance("me.ckhoidea.lakeplugin.SimplePlugin")
        if (driver != null) {
            val realDriver = driver as Class<*>
            val instance = realDriver.getDeclaredConstructor().newInstance() as LakePluginBase
            println(instance.translateRequests("SSS"))
//            val met: Method = realDriver.getDeclaredMethod("plus", Int::class.java, Int::class.java)
//            println(met.invoke(instance, 1, 2))
        }

//        val dbDriver = "org.sqlite.JDBC"
//        val url = "jdbc:sqlite:G:\\MetaDataCenter\\djsb.db"
//        val dataSource = DriverManagerDataSource()
//        dataSource.url = url
//        dataSource.setDriverClassName(dbDriver)
//        val jdbcTemplate = JdbcTemplate(dataSource)
//        val result = jdbcTemplate.queryForList("SELECT * FROM djs_books LIMIT 5;")
//        println(result[0])

//        val config = HikariConfig()
//        config.jdbcUrl = url
//        config.poolName = "ConnectionPoolOfXX"
//        config.maximumPoolSize = 2
//        val ds = HikariDataSource(config)
//        val jdbcTemplate = JdbcTemplate(ds)
//        val result = jdbcTemplate.queryForList("SELECT * FROM djs_books LIMIT 5;")
//        println(result[0])
    }
}

fun main(args: Array<String>) {
    SpringApplication.run(MainApp::class.java, *args)
    // close server when enter quit/exit in shell
    // SpringApplication.run(MainApp::class.java, *args).close()
}