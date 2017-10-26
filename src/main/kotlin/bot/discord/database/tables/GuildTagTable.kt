package bot.discord.database.tables

import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.Table

internal abstract class GuildTagTable(name: String = "") : Table(name) {
	open val id = integer("id").autoIncrement().primaryKey()
	open val guild = integer("guild_id") references Guild.id
	open val tag = integer("tag_id") references Tags.id
}