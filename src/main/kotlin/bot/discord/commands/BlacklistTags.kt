package bot.discord.commands

import bot.discord.commands.general.split
import bot.discord.database.GuildTable
import com.jagrosh.jdautilities.commandclient.Command
import com.jagrosh.jdautilities.commandclient.CommandEvent

object BlacklistTags : Command() {
	init {
		this.name = "BlacklistTags"
		this.arguments = "<tags>"
		this.help = "Blacklists a tag or tags"
	}

	override fun execute(event: CommandEvent) {
		val arguments = event.args.split()
		GuildTable.blacklistTags(event.guild, arguments)
		event.replySuccess("Blacklisted tags: ${arguments.joinToString(", ")}.")
	}
}