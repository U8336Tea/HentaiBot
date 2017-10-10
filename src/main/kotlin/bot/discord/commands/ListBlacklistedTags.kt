package bot.discord.commands

import bot.discord.blacklistedTags
import com.jagrosh.jdautilities.commandclient.Command
import com.jagrosh.jdautilities.commandclient.CommandEvent

object ListBlacklistedTags : Command() {
	init {
		this.name = "ListBlacklistedTags"
		this.arguments = "<tags>"
		this.help = "Lists all blacklisted tags."
	}

	override fun execute(event: CommandEvent) {
		event.reply("Blacklisted:\n${event.guild.blacklistedTags.joinToString("\n")}")
	}
}