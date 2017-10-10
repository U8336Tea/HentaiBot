package bot.discord.commands

import bot.discord.imageNumber
import com.jagrosh.jdautilities.commandclient.Command
import com.jagrosh.jdautilities.commandclient.CommandEvent
import net.dv8tion.jda.core.Permission

object GetPictureNumber : Command() {
	init {
		this.name = "GetPictureNumber"
		this.help = "Gets the number of pictures that will be sent each time."
		this.botPermissions = arrayOf(Permission.MESSAGE_WRITE)
	}

	override fun execute(event: CommandEvent) {
		event.reply("${event.guild.imageNumber} images will be sent!")
	}
}