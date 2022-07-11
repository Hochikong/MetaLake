package me.ckhoidea.metalake.shell

import me.ckhoidea.metalake.domain.AuthHashEntity
import me.ckhoidea.metalake.repository.AuthHashRepository
import me.ckhoidea.metalake.repository.AuthRepository
import me.ckhoidea.metalake.repository.LakeBindingRepository
import me.ckhoidea.metalake.service.AccessKeyValidService
import me.ckhoidea.metalake.service.GenerateHashService
import me.ckhoidea.metalake.service.PasswordValidService
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
    @Autowired val hashService: GenerateHashService,
    @Autowired val passValidService: PasswordValidService,
    @Autowired val keyValidService: AccessKeyValidService
) {
    @ShellMethod(
        value = """
        Create new access key, token and hash
        Usage: /new-token -password 1234
        Options: -P/--password 
                         """,
        key = ["/new-token"], group = "Management"
    )
    fun newAccessToken(@ShellOption("-P", "--password") password: String) {
        if (passValidService.isValidPassword(password)) {
            hashService.generateNewHash()
            println("Done, execute 'clear' to clean your input history")
        } else {
            println("Error password or not exists")
        }
    }

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
            for (map in maps) {
                println("--------------------------------------------------------------------")
                for (entry in map.entries) {
                    println("${entry.key}: ${entry.value}")
                }
                println("--------------------------------------------------------------------")
                println()
            }

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
            for (map in maps) {
                println("--------------------------------------------------------------------")
                for (entry in map.entries) {
                    println("${entry.key}: ${entry.value}")
                }
                println("--------------------------------------------------------------------")
                println()
            }

        }
    }

    @ShellMethod(
        value = """
            List all lakes by main password
            Usage: /new-lake -P 1234 -K ACCESS_KEY -S jdbc:xxxxx -SN MyData -SD MyData's Content --plugin PLUGIN_ID 
        """, key = ["/new-lake"], group = "Service"
    )
    fun registerLake(
        @ShellOption("-P") password: String,
        @ShellOption("-K") accessKey: String,
        @ShellOption("-S") dataURL: String,
        @ShellOption("-SN") dataSourceName: String,
        @ShellOption("-SD") dataSourceDesc: String,
        @ShellOption("--plugin") pluginID: String
    ) {
        if (passValidService.isValidPassword(password)) {
            val maps = authRepo.findAll().map {
                mapOf(
                    "AccessKey" to it.accessKey,
                    "AccessSecret" to it.accessSecret,
                )
            }.toList()
            for (map in maps) {
                println("--------------------------------------------------------------------")
                for (entry in map.entries) {
                    println("${entry.key}: ${entry.value}")
                }
                println("--------------------------------------------------------------------")
                println()
            }

        }
    }

}