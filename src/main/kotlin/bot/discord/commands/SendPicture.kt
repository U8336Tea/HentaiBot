package bot.discord.commands

import bot.discord.ImageSender
import bot.discord.exceptions.*
import com.jagrosh.jdautilities.commandclient.Command
import com.jagrosh.jdautilities.commandclient.CommandEvent
import kotlinx.coroutines.experimental.async
import net.dv8tion.jda.core.Permission

object SendPicture : Command() {
	init {
		this.name = "SendPicture"
		this.help = "Sends a picture with your tags."
		this.aliases = arrayOf("pic")
		this.botPermissions = arrayOf(Permission.MESSAGE_WRITE, Permission.MESSAGE_EMBED_LINKS)
	}

	override fun execute(event: CommandEvent) {
		// This line has to be here for replyError to work. Don't ask why
		event.reply("")
		val sender = ImageSender.default

		if (sender == null) {
			event.replyError("Could not send an image!")
			return
		}

		if (event.args.isNotBlank()) {
			val num = try {
				event.args.toInt()
			} catch (e: NumberFormatException) {
				event.replyError("This is not a valid number!")
				return
			}

			try {
				sender.sendImage(event.guild, 2, images = num)
			} catch (e: NoTagException) {
				event.replyError("You need a tag!")
			} catch (e: NoPictureException) {
				event.replyError("No pictures for tag ${e.tag}!")
			} catch (e: MissingChannelException) {
				event.replyError("You must have an NSFW channel!")
			}
		} else {
			try {
				sender.sendImage(event.guild, 2)
			} catch (e: NoTagException) {
				event.replyError("You need a tag!")
			} catch (e: NoPictureException) {
				event.replyError("No pictures for tag ${e.tag}!")
			} catch (e: MissingChannelException) {
				event.replyError("You must have an NSFW channel!")
			}
		}
	}
}