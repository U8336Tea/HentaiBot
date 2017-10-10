package shared

import bot.discord.ImageSender
import bot.discord.Listener
import bot.discord.commands.*
import bot.discord.database.GuildTable
import images.danbooru.DanbooruAPI
import images.gelbooru.GelbooruAPI
import com.jagrosh.jdautilities.commandclient.CommandClientBuilder
import net.dv8tion.jda.core.AccountType
import net.dv8tion.jda.core.JDABuilder
import net.dv8tion.jda.core.OnlineStatus
import java.util.*

//https://discordapp.com/api/oauth2/authorize?client_id=364067025850728449&scope=bot&permissions=0x4c00
fun main(args: Array<String>) {
	val apis = arrayListOf(DanbooruAPI, GelbooruAPI)

	GuildTable.clearUnusedTags()

	val client = CommandClientBuilder()
			.addCommands(AddTags, ListTags, RemoveTags, ResetTags)
			.addCommands(BlacklistTags, ListBlacklistedTags, UnblacklistTags, ResetBlacklist)
			.addCommands(GetPictureNumber, SetPictureNumber)
			.addCommands(SendPicture)
			.addCommands(ChangeChannel, SayChannel)
			.addCommands(QuitBot)
			.setOwnerId("187676588182077451")
			.setPrefix("!!")
			.setAlternativePrefix("..")
			.build()

	val bot = JDABuilder(AccountType.BOT)
			.setStatus(OnlineStatus.ONLINE)
			.setToken ("MzY0MDY3MDI1ODUwNzI4NDQ5.DLKXWw.aIuD3iB9K2Oz5YrLmo8z82nKfJI")
			.addEventListener(client, Listener)
			.buildAsync()

	val sender = ImageSender(bot, apis)
	val timer = Timer()

	//30 minutes to milliseconds: https://www.google.com/search?q=30%20minutes%20to%20milliseconds
	timer.scheduleAtFixedRate(object : TimerTask() {
		override fun run() {
			sender.sendImages(10)
		}
	}, 0, 1.8E6.toLong())

	//2 hours
	timer.scheduleAtFixedRate(object : TimerTask() {
		override fun run() = GuildTable.clearUnusedTags()
	}, 0, 7.2E6.toLong())
}
