package bot.discord.database.tables

import org.jetbrains.exposed.sql.Table

internal object GuildToSentImages : Table() {
	val id = integer("id").autoIncrement().primaryKey()
	val guild = integer("guild_id") references Guild.id
	val image = integer("image_id") references SentImages.id
}