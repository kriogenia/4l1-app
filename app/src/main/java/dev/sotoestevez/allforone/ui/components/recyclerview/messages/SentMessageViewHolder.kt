package dev.sotoestevez.allforone.ui.components.recyclerview.messages

import dev.sotoestevez.allforone.R
import dev.sotoestevez.allforone.vo.Message
import dev.sotoestevez.allforone.ui.components.recyclerview.ViewHolderWrapper

/**
 * ViewHolder for SentMessages
 */
class SentMessageViewHolder(override val data: Message): ViewHolderWrapper<Message> {

	override val layoutId: Int = R.layout.content_sent_message

	override val viewType: Int = ViewMessageType.SENT.ordinal

}