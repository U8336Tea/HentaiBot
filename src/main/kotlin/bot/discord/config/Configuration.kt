package bot.discord.config

import java.io.File
import javax.xml.bind.JAXBContext

object Configuration {
	val settings by lazy {
		File("resources/config.xml").inputStream().use {
			val unmarshaller = JAXBContext.newInstance(Settings::class.java).createUnmarshaller()

			unmarshaller.unmarshal(it) as Settings
		}
	}
}