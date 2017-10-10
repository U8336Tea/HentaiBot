package bot.discord.commands

import bot.discord.tags
import com.jagrosh.jdautilities.commandclient.Command
import com.jagrosh.jdautilities.commandclient.CommandEvent
import net.dv8tion.jda.core.Permission

object ListTags : Command() {
	init {
		this.name = "ListTags"
		this.help = "Lists all tags for your guild."
		this.botPermissions = arrayOf(Permission.MESSAGE_WRITE)
	}

	override fun execute(event: CommandEvent) {
		event.reply("Tags:\n${event.guild.tags.joinToString("\n")}")
	}
}