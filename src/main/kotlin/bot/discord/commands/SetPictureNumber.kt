package bot.discord.commands

import bot.discord.imageNumber
import com.jagrosh.jdautilities.commandclient.Command
import com.jagrosh.jdautilities.commandclient.CommandEvent

object SetPictureNumber : Command() {
	init {
		this.name = "SetPictureNumber"
		this.help = "Sets the number of pictures that will be sent each time"
		this.arguments = "<number>"
	}

	override fun execute(event: CommandEvent) {
		event.guild.imageNumber = try {
			event.args.toInt()
		} catch (e: NumberFormatException) {
			event.replyWarning("You must enter a valid number!")
			return
		}

		event.replySuccess("Changed the number!")
	}
}