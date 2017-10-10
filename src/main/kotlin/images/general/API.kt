package images.general

interface API {
	fun request(tags: ArrayList<String>): ImageSet?
	fun getSortTag(by: String): String
}