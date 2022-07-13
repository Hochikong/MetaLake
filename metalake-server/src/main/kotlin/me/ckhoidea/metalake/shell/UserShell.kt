package me.ckhoidea.metalake.shell

import me.ckhoidea.metalake.domain.AuthHashEntity
import me.ckhoidea.metalake.domain.LakeBindingEntity
import me.ckhoidea.metalake.domain.PluginEntity
import me.ckhoidea.metalake.repository.AuthHashRepository
import me.ckhoidea.metalake.repository.AuthRepository
import me.ckhoidea.metalake.repository.LakeBindingRepository
import me.ckhoidea.metalake.repository.PluginRepository
import me.ckhoidea.metalake.service.AccessKeyValidService
import me.ckhoidea.metalake.service.TokenFactoryService
import me.ckhoidea.metalake.service.PasswordValidService
import me.ckhoidea.metalake.utils.generateUIDByString
import me.ckhoidea.metalake.utils.getCurrentDatetimeAsDate
import me.ckhoidea.metalake.utils.string2MD5
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.shell.standard.ShellComponent
import org.springframework.shell.standard.ShellMethod
import org.springframework.shell.standard.ShellOption

@ShellComponent

class UserShell(
    @Autowired val authRepo: AuthRepository,
    @Autowired val authHashRepo: AuthHashRepository,
    @Autowired val lakeBindingRepo: LakeBindingRepository,
    @Autowired val pluginRepo: PluginRepository,
    @Autowired val tokenService: TokenFactoryService,
    @Autowired val passValidService: PasswordValidService,
    @Autowired val keyValidService: AccessKeyValidService
) {
//    @ShellMethod(
//        value = """
//        Create new access key, token and hash
//        Usage: /new-token -password 1234
//        Options: -P/--password
//                         """,
//        key = ["/new-token"], group = "Management"
//    )
//    fun newAccessToken(@ShellOption("-P", "--password") password: String) {
//        if (passValidService.isValidPassword(password)) {
//            hashService.generateNewHash()
//            println("Done, execute 'clear' to clean your input history")
//        } else{
//            println("Password error or main password not exists")
//        }
//    }

    @ShellMethod(
        value = """
        Create(one once) new main password hash
        Usage: /new-password -password 1234
        Options: -P/--password 
    """, key = ["/new-password"], group = "Management"
    )
    fun newPasswordHash(@ShellOption("-P", "--password") password: String) {
        val mainHash = authHashRepo.findAll().filter { it.isMainHash }.map { it.hash }.toList()
        if (mainHash.isNotEmpty()) {
            println("A main password hash exists")
        } else {
            authHashRepo.save(
                AuthHashEntity(
                    createTime = getCurrentDatetimeAsDate(),
                    updateTime = getCurrentDatetimeAsDate(),
                    hash = string2MD5("FryK2njKm5:^05", password),
                    isMainHash = true
                )
            )
            println("Done, execute 'clear' to clean your input history")
        }
    }

    @ShellMethod(
        value = """
            List all lakes by main password
            Usage: /ls-lakes -P 1234 
            Options: -P
        """, key = ["/ls-lakes"], group = "Service"
    )
    fun lsLakes(@ShellOption("-P") password: String) {
        if (passValidService.isValidPassword(password)) {
            val maps = lakeBindingRepo.findAll().map {
                mapOf(
                    "ID" to it.id,
                    "AccessKey" to it.accessKey,
                    "DataSource" to it.dataSource,
                    "DataSourceDesc" to it.dataSourceDesc,
                    "DataSourceName" to it.dataSourceName,
                    "PluginUID" to it.pluginUID
                )
            }.toList()
            if (maps.isEmpty()) {
                println("Empty")
                return
            }
            for (map in maps) {
                println("--------------------------------------------------------------------")
                for (entry in map.entries) {
                    println("${entry.key}: ${entry.value}")
                }
                println("--------------------------------------------------------------------")
                println()
            }

        } else {
            println("Password error or main password not exists")
        }
    }

    @ShellMethod(
        value = """
            List all plugins by main password
            Usage: /ls-plugins -P 1234 
            Options: -P
        """, key = ["/ls-plugins"], group = "Service"
    )
    fun lsPlugins(@ShellOption("-P") password: String) {
        if (passValidService.isValidPassword(password)) {
            val maps = pluginRepo.findAll().map {
                mapOf(
                    "ID" to it.id,
                    "JarPath" to it.jarPath,
                    "ClassName" to it.nameClass
                )
            }.toList()
            if (maps.isEmpty()) {
                println("Empty")
                return
            }
            for (map in maps) {
                println("--------------------------------------------------------------------")
                for (entry in map.entries) {
                    println("${entry.key}: ${entry.value}")
                }
                println("--------------------------------------------------------------------")
                println()
            }

        } else {
            println("Password error or main password not exists")
        }
    }

    @ShellMethod(
        value = """
            List all lakes by main password
            Usage: /ls-auths -P 1234 
            Options: -P
        """, key = ["/ls-auths"], group = "Service"
    )
    fun lsAuths(@ShellOption("-P") password: String) {
        if (passValidService.isValidPassword(password)) {
            val maps = authRepo.findAll().map {
                mapOf(
                    "ID" to it.id,
                    "AccessKey" to it.accessKey,
                    "AccessSecret" to it.accessSecret,
                )
            }.toList()
            if (maps.isEmpty()) {
                println("Empty")
                return
            }
            for (map in maps) {
                println("--------------------------------------------------------------------")
                for (entry in map.entries) {
                    println("${entry.key}: ${entry.value}")
                }
                println("--------------------------------------------------------------------")
                println()
            }

        } else {
            println("Password error or main password not exists")
        }
    }

    @ShellMethod(
        value = """
            Create new meta-lake
            Usage: /new-lake -P 1234 -S jdbc:xxxxx -SN MyData -SD MyData's Content --plugin PLUGIN_UID --cred JACK:JACKPASSWORD
            Options: -P Main password
                     -S Data source's url
                     -SN Data source's name
                     -SD Data source's decription
                     --plugin Exists plugin uuid (by default is empty)
                     --cred USERNAME:PASSWORD
        """, key = ["/new-lake"], group = "Service"
    )
    fun registerLake(
        @ShellOption("-P") password: String,
        @ShellOption("-S") dataURL: String,
        @ShellOption("-N") dataSourceName: String,
        @ShellOption("-D") dataSourceDesc: String,
        @ShellOption("--plugin", defaultValue = "") pluginID: String,
        @ShellOption("--cred", defaultValue = "") cred: String
    ) {
        if (passValidService.isValidPassword(password)) {
            val token = tokenService.generateNewAccessToken()
            println("You accessKey is ${token["Key"]}, secret is ${token["Secret"]}")
            if (":" in cred) {
                val dbUsername = cred.split(":")[0]
                val dbPassword = cred.split(":")[1]
                lakeBindingRepo.save(
                    LakeBindingEntity(
                        createTime = getCurrentDatetimeAsDate(),
                        updateTime = getCurrentDatetimeAsDate(),
                        accessKey = token["Key"]!!,
                        dataSource = dataURL,
                        dataSourceDesc = dataSourceDesc,
                        dataSourceName = dataSourceName,
                        pluginUID = pluginID,
                        username = dbUsername,
                        password = dbPassword
                    )
                )
            } else {
                lakeBindingRepo.save(
                    LakeBindingEntity(
                        createTime = getCurrentDatetimeAsDate(),
                        updateTime = getCurrentDatetimeAsDate(),
                        accessKey = token["Key"]!!,
                        dataSource = dataURL,
                        dataSourceDesc = dataSourceDesc,
                        dataSourceName = dataSourceName,
                        pluginUID = pluginID
                    )
                )
            }
            println("Done")
        } else {
            println("Password error or main password not exists")
        }
    }

    @ShellMethod(
        value = """
            Register meta-lake plugin
            Usage: /new-plugin -P 1234 -J "C:\\\PATH\\\xxx.jar" -N me.xxxx.Plugin -V "1.20"
            Options: -P Main password
                     -J Jar file path
                     -N Class name of plugin
                     -V Version
        """, key = ["/new-plugin"], group = "Service"
    )
    fun registerPlugin(
        @ShellOption("-P") password: String,
        @ShellOption("-J") jarPath: String,
        @ShellOption("-N") nameClass: String,
        @ShellOption("-V") version: String
    ) {
        if (passValidService.isValidPassword(password)) {
            val pluginUID = generateUIDByString("$jarPath $nameClass")
            pluginRepo.save(
                PluginEntity(
                    createTime = getCurrentDatetimeAsDate(),
                    updateTime = getCurrentDatetimeAsDate(),
                    jarPath = jarPath,
                    nameClass = nameClass,
                    pluginUID = pluginUID,
                    version = version
                )
            )
            println("Plugin UID is: $pluginUID")
            println("Done")

        } else {
            println("Password error or main password not exists")
        }
    }

}