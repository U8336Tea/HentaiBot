package bot.discord.exceptions

import net.dv8tion.jda.core.entities.Guild

class MissingChannelException(val guild: Guild, message: String = "No NSFW channel could be found!")
	: Exception(message)