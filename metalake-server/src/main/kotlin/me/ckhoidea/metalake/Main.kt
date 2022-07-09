package me.ckhoidea.metalake


import me.ckhoidea.metalake.repository.AuthRepository
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration
import org.springframework.core.annotation.Order
import java.lang.reflect.Method
import java.net.URL
import java.net.URLClassLoader
import java.util.jar.JarFile

// 禁用默认生成密码
@SpringBootApplication(exclude = [UserDetailsServiceAutoConfiguration::class])
@Order(-1)
class MainApp(
    @Autowired val authRepo: AuthRepository
) : CommandLineRunner {
    private var logger: Logger = LoggerFactory.getLogger(MainApp::class.java)

    override fun run(vararg args: String?) {
//        authRepo.save(
//            AuthEntity(
//                createTime = Date.from(string2instant("2022-07-10 00:06:00")),
//                updateTime = Date.from(string2instant("2022-07-10 00:06:00")),
//                accessKey = "SDFSDF3T9",
//                accessSecret = "F458F$%^2EDDF"
//            )
//        )


        val path =
            "C:\\Users\\ckhoi\\IdeaProjects\\MetaLake\\metalake-plugin\\build\\libs\\metalake-plugin-0.0.1-SNAPSHOT.jar"
        val absp = "file:$path"
        val m = JarFile(path).manifest
        val ma = m.mainAttributes
        val classNames = ma.keys.filter { it.toString().startsWith("PluginRootName") }.map { ma[it] }.toList()
        println(classNames)

        val urlClassLoader = URLClassLoader(arrayOf(URL(absp)))
        val driver = urlClassLoader.loadClass("me.ckhoidea.lakeplugin.SimplePlugin")
        val instance = driver.getDeclaredConstructor().newInstance()
        println(driver.declaredMethods.forEach { println(it.name) })

        val met: Method = driver.getDeclaredMethod("plus", Int::class.java, Int::class.java)
        println(met.invoke(instance, 1, 2))
        urlClassLoader.close()
    }
}

fun main(args: Array<String>) {
    SpringApplication.run(MainApp::class.java, *args)
}