package bot.discord.commands.general

import java.util.regex.Pattern

//https://stackoverflow.com/a/3366634
fun String.split(): ArrayList<String> {
	val list = arrayListOf<String>()
	val m = Pattern.compile("\"([^\"]*)\"|(\\S+)").matcher(this)

	while (m.find()) {
		if (m.group(1) != null) {
			list.add(m.group(1))
		} else {
			list.add(m.group(2))
		}
	}

	return list
}