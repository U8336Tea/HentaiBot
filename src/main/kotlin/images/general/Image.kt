package images.general

interface Image {
	val url: String?
	val bigUrl: String?
	val score: Int
	val rating: ImageRating
	val source: String?
	val tags: List<String>
}