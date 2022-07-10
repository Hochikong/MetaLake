package me.ckhoidea.metalake.shell

import me.ckhoidea.metalake.domain.AuthHashEntity
import me.ckhoidea.metalake.repository.AuthHashRepository
import me.ckhoidea.metalake.service.GenerateHashService
import me.ckhoidea.metalake.utils.getCurrentDatetimeAsDate
import me.ckhoidea.metalake.utils.string2MD5
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.shell.standard.ShellComponent
import org.springframework.shell.standard.ShellMethod
import org.springframework.shell.standard.ShellOption

@ShellComponent

class UserShell(
    @Autowired hashService: GenerateHashService,
    @Autowired authHashRepo: AuthHashRepository
) {
    private val service = hashService
    private val authHashRepo = authHashRepo


    @ShellMethod(
        value = """
        Create new access key, token and hash
        Usage: /new-token -password 1234
        Options: -P/--password 
                         """,
        key = ["/new-token"], group = "Management"
    )
    fun newAccessToken(@ShellOption("-P", "--password") password: String) {
        val mainHash = authHashRepo.findAll().filter { it.isMainHash }.map { it.hash }.toList()
        if (mainHash.isNotEmpty()) {
            if (string2MD5("FryK2njKm5:^05", password) !in mainHash) {
                println("Error password or not exists")
            } else {
                service.generateNewHash()
                println("Done, execute 'clear' to clean your input history")
            }
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


}