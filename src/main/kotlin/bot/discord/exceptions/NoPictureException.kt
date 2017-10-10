package bot.discord.exceptions

import net.dv8tion.jda.core.entities.Guild

class NoPictureException(val guild: Guild, val tag: String) : Exception("No picture for tag $tag")