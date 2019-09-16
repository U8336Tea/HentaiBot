package shared

import bot.discord.ImageSender
import bot.discord.Listener
import bot.discord.commands.*
import bot.discord.config.Configuration
import bot.discord.database.GuildTable
import images.danbooru.DanbooruAPI
import images.gelbooru.GelbooruAPI
import com.jagrosh.jdautilities.commandclient.CommandClientBuilder
import images.realbooru.RealbooruAPI
import net.dv8tion.jda.core.AccountType
import net.dv8tion.jda.core.JDA
import net.dv8tion.jda.core.JDABuilder
import net.dv8tion.jda.core.OnlineStatus
import java.util.*

//https://discordapp.com/api/oauth2/authorize?client_id=364067025850728449&scope=bot&permissions=0x4c00
fun main(args: Array<String>) {
	val settings = Configuration.settings

	val apis = arrayListOf(DanbooruAPI, GelbooruAPI, RealbooruAPI)

	GuildTable.clearUnusedTags()

	val clientBuilder = CommandClientBuilder()
			.addCommands(AddTags, ListTags, RemoveTags, ResetTags, ResetSentImages)
			.addCommands(BlacklistTags, ListBlacklistedTags, UnblacklistTags, ResetBlacklist)
			.addCommands(GetPictureNumber, SetPictureNumber)
			.addCommands(SendPicture)
			.addCommands(ChangeChannel, SayChannel)
			.addCommands(QuitBot)
			.setOwnerId(settings.ownerId)
			.setPrefix(settings.prefix)

	if (settings.altPrefix != null) {
		clientBuilder.setAlternativePrefix(settings.altPrefix)
	}

	val client = clientBuilder.build()

	val bot = JDABuilder(AccountType.BOT)
			.setStatus(OnlineStatus.ONLINE)
			.setToken(settings.token)
			.addEventListener(client, Listener)
			.buildAsync()

	val sender = ImageSender(bot, apis)

	//Do nothing until the bot is ready
	while (bot.status != JDA.Status.CONNECTED) {
		//Prevent the compiler from optimizing out this loop
		print("")
	}

	val errorChannel = bot.getUserById(settings.ownerId).openPrivateChannel().complete()
	val timer = Timer()

	//30 minutes to milliseconds: https://www.google.com/search?q=30%20minutes%20to%20milliseconds
	timer.scheduleAtFixedRate(object : TimerTask() {
		override fun run() {
			try {
				sender.sendImages(10)
			} catch (e: Throwable) {
				e.printStackTrace()
				e.sendToChannel(errorChannel)

				this.run()
			}
		}
	}, 1000, 1.8E6.toLong())

	//2 hours
	timer.scheduleAtFixedRate(object : TimerTask() {
		override fun run() {
			try {
				GuildTable.clearUnusedTags()
			} catch (e: Throwable) {
				e.printStackTrace()

				this.run()
			}
		}
	}, 0, 7.2E6.toLong())
}
