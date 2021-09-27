package dev.sotoestevez.allforone.ui.components.recyclerview.feed

import dev.sotoestevez.allforone.R
import dev.sotoestevez.allforone.vo.Message

/** ViewHolder for messages sent by the user */
class SentTextMessageView(data: Message): FeedView {

	override val layoutId: Int = R.layout.content_sent_message

	override val viewType: Int = FeedView.Type.SENT.ordinal

	/** Body of the message */
	val text: String = data.message

	/** Sending time of the message */
	val time: String = data.time

}