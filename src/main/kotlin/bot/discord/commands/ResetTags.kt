package bot.discord.commands

import bot.discord.database.GuildTable
import com.jagrosh.jdautilities.commandclient.Command
import com.jagrosh.jdautilities.commandclient.CommandEvent
import shared.Defaults

object ResetTags : Command() {
	init {
		this.name = "ResetTags"
		this.help = "Resets all tags back to default."
	}

	override fun execute(event: CommandEvent) {
		GuildTable.removeAllTags(event.guild)
		GuildTable.addTags(event.guild, Defaults.defaultTags)
		event.replySuccess("Reset tags to default.")
	}
}