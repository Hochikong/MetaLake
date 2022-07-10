package me.ckhoidea.metalake.shell

import org.jline.utils.AttributedString
import org.jline.utils.AttributedStyle
import org.springframework.shell.jline.PromptProvider
import org.springframework.stereotype.Component

@Component
class CustomShell : PromptProvider {
    override fun getPrompt(): AttributedString {
        // 定制命令提示符
        return AttributedString("MetaLake|MGMTConsole#>", AttributedStyle.DEFAULT.background(AttributedStyle.BLUE))
    }
}