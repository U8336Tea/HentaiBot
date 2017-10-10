package bot.discord

import bot.discord.database.GuildTable
import net.dv8tion.jda.core.events.guild.GuildJoinEvent
import net.dv8tion.jda.core.events.guild.GuildLeaveEvent
import net.dv8tion.jda.core.hooks.ListenerAdapter
import kotlinx.coroutines.experimental.async
import net.dv8tion.jda.core.events.guild.GuildUnavailableEvent
import shared.Defaults

object Listener : ListenerAdapter() {
	override fun onGuildJoin(event: GuildJoinEvent) {
		if (event.guild.tags.isEmpty()) {
			GuildTable.addTags(event.guild, Defaults.defaultTags)
		}

		if (event.guild.blacklistedTags.isEmpty()) {
			GuildTable.blacklistTags(event.guild, Defaults.defaultBlacklist)
		}
	}

	override fun onGuildLeave(event: GuildLeaveEvent) {
		async {
			GuildTable.removeServer(event.guild)
		}
	}
}