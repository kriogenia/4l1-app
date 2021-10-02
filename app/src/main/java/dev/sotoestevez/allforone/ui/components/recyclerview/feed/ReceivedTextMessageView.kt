package dev.sotoestevez.allforone.ui.components.recyclerview.feed

import dev.sotoestevez.allforone.R
import dev.sotoestevez.allforone.vo.feed.TextMessage

/** ViewHolder for messages received by the user */
class ReceivedTextMessageView(data: TextMessage) : FeedView {

    override val layoutId: Int = R.layout.content_received_message

    override val viewType: Int = FeedView.Type.RECEIVED.ordinal

    /** Body of the message */          // TODO extract abstract textmessageview
    val text: String = data.message

    /** Sending time of the message */
    val time: String = data.time

    /** Author of the message */
    val author: String = data.submitter.displayName!!

}