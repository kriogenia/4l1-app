package dev.sotoestevez.allforone.ui.components.recyclerview.feed

import dev.sotoestevez.allforone.R
import dev.sotoestevez.allforone.vo.feed.TextMessage

/** ViewHolder for messages sent by the user */
class SentTextMessageView(data: TextMessage): FeedView {

	override val id: String = data.id

	override val layoutId: Int = R.layout.content_sent_message

	override val viewType: Int = FeedView.Type.TEXT_SENT.ordinal

	/** Body of the message */
	val text: String = data.content

	/** Sending time of the message */
	val time: String = data.time

}