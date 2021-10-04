package dev.sotoestevez.allforone.ui.components.recyclerview.feed

import dev.sotoestevez.allforone.R
import dev.sotoestevez.allforone.vo.feed.TaskMessage

class ReceivedTaskMessageView(data: TaskMessage): FeedView {

    override val layoutId: Int = R.layout.content_received_task

    override val viewType: Int = FeedView.Type.TASK_RECEIVED.ordinal

    /** Body of the message */
    val text: String = data.content

    /** Sending time of the message */
    val time: String = data.time

    /** Author of the message */
    val author: String = data.submitter.displayName!!

}