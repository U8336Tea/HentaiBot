package images.general

import org.apache.http.client.methods.HttpGet
import org.apache.http.client.methods.HttpRequestBase
import org.apache.http.impl.client.HttpClients
import java.io.ByteArrayInputStream
import java.io.IOException
import java.io.InputStream

fun request(url: HttpRequestBase, retries: Int = 0): InputStream {
	url.addHeader("User-Agent", "HentaiBot/0.1")

	val client = HttpClients.createDefault()
	val ent = try {
		client.execute(url).entity
	} catch (e: IOException) {
		if (retries >= 10) throw e

		return request(url, retries)
	}
	val data = arrayListOf<Byte>()

	while (true) {
		val byte = ent.content.read()
		if (byte == -1) break

		data.add(byte.toByte())
	}

	return ByteArrayInputStream(data.toByteArray())
}

fun get(url: String): InputStream {
	return request(HttpGet(url))
}