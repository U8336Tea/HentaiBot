package images.realbooru

import images.gelbooru.GelbooruRoot
import images.general.API
import images.general.ImageSet
import images.general.get
import java.io.IOException
import javax.xml.bind.JAXBContext
import javax.xml.bind.UnmarshalException

object RealbooruAPI : API {
	override fun request(tags: ArrayList<String>): GelbooruRoot? {
		val tagString = tags.joinToString("+")
		val unmarshaller = JAXBContext.newInstance(GelbooruRoot::class.java).createUnmarshaller()

		val response = try {
			get("https://realbooru.com/index.php?page=dapi&s=post&q=index&tags=$tagString")
		} catch (e: IOException) {
			return null
		}

		return try {
			unmarshaller.unmarshal(response) as GelbooruRoot
		} catch (e: UnmarshalException) {
			null
		} finally {
			response.close()
		}
	}

	override fun getSortTag(by: String): String {
		return "sort:$by"
	}
}