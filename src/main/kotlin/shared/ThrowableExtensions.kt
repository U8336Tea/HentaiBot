package shared

import net.dv8tion.jda.core.MessageBuilder
import net.dv8tion.jda.core.entities.MessageChannel

fun Throwable.sendToChannel(channel: MessageChannel) {
	val builder = MessageBuilder()
			.append(this)
			.append("\n")

	for (element in this.stackTrace) {
		builder
				.append("\t")
				.append(element)
				.append("\n")
	}

	channel.sendMessage(builder.build()).queue()
}