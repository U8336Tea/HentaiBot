package bot.discord.commands

import com.jagrosh.jdautilities.commandclient.Command
import com.jagrosh.jdautilities.commandclient.CommandEvent

object QuitBot : Command() {
	init {
		this.name = "QuitBot"
		this.ownerCommand = true
		this.help = "Quits the bot."
		this.guildOnly = false
	}

	override fun execute(event: CommandEvent) {
		event.replySuccess("Quitting bot...")
		System.exit(0)
	}
}