package dev.sotoestevez.allforone.model.feed.viewitems

import dev.sotoestevez.allforone.R
import dev.sotoestevez.allforone.data.Message
import dev.sotoestevez.allforone.model.feed.MessageViewItem

class SentMessageViewItem(val message: Message): MessageViewItem {

	override val layoutId: Int = R.layout.content_sent_message

	override val viewType: Int = MessageViewItem.ViewType.SENT.ordinal

}