package bot.discord.commands

import bot.discord.nsfwChannel
import com.jagrosh.jdautilities.commandclient.Command
import com.jagrosh.jdautilities.commandclient.CommandEvent
import net.dv8tion.jda.core.MessageBuilder

object SayChannel : Command() {
	init {
		this.name = "SayChannel"
		this.help = "Puts the name of the hentai channel."
	}

	override fun execute(event: CommandEvent) {
		val message = MessageBuilder()
				.append("This bot will send messages on channel ")
				.append(event.guild.nsfwChannel)
				.build()

		event.reply(message)
	}
}