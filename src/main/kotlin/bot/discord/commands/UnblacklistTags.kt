package bot.discord.commands

import bot.discord.commands.general.split
import bot.discord.database.GuildTable
import bot.discord.tags
import com.jagrosh.jdautilities.commandclient.Command
import com.jagrosh.jdautilities.commandclient.CommandEvent

object UnblacklistTags : Command() {
	init {
		this.name = "UnblacklistTags"
		this.help = "Removes a tag or tags from the blacklist"
		this.arguments = "<tags>"
	}

	override fun execute(event: CommandEvent) {
		val removed = event.args.split()
		GuildTable.removeBlacklistedTags(event.guild, removed)
		event.replySuccess("Tags ${removed.joinToString(", ")} unblacklisted.")
	}
}