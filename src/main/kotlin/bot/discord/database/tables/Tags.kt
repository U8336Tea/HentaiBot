package bot.discord.database.tables

import org.jetbrains.exposed.sql.Table

internal object Tags : Table() {
	val id = integer("id").autoIncrement().primaryKey()
	val tag = varchar("tag", 50)
}