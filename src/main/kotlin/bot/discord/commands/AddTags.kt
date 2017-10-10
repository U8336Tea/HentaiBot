package bot.discord.commands

import bot.discord.commands.general.split
import bot.discord.database.GuildTable
import com.jagrosh.jdautilities.commandclient.Command
import com.jagrosh.jdautilities.commandclient.CommandEvent
import net.dv8tion.jda.core.Permission

object AddTags : Command() {
	init {
		this.name = "AddTags"
		this.arguments = "<tags>"
		this.help = "Adds a tag to the list."
		this.botPermissions = arrayOf(Permission.MESSAGE_READ, Permission.MESSAGE_WRITE)
	}

	override fun execute(event: CommandEvent) {
		val arguments = event.args.split()
		GuildTable.addTags(event.guild, arguments)
		event.replySuccess("Added tags: ${arguments.joinToString(", ")}.")
	}
}