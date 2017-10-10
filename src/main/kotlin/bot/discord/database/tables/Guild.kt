package bot.discord.database.tables

import org.jetbrains.exposed.sql.*

internal object Guild : Table() {
	val id = integer("id").autoIncrement().primaryKey()
	val nsfwChannelId = varchar("channel_id", 30).nullable()
	val guildId = varchar("guild_id", 30)
	val imageNumber = integer("image_number").default(10)
}