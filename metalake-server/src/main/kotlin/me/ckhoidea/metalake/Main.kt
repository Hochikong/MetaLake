package me.ckhoidea.metalake


import me.ckhoidea.metalake.repository.AuthHashRepository
import me.ckhoidea.metalake.service.PluginCentreService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration
import org.springframework.core.annotation.Order


// 禁用默认生成密码
@SpringBootApplication(exclude = [UserDetailsServiceAutoConfiguration::class])
@Order(-1)
class MainApp(
    @Autowired val authHashRepo: AuthHashRepository,
    @Autowired val pluginCentreService: PluginCentreService,
) : CommandLineRunner {
    private var logger: Logger = LoggerFactory.getLogger(MainApp::class.java)
    override fun run(vararg args: String?) {
        if (authHashRepo.findAll().filter { it.isMainHash }.toList().isEmpty()) {
            println("WARNING: Remember to create a new password hash to protect your data!")
        }
        pluginCentreService.initAllPlugins()
    }
}

fun main(args: Array<String>) {
    SpringApplication.run(MainApp::class.java, *args)
    // close server when enter quit/exit in shell
    // SpringApplication.run(MainApp::class.java, *args).close()
}