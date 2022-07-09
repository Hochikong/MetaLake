package me.ckhoidea.metalake.shell

import me.ckhoidea.metalake.service.GenerateHashService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.shell.standard.ShellComponent
import org.springframework.shell.standard.ShellMethod

@ShellComponent

class UserShell(@Autowired hashService: GenerateHashService) {
    private val service = hashService

    @ShellMethod("Create new hash")
    fun newHash() {
        service.generateNewHash()
    }
}