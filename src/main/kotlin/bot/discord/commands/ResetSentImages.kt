package bot.discord.commands

import bot.discord.database.GuildTable
import com.jagrosh.jdautilities.commandclient.Command
import com.jagrosh.jdautilities.commandclient.CommandEvent

object ResetSentImages : Command() {
	override fun execute(event: CommandEvent) {
		GuildTable.removeSentImages(event.guild)
		event.replySuccess("Reset sent images!")
	}
}