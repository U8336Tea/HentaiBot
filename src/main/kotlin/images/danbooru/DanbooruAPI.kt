package images.danbooru

import images.general.API
import images.general.get
import java.io.IOException
import javax.xml.bind.JAXBContext
import javax.xml.bind.UnmarshalException

object DanbooruAPI : API {
	override fun request(tags: ArrayList<String>): DanbooruRoot? {
		val tagString = tags.joinToString("+")
		val unmarshaller = JAXBContext.newInstance(DanbooruRoot::class.java).createUnmarshaller()

		val response = try {
			get("https://danbooru.donmai.us/posts.xml?tags=$tagString")
		} catch (e: IOException) {
			return null
		}

		return try {
			unmarshaller.unmarshal(response) as DanbooruRoot
		} catch (e: UnmarshalException) {
			null
		} finally {
			response.close()
		}
	}

	override fun getSortTag(by: String): String {
		return "order:$by"
	}
}