package me.ckhoidea.metalake


import me.ckhoidea.metalake.repository.AuthHashRepository
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
    @Autowired val authHashRepo: AuthHashRepository
) : CommandLineRunner {
    private var logger: Logger = LoggerFactory.getLogger(MainApp::class.java)
    override fun run(vararg args: String?) {
        if (authHashRepo.findAll().filter { it.isMainHash }.toList().isEmpty()) {
            println("WARNING: Remember to create a new password hash to protect your data!")
        }

//        val path =
//            "C:\\Users\\ckhoi\\IdeaProjects\\MetaLake\\metalake-plugin\\build\\libs\\metalake-plugin-0.0.1-SNAPSHOT.jar"
//        val absp = "file:$path"
//        val m = JarFile(path).manifest
//        val ma = m.mainAttributes
//        val classNames = ma.keys.filter { it.toString().startsWith("PluginRootName") }.map { ma[it] }.toList()
//        println(classNames)
//
//        val urlClassLoader = URLClassLoader(arrayOf(URL(absp)))
//        val driver = urlClassLoader.loadClass("me.ckhoidea.lakeplugin.SimplePlugin")
//        val instance = driver.getDeclaredConstructor().newInstance()
//        println(driver.declaredMethods.forEach { println(it.name) })
//
//        val met: Method = driver.getDeclaredMethod("plus", Int::class.java, Int::class.java)
//        println(met.invoke(instance, 1, 2))
//        urlClassLoader.close()
    }
}

fun main(args: Array<String>) {
    // close server when enter quit/exit in shell
    SpringApplication.run(MainApp::class.java, *args).close()
}