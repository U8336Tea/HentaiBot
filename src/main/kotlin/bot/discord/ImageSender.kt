package bot.discord

import bot.discord.exceptions.*
import bot.discord.database.GuildTable
import images.general.API
import images.general.Image
import net.dv8tion.jda.core.JDA
import net.dv8tion.jda.core.MessageBuilder
import net.dv8tion.jda.core.entities.Guild
import java.util.*
import java.util.Collections.disjoint

/**
 * Class that sends an image
 *
 * @param bot The bot that will send a message
 * @param apis The list of APIs to get an image from
 */
class ImageSender(private val bot: JDA, private val apis: List<API>) {
	private var apiIndex = 0

	init {
		if (ImageSender.default == null) ImageSender.default = this
	}

	companion object {
		/**
		 * The default ImageSender
		 */
		@JvmStatic
		var default: ImageSender? = null
			private set
	}

	private fun incrementAPI(increment: Int = 1) {
		// Negative increments result in OutOfBoundsException
		var i = increment
		if (i < 0) i = -i
		apiIndex += i
		apiIndex %= apis.size
	}

	private fun getUrl(image: Image): String? {
		return image.bigUrl ?: image.url ?: image.source
	}

	/**
	 * Sends images to every guild the bot joined.
	 *
	 * @param depth How many times to recurse
	 * @see sendImage
	 */
	fun sendImages(depth: Int = 0) {
 		for (guild in this.bot.guilds)  {
			try {
				this.sendImage(guild, depth, images = guild.imageNumber)
			} catch (e: NoTagException) {
			} catch (e: NoPictureException) {
			} catch (e: MissingChannelException) {
				if (!e.guild.sentNSFWMessage) {
					e.guild.messageChannel?.sendMessage("You must have an NSFW channel!")?.queue()
				}
			}
		}
	}

	/**
	 * Sends an image to a guild.
	 *
	 * @param guild The guild to send to
	 * @param depth How many images to recurse
	 * @param images How many images to send
	 *
	 * @throws NoTagException if there are no tags
	 * @throws NoPictureException if there are no pictures for a tag
	 * @throws MissingChannelException if there is no channel to send the message to
	 */
	@Throws(NoTagException::class, NoPictureException::class, MissingChannelException::class)
	fun sendImage(guild: Guild, depth: Int = 0, images: Int = 1) {
		val tags = guild.tags

		//Ensure we don't try to retrieve a nonexistent tag
		if (tags.isEmpty()) {
			throw NoTagException(guild)
		}

		val random = Random()
		val api = this.apis[apiIndex]
		val imageTag = tags[random.nextInt(tags.size)]
		val sentTags = GuildTable.getSentImages(guild)

		val imageArray = api.request(arrayListOf(imageTag, api.getSortTag("score")))
				?.images?.filter {
			//https://stackoverflow.com/a/20244275
			disjoint(it.tags, guild.blacklistedTags)
					&& !sentTags.contains(getUrl(it))
		}

		//Ensure we don't try to send a nonexistent image
		if (imageArray == null || imageArray.isEmpty()) {
			if (depth > 0) {
				incrementAPI()
				return sendImage(guild, depth - 1, images)
			}

			throw NoPictureException(guild, imageTag)
		}

		val channel = guild.nsfwChannel

		if (channel == null || !channel.isNSFW) {
			throw MissingChannelException(guild)
		}

		channel.sendTyping().queue()

		var builder = MessageBuilder().append("Pictures for tag $imageTag:\n")
		val imagesAdded = arrayListOf<String>()

		if (imageArray.size <= images) {
			var imagesSent = 0

			for (image in imageArray) {
				val url = this.getUrl(image) ?: continue

				if (imagesAdded.contains(url)) continue

				imagesSent++

				// Send the message if it is close to the size limit
				// or if we have 5 pictures already. This is because that is what Discord will show.
				if (builder.length() >= 1800 || imagesSent % 5 == 0) {
					channel.sendMessage(builder.build()).queue()
					builder = MessageBuilder()
				}

				builder.appendln(url)
				imagesAdded.add(url)
			}
		} else {
			var index = 0

			// We need 1..times so that we can split the message every 5 pictures.
			iter@ for (i in 1..images) {
				index++

				// Send the message if it is close to the size limit
				// Or if we have 5 pictures already. This is because that is what Discord will show.
				if (builder.length() >= 1800 || i % 5 == 0) {
					channel.sendMessage(builder.build()).queue()
					builder = MessageBuilder()
				}

				var image = imageArray[index]
				var url = this.getUrl(image)

				while (url == null) {
					index++
					if (index > imageArray.lastIndex) break@iter

					image = imageArray[index]
					url = this.getUrl(image)
				}

				builder.appendln(url)
				imagesAdded.add(url)
			}
		}

		GuildTable.addSentImages(guild, imagesAdded)

		if (!builder.isEmpty) channel.sendMessage(builder.build()).queue()

		// Need to fetch from a random API each time.
		incrementAPI(random.nextInt())
	}
}