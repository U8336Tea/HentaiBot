package bot.discord.commands

import bot.discord.database.GuildTable
import bot.discord.nsfwChannel
import com.jagrosh.jdautilities.commandclient.Command
import com.jagrosh.jdautilities.commandclient.CommandEvent

object ChangeChannel : Command() {
	init {
		this.name = "ChangeChannel"
		this.arguments = "[Channel ID]"
		this.help = "Changes the channel to spam hentai"
	}

	override fun execute(event: CommandEvent) {
		val channel = if (event.args.isBlank()) {
			event.textChannel
		} else {
			event.message.mentionedChannels.elementAtOrNull(0)
		}

		if (channel == null) {
			event.replyError("This channel does not exist!")
			return
		}

		if (!channel.isNSFW) {
			event.replyError("This channel is not marked NSFW!")
			return
		}

		event.guild.nsfwChannel = channel

		event.replySuccess("Changed the channel!")
	}
}