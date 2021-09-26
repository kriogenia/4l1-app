package dev.sotoestevez.allforone.ui.components.recyclerview.messages

import dev.sotoestevez.allforone.R
import dev.sotoestevez.allforone.vo.Message

/** ViewHolder for messages received by the user */
class ReceivedTextMessageView(override val data: Message) : MessageView {

    override val layoutId: Int = R.layout.content_received_message

    override val viewType: Int = MessageView.Type.RECEIVED.ordinal

}