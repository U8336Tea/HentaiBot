package bot.discord.commands

import bot.discord.commands.general.split
import bot.discord.database.GuildTable
import bot.discord.tags
import com.jagrosh.jdautilities.commandclient.Command
import com.jagrosh.jdautilities.commandclient.CommandEvent
import net.dv8tion.jda.core.Permission

object RemoveTags : Command() {
	init {
		this.name = "RemoveTags"
		this.arguments = "<tags>"
		this.help = "Removes a tag from the list."
		this.aliases = arrayOf("rmt")
	}

	override fun execute(event: CommandEvent) {
		val removed = event.args.split().filter { event.guild.tags.contains(it) }
		GuildTable.removeTags(event.guild, removed)
		event.replySuccess("Tags ${removed.joinToString(", ")} removed")
	}
}