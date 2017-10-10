package images.general

import javax.xml.bind.annotation.XmlEnum
import javax.xml.bind.annotation.XmlEnumValue

@XmlEnum
enum class ImageRating {
	@XmlEnumValue("s")
	safe,

	@XmlEnumValue("q")
	questionable,

	@XmlEnumValue("e")
	explicit
}