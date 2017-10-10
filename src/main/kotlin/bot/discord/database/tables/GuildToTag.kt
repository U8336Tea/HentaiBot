package bot.discord.database.tables

import org.jetbrains.exposed.sql.Table

internal object GuildToTag : GuildTagTable() {
	override val id = integer("id").autoIncrement().primaryKey()
	override val guild = integer("guild_id") references Guild.id
	override val tag = integer("tag_id") references Tags.id
}