package dev.sotoestevez.allforone.ui.components.recyclerview.messages

import dev.sotoestevez.allforone.R
import dev.sotoestevez.allforone.vo.Message

/** ViewHolder for messages sent by the user */
class SentMessageTextView(override val data: Message): MessageView {

	override val layoutId: Int = R.layout.content_sent_message

	override val viewType: Int = MessageView.Type.SENT.ordinal

}