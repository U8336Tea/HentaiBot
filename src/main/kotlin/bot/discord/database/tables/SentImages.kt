package bot.discord.database.tables

import org.jetbrains.exposed.sql.Table

object SentImages : Table() {
	val id = integer("id").autoIncrement().primaryKey()
	val url = varchar("url", 200)
}