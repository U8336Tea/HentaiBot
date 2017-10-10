package bot.discord

import bot.discord.database.GuildTable
import net.dv8tion.jda.core.entities.Guild
import net.dv8tion.jda.core.entities.TextChannel

internal val sentWarningMap = mutableMapOf<Guild, Boolean>()
internal val tagGoneMap = mutableMapOf<Guild, MutableList<String>>()

val Guild.tags: List<String>
	get() = GuildTable.getTags(this)

val Guild.blacklistedTags: List<String>
	get() = GuildTable.getBlacklistedTags(this)

var Guild.nsfwChannel: TextChannel?
	get() {
		val id = GuildTable.getChannelId(this) ?: return this.textChannels.find { it.isNSFW }

		return this.getTextChannelById(id)
	}
	set(value) {
		if (value != null) GuildTable.changeChannel(this, value)
	}

val Guild.messageChannel: TextChannel?
	get() = this.defaultChannel ?: this.nsfwChannel ?: this.textChannels[0]

var Guild.sentNSFWMessage: Boolean
	get() = sentWarningMap[this] ?: false
	set(value) {
		sentWarningMap[this] = value
	}

var Guild.tagsSent: MutableList<String>
	get() {
		if (tagGoneMap[this] == null) tagGoneMap[this] = mutableListOf()

		return tagGoneMap[this]!!
	}
	set(value) {
		tagGoneMap[this] = value
	}

var Guild.imageNumber: Int
	get() = GuildTable.getImageNumber(this) ?: 10
	set(value) = GuildTable.changeImageNumber(this, value)