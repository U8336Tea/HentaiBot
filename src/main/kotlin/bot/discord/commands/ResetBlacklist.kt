package bot.discord.commands

import bot.discord.database.GuildTable
import com.jagrosh.jdautilities.commandclient.Command
import com.jagrosh.jdautilities.commandclient.CommandEvent
import shared.Defaults

object ResetBlacklist : Command() {
	init {
		this.name = "ResetBlacklist"
		this.help = "Resets the blacklist back to default."
	}

	override fun execute(event: CommandEvent) {
		GuildTable.removeAllBlacklistedTags(event.guild)
		GuildTable.blacklistTags(event.guild, Defaults.defaultBlacklist)

		event.replySuccess("Reset the blacklist!")
	}
}