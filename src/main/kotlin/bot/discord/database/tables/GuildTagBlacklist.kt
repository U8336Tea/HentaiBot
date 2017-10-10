package bot.discord.database.tables

internal object GuildTagBlacklist : GuildTagTable("GUILD_TO_BLACKLISTED_TAG") {
	override val id = integer("id").autoIncrement().primaryKey()
	override val guild = integer("guild_id") references Guild.id
	override val tag = integer("tag_id") references Tags.id
}