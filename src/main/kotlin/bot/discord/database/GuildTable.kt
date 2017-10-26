package bot.discord.database

import bot.discord.config.Configuration
import bot.discord.database.tables.*
import bot.discord.database.tables.GuildToSentImages.guild
import bot.discord.database.tables.SentImages.url
import bot.discord.database.tables.Tags.tag
import com.sun.xml.internal.fastinfoset.alphabet.BuiltInRestrictedAlphabets.table
import org.jetbrains.exposed.sql.*
import net.dv8tion.jda.core.entities.Guild as DiscordGuild
import net.dv8tion.jda.core.entities.TextChannel as DiscordTextChannel
import org.jetbrains.exposed.sql.SchemaUtils.create
import org.jetbrains.exposed.sql.transactions.transaction

object GuildTable {
	init {
		val settings = Configuration.settings

		Database.connect(settings.databaseUrl, settings.databaseDriver)

		transaction {
			create(Guild, Tags, GuildToTag, GuildTagBlacklist)
		}
	}

	private fun add(id: String, table: GuildTagTable, tags: List<String>) {
		transaction {
			//Gets the ID in the table or inserts it.
			val guildId = Guild.select {
				Guild.guildId eq id
			}.map { it[Guild.id] }.elementAtOrNull(0) ?: Guild.insert {
				it[guildId] = id
			} get Guild.id

			for (searchTag in tags) {
				val query = Tags.select { Tags.tag eq searchTag }

				if (query.empty()) {
					val tagId = Tags.insert {
						it[tag] = searchTag
					} get Tags.id

					table.insert {
						it[guild] = guildId
						it[tag] = tagId
					}
				} else {
					table.insert {
						it[guild] = guildId
						it[tag] = query.iterator().next()[Tags.id]
					}
				}
			}
		}
	}

	fun addTags(server: DiscordGuild, tags: List<String>) {
		add(server.id, GuildToTag, tags)
	}

	fun addSentImages(server: DiscordGuild, urls: List<String>) {
		transaction {
			//Gets the ID in the table or inserts it.
			val guildId = Guild.select {
				Guild.guildId eq server.id
			}.map { it[Guild.id] }.elementAtOrNull(0) ?: Guild.insert {
				it[guildId] = server.id
			} get Guild.id

			val query = SentImages.select { SentImages.url eq url }

			for (url in urls) {
				if (query.empty()) {
					val imageId = SentImages.insert {
						it[tag] = url
					} get SentImages.id

					GuildToSentImages.insert {
						it[guild] = guildId
						it[image] = imageId
					}
				} else {
					GuildToSentImages.insert {
						it[guild] = guildId
						it[image] = query.iterator().next()[SentImages.id]
					}
				}
			}
		}
	}

	fun blacklistTags(server: DiscordGuild, tags: List<String>) {
		add(server.id, GuildTagBlacklist, tags)
	}

	fun removeServer(server: DiscordGuild) {
		transaction {
			val ids = Guild.select {
				Guild.guildId eq server.id
			}.map { it[Guild.id] }

			Guild.deleteWhere {
				Guild.guildId eq server.id
			}

			GuildToTag.deleteWhere {
				GuildToTag.guild inList ids
			}

			GuildTagBlacklist.deleteWhere {
				GuildTagBlacklist.guild inList ids
			}
		}

		this.clearUnusedTags()
	}

	fun removeTags(server: DiscordGuild, tags: List<String>) {
		transaction {
			val ids = Guild.select {
				Guild.guildId eq server.id
			}.map { it[Guild.id] }

			val tagids = Tags.select {
				Tags.tag inList tags
			}.map { it[Tags.id] }

			GuildToTag.deleteWhere {
				(GuildToTag.guild inList ids) and (GuildToTag.tag inList tagids)
			}
		}
	}

	fun removeAllTags(server: DiscordGuild) {
		transaction {
			val ids = Guild.select {
				Guild.guildId eq server.id
			}.map { it[Guild.id] }

			GuildToTag.deleteWhere {
				GuildToTag.guild inList ids
			}
		}
	}

	fun removeBlacklistedTags(server: DiscordGuild, tags: List<String>) {
		transaction {
			val ids = Guild.select {
				Guild.guildId eq server.id
			}.map { it[Guild.id] }

			val tagIds = Tags.select {
				Tags.tag inList tags
			}.map { it[Tags.id] }

			GuildTagBlacklist.deleteWhere {
				(GuildTagBlacklist.guild inList ids) and (GuildTagBlacklist.tag inList tagIds)
			}
		}
	}

	fun removeAllBlacklistedTags(server: DiscordGuild) {
		transaction {
			val ids = Guild.select {
				Guild.guildId eq server.id
			}.map { it[Guild.id] }

			GuildTagBlacklist.deleteWhere {
				GuildTagBlacklist.guild inList ids
			}
		}
	}

	fun changeChannel(server: DiscordGuild, textChannel: DiscordTextChannel) {
		transaction {
			Guild.update({
				Guild.guildId eq server.id
			}, 1, {
				it[nsfwChannelId] = textChannel.id
			})
		}
	}

	fun changeImageNumber(server: DiscordGuild, number: Int) {
		transaction {
			Guild.update({
				Guild.guildId eq server.id
			}, null, {
				it[imageNumber] = number
			})
		}
	}

	fun getTags(server: DiscordGuild): List<String> {
		return transaction {
			((GuildToTag innerJoin Guild) innerJoin Tags).select {
				Guild.guildId eq server.id
			}.map { it[Tags.tag] }
		}
	}

	fun getChannelId(server: DiscordGuild): String? {
		return transaction {
			val ids = Guild.select {
				Guild.guildId eq server.id
			}.limit(1).map { it[Guild.nsfwChannelId] }

			if (ids.isEmpty()) return@transaction null
			return@transaction ids[0]
		}
	}

	fun getImageNumber(server: DiscordGuild): Int? {
		return transaction {
			return@transaction Guild.select {
				Guild.guildId eq server.id
			}.map { it[Guild.imageNumber] }.elementAtOrNull(0)
		}
	}

	fun getBlacklistedTags(server: DiscordGuild): List<String> {
		return transaction {
			((GuildTagBlacklist innerJoin Guild) innerJoin Tags).select {
				Guild.guildId eq server.id
			}.map { it[Tags.tag] }
		}
	}

	fun getSentImages(server: DiscordGuild): List<String> {
		return transaction {
			((GuildToSentImages innerJoin Guild) innerJoin SentImages).select {
				Guild.guildId eq server.id
			}.map { it[SentImages.url] }
		}
	}

	fun clearUnusedTags() {
		transaction {
			val tagIds = GuildToTag.selectAll().map { it[GuildToTag.tag] }

			val blacklistIds = GuildTagBlacklist.selectAll().map { it[GuildTagBlacklist.tag] }

			Tags.deleteWhere {
				(Tags.id notInList tagIds) and (Tags.id notInList blacklistIds)
			}
		}
	}
}