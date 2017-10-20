package shared

import net.dv8tion.jda.core.MessageBuilder
import net.dv8tion.jda.core.entities.MessageChannel
import java.io.OutputStream

class MessageChannelStream(private val channel: MessageChannel) : OutputStream() {
	private val builder = MessageBuilder()

	override fun write(b: Int) {
		builder.append(b.toChar())
	}

	override fun flush() {
		channel.sendMessage(builder.build()).queue()
	}

	override fun close() {
		this.flush()
	}
}
