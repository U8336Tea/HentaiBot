package bot.discord.exceptions

import net.dv8tion.jda.core.entities.Guild

class NoTagException(val guild: Guild, message: String = "No tag.") : Exception(message)