package bot.discord.database.tables

import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.Table

internal abstract class GuildTagTable(name: String = "") : Table(name) {
	abstract val id: Column<Int>
	abstract val guild: Column<Int>
	abstract val tag: Column<Int>
}